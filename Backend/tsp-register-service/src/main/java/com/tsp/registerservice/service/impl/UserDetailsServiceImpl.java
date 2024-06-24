package com.tsp.registerservice.service.impl;

import com.tsp.registerservice.client.NotificationClient;
import com.tsp.registerservice.client.UserserviceClient;
import com.tsp.registerservice.config.MasterData;
import com.tsp.registerservice.dto.*;
import com.tsp.registerservice.entities.UserDetails;
import com.tsp.registerservice.exception.CustomExceptions;
import com.tsp.registerservice.exception.InvalidCredentialsException;
import com.tsp.registerservice.exception.InvalidInputException;
import com.tsp.registerservice.exception.UserNotFoundException;
import com.tsp.registerservice.mapper.UserDetailsMapper;
import com.tsp.registerservice.repository.UserDetailsRepository;
import com.tsp.registerservice.service.IUserDetailsService;
import com.tsp.registerservice.util.CommonUtils;
import com.tsp.registerservice.util.Constants;
import com.tsp.registerservice.util.PasswordGenerator;
import com.tsp.registerservice.dto.*;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * UserDetailsServiceImpl class for all user CRUD operations & search, fetching master data
* */
@Service
@Slf4j
@Transactional
public class UserDetailsServiceImpl implements IUserDetailsService {
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private NotificationClient notificationClient;

    @Autowired
    private UserDetailsMapper userDetailsMapper;

    @Autowired
    private MasterData masterData;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private UserserviceClient userserviceClient;

    @Value("${tsp.email.register-success-body}")
    private String registerSuceessMailBody;

    @Value("${tsp.email.forgot-password-body}")
    private String forgotPasswordMailBody;

    @Value("${tsp.email.registration-mail-subject}")
    private String registrationSubject;

    @Value("${tsp.email.forgot-password-mail-subject}")
    private String forgotPasswordSubject;


    /**
     * Authenticates a user by performing login with provided credentials.
     *
     * @param loginDto The LoginDto contains mailId of the user attempting to login and password associated with that maildId.
     * @return UserDetailsDto if the login is successfull with provided valid credentials.
     * @throws UserNotFoundException if the authentication fails due to invalid maild Id provided.
     * @throws InvalidCredentialsException if the authentication fails due to incorrect password Id provided.
     * @throws  Exception If there is another authentication-related issue.
     *
     * @see IUserDetailsService#loginUser(LoginDto)
    * */
    @Override
    public UserDetailsDto loginUser(LoginDto loginDto) {
        log.info("Login request for maild :{}",loginDto.getEmailId());
        UserDetails userDetails= userDetailsRepository.findByEmailIdAndStatusTrue(loginDto.getEmailId())
                .orElseThrow(()->new UserNotFoundException("User Not Found with emailId:" + loginDto.getEmailId()));
        if (!decryptPassword(userDetails.getPassword()).equals(decryptPassword(loginDto.getPassword())))
            throw new InvalidCredentialsException("Entered Incorrect Password");
        log.info("Login successfull :{}",loginDto.getEmailId());
        return userDetailsMapper.fromUserDetailsToUserDetailsDto(userDetails);
    }

    /**
     *  Registers new user with provided registration information.
     *
     * @param registerDto The registerDto contains all registration entered by user.
     * @return UserDetailsDto Containing details of registered user.
     * @throws DataIntegrityViolationException if trying to register with already existing mail Id.
     * @throws FeignException if something goes wrong while communicating with other microservices.
     * 
     * @see IUserDetailsService#registerUser(RegisterDto)
     * */
    @Override
    public UserDetailsDto registerUser(RegisterDto registerDto) {
        log.info("Inside Register");
        userDetailsRepository.findByEmailId(registerDto.getEmailId())
                .ifPresent(user->{throw new CustomExceptions("User Already Exist with MailId:"+registerDto.getEmailId());});
        UserDetails userDetails=userDetailsMapper.fromRegisterDtoToUserDetailsEntity(registerDto);
        commonUtils.validateMasterData(registerDto.getUserGroup(),registerDto.getBusinessUnit());
        userDetails.setPassword(generatePassword());
        userDetailsRepository.save(userDetails);
        EmailRequestDto emailRequestDto=getEmailRequestDtoForRegister(registerDto.getEmailId(),userDetails.getFirstName(),decryptPassword(userDetails.getPassword()),userDetails.getUserGroup());
        log.info("Sending mail Notification to mail Id:"+registerDto.getEmailId());
        notificationClient.sendEmail(emailRequestDto);
        log.info("Registered Successfully");
        return userDetailsMapper.fromUserDetailsToUserDetailsDto(userDetails);
    }

    /**
     * Updates the newly system generated password for the account
     *
     * @param MailId to which account the new system generated password has to be set.
     * @return The success message "password Reset Successfully" and sends new password via mail.
     * @throws UserNotFoundException if user not exist in the database with provide mail Id.
     * @throws FeignException if something goes wrong while communicating with other microservices.
     * 
     * @see IUserDetailsService#forgotPassword(String)
     * */
    @Override
    public String forgotPassword(String emailId) {
        log.info("Forgot password request for user acoount:{}",emailId);
        UserDetails userDetails=userDetailsRepository.findByEmailIdAndStatusTrue(emailId)
                .orElseThrow(()->new UserNotFoundException("User Not Found with mail Id:"+emailId));
        userDetails.setPassword(generatePassword());
        userDetails.setPasswordReset(true);
        userDetails.setUpdatedBy(userDetails.getEmailId());
        userDetailsRepository.save(userDetails);
        log.info("New system generated password set to :{}",emailId);
        EmailRequestDto emailRequest=getEmailRequestDtoForForgotPassword(userDetails.getEmailId(),userDetails.getFirstName(),decryptPassword(userDetails.getPassword()));
        notificationClient.sendEmail(emailRequest);
        log.info("New System generated password sent to {}",emailId);
        return Constants.FORGOT_PASSWORD_SUCCESS_MSG;
    }

    private EmailRequestDto getEmailRequestDtoForForgotPassword(String mailId,String name,String password) {
        EmailRequestDto emailRequest=new EmailRequestDto();
        emailRequest.setRecipient(mailId);
        emailRequest.setSubject(forgotPasswordSubject);
        String mailBody=forgotPasswordMailBody.replace("<Name>",name)
                        .replace("<password>",password);
        emailRequest.setMessage(mailBody);
        return emailRequest;
    }

    /**
     * makes the user status false(Inactive) and sets exit date of the users.
     *
     * @param List<DeleteRequestDto> contains mail ids and respective exit dates to delete the users.
     * @return successfull deleted message "User’s Successfully Deleted"
     * @throws InvalidInputException if input list is empty.
     *
     * @see IUserDetailsService#deleteUser(List) 
     * */
    @Override
    public String deleteUser(List<DeleteRequestDto> deleteRequestDtos) {
        log.info("Inside delete users");
        if (deleteRequestDtos.isEmpty())
            throw new InvalidInputException("Input Data is empty");
        deleteRequestDtos.stream().forEach(deleteRequestDto->{
            Optional<UserDetails> user=userDetailsRepository.findByEmailIdAndStatusTrue(deleteRequestDto.getUserMailId());
            if (user.isPresent()){
                user.get().setStatus(false);
                user.get().setExitDate(commonUtils.convertToLocaDateFormat(deleteRequestDto.getExitDate()));
                userDetailsRepository.save(user.get());
                userserviceClient.updateProfileStatus(new UserStatusDto(deleteRequestDto.getUserMailId(),Constants.PROFILE_STATUS_UNAVAILABLE));
                log.info("deleted user with mail id {}",deleteRequestDto.getUserMailId());
            }else {
                log.error("No User Found With mail Id {} to delete",deleteRequestDto.getUserMailId());
            }
        });
        return Constants.DELETE_SUCCESS_MSG;

    }

    /**
     * Removes the old password and sets new password provided by the user.
     *
     * @param ResetDto contains information mailId,newPassword & confirmPassword which are required to reset the password.
     * @return Successful reset password message "New Password has been reset successfully"
     * @throws UserNotFoundException if user not exist with provided mailId.
     * @throws InvalidCredentialsException if the password & confirmPassword doesn't match
     * 
     * @see IUserDetailsService#resetPassword(ResetDto)
     * */
    @Override
    public String resetPassword(ResetDto resetDto) {
        log.info("Password reset request for the account :{}",resetDto.getEmailId());
        UserDetails userDetails=userDetailsRepository.findByEmailIdAndStatusTrue(resetDto.getEmailId())
                .orElseThrow(()->new UserNotFoundException("User Not Found With EmailId:"+resetDto.getEmailId()));
        if (!decryptPassword(resetDto.getNewPassword()).equals(decryptPassword(resetDto.getConfirmPassword())))
            throw new InvalidCredentialsException("Passwords Mismatch");
        userDetails.setPassword(resetDto.getConfirmPassword());
        if (userDetails.isPasswordReset()){
            userDetails.setPasswordReset(false);
        }
        userDetails.setUpdatedBy(resetDto.getEmailId());
        userDetailsRepository.save(userDetails);
        log.info("Password has been reset successfully for the account:{}",resetDto.getEmailId());
        return Constants.RESET_PASSWORD_SUCCESS_MSG;
    }

    /**
     * Returns all details of the user
     *
     * @param String mailId to fetch user details for this mailId.
     * @return UserDetailsDto contains all the user details.
     * @throws UserNotFoundException if the user does not exist in data base with given mail Id.
     *
     * @see IUserDetailsService#fetchUserByEmailId(String) 
     * */
    @Override
    public UserDetailsDto fetchUserByEmailId(String emailId) {
        log.info("Fetching user with mail id:{}",emailId);
        UserDetails userDetails=userDetailsRepository.findByEmailIdAndStatusTrue(emailId)
                .orElseThrow(()->new UserNotFoundException("User Not Found With MailId:"+emailId));
        log.info("User:{}",userDetails);
        return userDetailsMapper.fromUserDetailsToUserDetailsDto(userDetails);
    }

    /**
     *  fetchesthe  stauts of the user, whether the user is active user or inactive user
     *
     * @param String mailId to fetch the status of the user.
     * @return True if the user's status true and False if the user's status is false.
     * @throws UserNotFoundException if the User does not exist in data base with provided mailId.
     * 
     * @see IUserDetailsService#getUserStatus(String)
     * */
    @Override
    public boolean getUserStatus(String emailId) {
        log.info("Fetching the status of user:{}",emailId);
        UserDetails userDetails=userDetailsRepository.findByEmailId(emailId)
                .orElseThrow(()->new UserNotFoundException("User not found with the name:"+emailId));
        log.info("Staus of {} is {}",emailId,userDetails.isStatus());
        return userDetails.isStatus();
    }

    /**
     * Fetches all the Active and Inactive Users exist in the data base.
     *
     * @return List<UserDetailsDto> UserDetailsDto contains all details of each User.
     * @see IUserDetailsService#getAllUsers()
     * */
    @Override
    public List<UserDetailsDto> getAllUsers() {
        log.info("Fetching all users");
        List<UserDetails> users=userDetailsRepository.findAll();
        if (users.isEmpty()){
            throw new UserNotFoundException("No Users Found");
        }
       return users.stream()
                .map(userDetailsMapper::fromUserDetailsToUserDetailsDto)
                .collect(Collectors.toList());
    }

    /**
     * Fetches All the Active And Inactive users exist in the data base between provided start date and end dates and departmentUnit.
     *
     * @param ReportRequestDto contains the information startDate,EndDate and department Type required to fetch users.
     * @return List<UserDetailsDto> all active and inactive users between start and end dates and department type.
     *
     * @see IUserDetailsService#fetchUsersBetweenDataRange(ReportRequestDto)
     * */
    @Override
    public List<UserDetailsDto> fetchUsersBetweenDataRange(ReportRequestDto reportRequestDto) {
        log.info("Fetching users between {} and {}",reportRequestDto.getLocalStartDate(),reportRequestDto.getLocalEndDate());
        LocalDateTime localStartDate = commonUtils.convertToLocaDateFormat(reportRequestDto.getLocalStartDate()).atStartOfDay();
        LocalDateTime localEndDate = commonUtils.convertToLocaDateFormat(reportRequestDto.getLocalEndDate()).atTime(LocalTime.MAX);
        String departmentUnit=String.valueOf(reportRequestDto.getDepartmentUnit());
        if(departmentUnit==null || departmentUnit.isBlank() || departmentUnit.isEmpty()) {
            List<UserDetails> users = Optional.ofNullable(userDetailsRepository.findByCreatedDateTimeBetween(localStartDate, localEndDate))
                    .orElseThrow(()->new UserNotFoundException("No users are available between "+localStartDate+" and "+localEndDate));
            return users.stream()
                    .map(userDetailsMapper::fromUserDetailsToUserDetailsDto)
                    .collect(Collectors.toList());
        } else{
            List<UserDetails> users = Optional.ofNullable(userDetailsRepository.findUsersBetweenDateRangeAndBusinessUnit(localStartDate,localEndDate,departmentUnit))
                    .orElseThrow(()->new UserNotFoundException("No users are available between "+localStartDate+" and "+localEndDate+" with department: "+departmentUnit));
            log.info("Fetching users from {} to {} with department:{}",localStartDate,localEndDate,departmentUnit);
            return users.stream()
                    .map(userDetailsMapper::fromUserDetailsToUserDetailsDto)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Fetches all master data configured in application.yml.
     *
     * @return MasterDataResponseDto contains list of Usergroups and List of businessUnits.
     *
     * @see IUserDetailsService#getMasterData() 
     * */
    @Override
    public MasterDataResponseDto getMasterData() {
        log.info("Fetching all master data");
        MasterDataResponseDto responseDto=new MasterDataResponseDto();
        List<String> allUsergroups = new ArrayList<>();
        List<String> allBusinessUnits = new ArrayList<>();
        masterData.getUsergroups().values().forEach(allUsergroups::addAll);
        masterData.getBusinessunits().values().forEach(allBusinessUnits::addAll);
        responseDto.setUserGroups(allUsergroups);
        responseDto.setBusinessUnits(allBusinessUnits);
        log.info("Returning master data:{}",responseDto);
        return  responseDto;
    }

    /**
     * Encrypts password from plain text to encrypted form using Base64 Algorithm.
     * 
     * @param String System generated password in plain text form.
     * @return The password which has been encrypted using Base64 Algorithm.
     * 
     * @see UserDetailsServiceImpl#encryptPassword(String)
     * */
    public String encryptPassword(String password) {
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedPassword = encoder.encodeToString(password.getBytes());
        log.info("Encrypted Password:"+encodedPassword);
        return encodedPassword;
    }

    private EmailRequestDto getEmailRequestDtoForRegister(String recipient,String name,String password,String userGroup){
        String emailBody=registerSuceessMailBody.replace("<Name>",name)
                .replace("<password>",password);
        EmailRequestDto emailRequest=new EmailRequestDto();
        emailRequest.setMessage(emailBody);
        emailRequest.setRecipient(recipient);
        emailRequest.setSubject(registrationSubject.replace("<userGroup>",userGroup));
        return emailRequest;
    }

    /**
     *  Decryts the password from encrypted form to plain text.
     *
     * @param String password which is in encrypted form.
     * @return Password in plain text form which has been decrypted from encrypted form.
     * @throws Exception if fails to convert from encrypted password to decrypted password.
     *
     * @see UserDetailsServiceImpl#decryptPassword(String)
     * */
    public String decryptPassword(String password) {
        Base64.Decoder decoder = Base64.getDecoder();
        try{
            return new String(decoder.decode(password));
        }catch (Exception e){
            log.info("Password Decoding failed:"+e.getMessage());
        }
        return null;
    }

    /**
     * Generates the Password with minimum 8 charecters length,atleast one number,one capital letter and a special charecter.
     *
     * @return Password which bas been generated with all usecases.
     *
     * @see UserDetailsServiceImpl#generatePassword()
     * */
    public String generatePassword(){
        String generatedPassword = passwordGenerator.generatePassword();
        if (!commonUtils.validateSystemGeneratedPassword(generatedPassword)) {
            generatePassword();
        }
        log.info("GENERATED PASSWORD:"+generatedPassword);
        return encryptPassword(generatedPassword);
    }
    @Override
    public UserDetailsDto updateUserDetails(RegisterDto registerDto) {
        UserDetails userDetails=userDetailsRepository.findByEmailIdAndStatusTrue(registerDto.getEmailId())
                .orElseThrow(()->new UserNotFoundException("User Not Found With mail Id:"+registerDto.getEmailId()));
        log.info("FROM DATABASE:"+userDetails);
        userDetails.setFirstName(registerDto.getFirstName());
        userDetails.setLastName(registerDto.getLastName());
        log.info("AFTER MAPPING:"+userDetails);
        userDetailsRepository.save(userDetails);
        return userDetailsMapper.fromUserDetailsToUserDetailsDto(userDetails);
    }



    @Override
    public List<UserDetailsDto> fetchAllActiveUsers() {
        return userDetailsRepository.findAllByStatusTrue().stream()
                .map(userDetailsMapper::fromUserDetailsToUserDetailsDto)
                .collect(Collectors.toList())
                .stream()
                .sorted((user1,user2)->user2.getCreatedDateTime().compareTo(user1.getCreatedDateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public long fetchActiveUsersCount() {
        return userDetailsRepository.countByStatusTrue();
    }
}
