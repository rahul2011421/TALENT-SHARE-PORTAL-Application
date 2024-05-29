package com.maveric.tsp.registerservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maveric.tsp.registerservice.client.NotificationClient;
import com.maveric.tsp.registerservice.client.UserserviceClient;
import com.maveric.tsp.registerservice.config.MasterData;
import com.maveric.tsp.registerservice.dto.*;
import com.maveric.tsp.registerservice.entities.UserDetails;
import com.maveric.tsp.registerservice.exception.InvalidCredentialsException;
import com.maveric.tsp.registerservice.exception.InvalidInputException;
import com.maveric.tsp.registerservice.exception.UserNotFoundException;
import com.maveric.tsp.registerservice.mapper.UserDetailsMapper;
import com.maveric.tsp.registerservice.repository.UserDetailsRepository;
import com.maveric.tsp.registerservice.util.CommonUtils;
import com.maveric.tsp.registerservice.util.Constants;
import com.maveric.tsp.registerservice.util.PasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.tomcat.util.bcel.Const;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
@Slf4j
class UserDetailsServiceImplTest {

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @Mock
    private UserserviceClient userserviceClient;

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private UserDetailsMapper userDetailsMapper;

    @Mock
    private MasterData masterData;

    @Mock
    private CommonUtils commonUtils;

    @Mock
    private PasswordGenerator passwordGenerator;

    @InjectMocks
    @Spy
    private UserDetailsServiceImpl userDetailsService;

    private static ObjectMapper objectMapper=new ObjectMapper();

    private static UserDetailsDto userDetails;
    private static LoginDto loginDto;
    private static RegisterDto registerDto;
    private static ResetDto resetDto;
    private static DeleteRequestDto deleteRequestDto;
    private static ReportRequestDto reportRequestDto;

    @BeforeAll
    public static void setUp(){
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.registerModule(new JavaTimeModule());
        try {
            userDetails=objectMapper.readValue(new File("src/test/resources/UserDetails.json"),UserDetailsDto.class);
            loginDto=objectMapper.readValue(new File("src/test/resources/login.json"), LoginDto.class);
            registerDto=objectMapper.readValue(new File("src/test/resources/register.json"),RegisterDto.class);
            resetDto=objectMapper.readValue(new File("src/test/resources/reset.json"),ResetDto.class);
            deleteRequestDto=objectMapper.readValue(new File("src/test/resources/deleteUsers.json"),DeleteRequestDto.class);
            reportRequestDto=objectMapper.readValue(new File("src/test/resources/talent-share-report-request.json"),ReportRequestDto.class);
        } catch (IOException e) {
            log.info("Json Parsing Exception:"+e.getMessage());
        }
    }

    /**
     * Scenario : when both user mailId and passwords are valid.
     * Inputs : LoginDto contains mailId and password fields.
     * Expectation : UserDetailsDto which contains all information of the user.
     * */
    @Test
    void test_loginUser_when_mailId_And_Password_are_valid(){
        UserDetails userDetailsEntity=new UserDetails();
        userDetailsEntity.setPassword(loginDto.getPassword());
        // Scenario: Mocking the userDetailsRepository to return a user with mailId
        when(userDetailsRepository.findByEmailIdAndStatusTrue(loginDto.getEmailId()))
                .thenReturn(Optional.ofNullable(userDetailsEntity));
        // Scenario: Mocking the userDetailsMapper to convert entity and return a dto
        when(userDetailsMapper.fromUserDetailsToUserDetailsDto(userDetailsEntity))
                .thenReturn(userDetails);
        //Calling the service method
        UserDetailsDto userDetailsDto=userDetailsService.loginUser(loginDto);
        assertNotNull(userDetailsDto);
        assertSame(userDetailsDto,userDetails);
        assertEquals(userDetailsDto.getEmailId(),userDetails.getEmailId());
        // Verification: Ensure that the service method returns the expected user
        verify(userDetailsRepository).findByEmailIdAndStatusTrue(loginDto.getEmailId());
        // Verification: Ensure that the mapper service method returns the expected dto
        verify(userDetailsMapper).fromUserDetailsToUserDetailsDto(userDetailsEntity);
    }

    @Test
    void test_loginUser_when_return_empty_with_given_mailId_then_throw_UserNotFoundException(){
        UserDetails userDetailsEntity=new UserDetails();
        userDetailsEntity.setPassword(loginDto.getPassword());
        // Scenario: Mocking the userDetailsRepository to return a user with mailId
        when(userDetailsRepository.findByEmailIdAndStatusTrue(loginDto.getEmailId()))
                .thenReturn(Optional.empty());

        //Calling the Service method
        Executable executable=()->{
            userDetailsService.loginUser(loginDto);
        };
        assertThrows(UserNotFoundException.class,executable);
        // Verification: Ensure that the userDetailsRepository method returns the empty userDetails Object
        verify(userDetailsRepository).findByEmailIdAndStatusTrue(loginDto.getEmailId());
    }


    @Test
    void test_loginUser_when_entered_valid_mailId_And_Incorrect_Password_then_throw_InvalidCredentialsException(){
        UserDetails userDetailsEntity=new UserDetails();
        userDetailsEntity.setPassword("S2FydGhpa0AxMjM=");
        // Scenario: Mocking the userDetailsRepository to return a user with mailId
        when(userDetailsRepository.findByEmailIdAndStatusTrue(loginDto.getEmailId()))
                .thenReturn(Optional.ofNullable(userDetailsEntity));
        //Calling the service method
        Executable executable=()->{
            userDetailsService.loginUser(loginDto);
        };
        assertThrows(InvalidCredentialsException.class,executable);
        // Verification: Ensure that the service method returns the expected user
        verify(userDetailsRepository).findByEmailIdAndStatusTrue(loginDto.getEmailId());
    }

    @Test
    void test_registerUser_when_input_valid_then_register_new_user(){
        ReflectionTestUtils.setField(userDetailsService,"registerSuceessMailBody",getRegisterMailBody());
//        ReflectionTestUtils.setField(userDetailsService,"tspSiteUrl","http://localhost:3002/login");
        ReflectionTestUtils.setField(userDetailsService,"registrationSubject","Notification-Registered");
        when(userDetailsMapper.fromRegisterDtoToUserDetailsEntity(any(RegisterDto.class)))
                .thenReturn(getUserDetails(userDetails));
        doNothing().when(commonUtils).validateMasterData(anyString(),anyString());
        doReturn("VmluZWV0QDEyMw==").when(userDetailsService).generatePassword();
        UserDetails userDetails1=getUserDetails(userDetails);
        userDetails1.setId(1l);
        when(userDetailsRepository.save(any(UserDetails.class)))
                .thenReturn(userDetails1);
        when(notificationClient.sendEmail(any(EmailRequestDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.OK)
                .body("Success"));
        when(userDetailsMapper.fromUserDetailsToUserDetailsDto(any(UserDetails.class)))
                .thenReturn(userDetails);
        UserDetailsDto userDetailsDto=userDetailsService.registerUser(registerDto);
        assertNotNull(userDetailsDto);
        verify(userDetailsMapper).fromRegisterDtoToUserDetailsEntity(any(RegisterDto.class));
        verify(commonUtils).validateMasterData(anyString(),anyString());
        verify(userDetailsRepository).save(any(UserDetails.class));
        verify(notificationClient).sendEmail(any(EmailRequestDto.class));
        verify(userDetailsMapper).fromUserDetailsToUserDetailsDto(any(UserDetails.class));
    }


    @Test
    void test_forgotPassword(){
        ReflectionTestUtils.setField(userDetailsService,"forgotPasswordMailBody",getForgotPasswordMailBody());
//        ReflectionTestUtils.setField(userDetailsService,"tspSiteUrl","http://localhost:3002/login");
        UserDetails userDetails1=getUserDetails(userDetails);
        userDetails1.setId(1l);
        when(userDetailsRepository.findByEmailIdAndStatusTrue(anyString()))
                .thenReturn(Optional.ofNullable(userDetails1));
        doReturn("VmluZWV0QDEyMw==").when(userDetailsService).generatePassword();
        when(userDetailsRepository.save(any(UserDetails.class)))
                .thenReturn(userDetails1);
        when(notificationClient.sendEmail(any(EmailRequestDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.OK)
                .body("Success"));
        String result=userDetailsService.forgotPassword(userDetails.getEmailId());
        assertEquals(result, Constants.FORGOT_PASSWORD_SUCCESS_MSG);
        verify(userDetailsRepository).findByEmailIdAndStatusTrue(anyString());
        verify(userDetailsRepository).save(any(UserDetails.class));
        verify(notificationClient).sendEmail(any(EmailRequestDto.class));
    }

    @Test
    void test_forgotPassword_when_No_User_Exist_with_mailId(){
        UserDetails userDetails1=getUserDetails(userDetails);
        userDetails1.setId(1l);
        when(userDetailsRepository.findByEmailIdAndStatusTrue(anyString()))
                .thenReturn(Optional.empty());
        Executable executable=()->{
            userDetailsService.forgotPassword(userDetails.getEmailId());
        };
        assertThrows(UserNotFoundException.class,executable);
        verify(userDetailsRepository).findByEmailIdAndStatusTrue(anyString());
    }

    @Test
    void test_deleteUser_when_input_empty_then_throw_InvalidInputException(){
        List<DeleteRequestDto> dtos1=new ArrayList<>();
        Executable executable=()->{
          userDetailsService.deleteUser(dtos1);
        };
        assertThrows(InvalidInputException.class,executable);
    }

    @Test
    void test_deleteUser_when_input_is_not_empty_then_set_User_Status_and_exitDate_return_message(){
        List<DeleteRequestDto> dtos1=new ArrayList<>();
        dtos1.add(deleteRequestDto);
        when(userDetailsRepository.findByEmailIdAndStatusTrue(anyString()))
                .thenReturn(Optional.ofNullable(getUserDetails(userDetails)));
        when(commonUtils.convertToLocaDateFormat(anyString()))
                .thenReturn(LocalDate.now());
        when(userDetailsRepository.save(any(UserDetails.class)))
                .thenReturn(getUserDetails(userDetails));
        ResponseDto<String> responseDto=new ResponseDto<>();
        when(userserviceClient.updateProfileStatus(any(UserStatusDto.class)))
                .thenReturn(ResponseEntity.ok(responseDto));
        String result=userDetailsService.deleteUser(dtos1);
        assertEquals("User’s Successfully Deleted",result);
        verify(userDetailsRepository).findByEmailIdAndStatusTrue(anyString());
        verify(commonUtils).convertToLocaDateFormat(anyString());
        verify(userDetailsRepository).save(any(UserDetails.class));
        verify(userserviceClient).updateProfileStatus(any(UserStatusDto.class));
    }

    @Test
    void test_deleteUser_when_input_is_not_empty_but_no_userFound_then_log_userNot_for_mail(){
        List<DeleteRequestDto> dtos1=new ArrayList<>();
        dtos1.add(deleteRequestDto);
        when(userDetailsRepository.findByEmailIdAndStatusTrue(anyString()))
                .thenReturn(Optional.ofNullable(getUserDetails(userDetails)));
        when(commonUtils.convertToLocaDateFormat(anyString()))
                .thenReturn(LocalDate.now());
        when(userDetailsRepository.save(any(UserDetails.class)))
                .thenReturn(new UserDetails());
        String result=userDetailsService.deleteUser(dtos1);
        assertEquals("User’s Successfully Deleted",result);
        verify(userDetailsRepository).findByEmailIdAndStatusTrue(anyString());
        verify(commonUtils).convertToLocaDateFormat(anyString());
        verify(userDetailsRepository).save(any(UserDetails.class));
    }

    @Test
    void test_resetPassword(){
        when(userDetailsRepository.findByEmailIdAndStatusTrue(anyString()))
                .thenReturn(Optional.ofNullable(getUserDetails(userDetails)));
        when(userDetailsRepository.save(any(UserDetails.class)))
                .thenReturn(getUserDetails(userDetails));
        String result=userDetailsService.resetPassword(resetDto);
        assertEquals(Constants.RESET_PASSWORD_SUCCESS_MSG,result);
        verify(userDetailsRepository).findByEmailIdAndStatusTrue(anyString());
        verify(userDetailsRepository).save(any(UserDetails.class));
    }

    @Test
    void test_resetPassword_when_No_User_exist_with_given_mail_id_then_throw_UserNotFoundException(){
        when(userDetailsRepository.findByEmailIdAndStatusTrue(anyString()))
                .thenReturn(Optional.empty());
        Executable executable=()->{
          userDetailsService.resetPassword(resetDto);
        };
        assertThrows(UserNotFoundException.class,executable);
        verify(userDetailsRepository).findByEmailIdAndStatusTrue(anyString());
    }

    @Test
    void test_resetPassword_when_User_exist_with_given_mail_id_but_Passwords_mismatch_then_throw_InvalidCredentialsException(){
        when(userDetailsRepository.findByEmailIdAndStatusTrue(anyString()))
                .thenReturn(Optional.ofNullable(getUserDetails(userDetails)));
        Executable executable=()->{
            ResetDto resetDto1=resetDto;
            resetDto1.setNewPassword(loginDto.getPassword());
            resetDto1.setConfirmPassword("TWFoZXNoQDEyMw==");
            userDetailsService.resetPassword(resetDto);
        };
        assertThrows(InvalidCredentialsException.class,executable);
        verify(userDetailsRepository).findByEmailIdAndStatusTrue(anyString());
    }

    @Test
    void test_fetchUserByEmailId_when_User_exists_with_mailId_then_Return_UserDetails(){
        when(userDetailsRepository.findByEmailIdAndStatusTrue(anyString()))
                .thenReturn(Optional.ofNullable(getUserDetails(userDetails)));
        when(userDetailsMapper.fromUserDetailsToUserDetailsDto(any(UserDetails.class)))
                .thenReturn(userDetails);
        UserDetailsDto userDetailsDto=userDetailsService.fetchUserByEmailId(userDetails.getEmailId());
        assertSame(userDetailsDto,userDetails);
        verify(userDetailsRepository).findByEmailIdAndStatusTrue(anyString());
        verify(userDetailsMapper).fromUserDetailsToUserDetailsDto(any(UserDetails.class));
    }

    @Test
    void test_fetchUserByEmailId_when_User_Not_exists_with_mailId_then_Throw_UserNotFoundException(){
        when(userDetailsRepository.findByEmailIdAndStatusTrue(anyString()))
                .thenReturn(Optional.empty());
        Executable executable=()->{
            userDetailsService.fetchUserByEmailId(userDetails.getEmailId());
        };
        assertThrows(UserNotFoundException.class,executable);
        verify(userDetailsRepository).findByEmailIdAndStatusTrue(anyString());
    }

    @Test
    void test_getUserStatus_when_user_exist_with_given_mailId_then_Return_UserStatus(){
        when(userDetailsRepository.findByEmailId(anyString()))
                .thenReturn(Optional.ofNullable(getUserDetails(userDetails)));
        boolean result=userDetailsService.getUserStatus(userDetails.getEmailId());
        assertTrue(result);
        verify(userDetailsRepository).findByEmailId(userDetails.getEmailId());
    }

    @Test
    void test_getUserStatus_when_user_Not_exist_with_given_mailId_then_Throw_UserNotFoundException(){
        when(userDetailsRepository.findByEmailId(anyString()))
                .thenReturn(Optional.empty());
        Executable executable=()->{
            userDetailsService.getUserStatus(userDetails.getEmailId());
        };
        assertThrows(UserNotFoundException.class,executable);
        verify(userDetailsRepository).findByEmailId(userDetails.getEmailId());
    }

    @Test
    void test_getAllUsers_when_users_exists_then_return_list_of_users(){
        when(userDetailsRepository.findAll())
                .thenReturn(Arrays.asList(getUserDetails(userDetails)));
        when(userDetailsMapper.fromUserDetailsToUserDetailsDto(any(UserDetails.class)))
                .thenReturn(userDetails);
        List<UserDetailsDto> userDetailsDtos=userDetailsService.getAllUsers();
        assertNotNull(userDetailsDtos);
        verify(userDetailsRepository).findAll();
        verify(userDetailsMapper).fromUserDetailsToUserDetailsDto(any(UserDetails.class));
    }

    @Test
    void test_getAllUsers_when_users_Not_exists_then_Throw_UsersNotFoundException(){
        when(userDetailsRepository.findAll())
                .thenReturn(new ArrayList<>());
        Executable executable=()->{
            userDetailsService.getAllUsers();
        };
        assertThrows(UserNotFoundException.class,executable);
        verify(userDetailsRepository).findAll();
    }

    @Test
    void test_fetchUsersBetweenDataRange_with_start_End_Date_and_without_departmentUnit(){
        when(commonUtils.convertToLocaDateFormat(anyString()))
                .thenReturn(LocalDate.now().minusDays(1));
        when(commonUtils.convertToLocaDateFormat(anyString()))
                .thenReturn(LocalDate.now().plusDays(1));
        when(userDetailsRepository.findByCreatedDateTimeBetween(any(LocalDateTime.class),any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(getUserDetails(userDetails)));
        when(userDetailsMapper.fromUserDetailsToUserDetailsDto(any(UserDetails.class)))
                .thenReturn(userDetails);
        ReportRequestDto requestDto=reportRequestDto;
        requestDto.setDepartmentUnit("");
        List<UserDetailsDto> users=userDetailsService.fetchUsersBetweenDataRange(requestDto);
        assertNotNull(users);
        verify(commonUtils,times(2)).convertToLocaDateFormat(anyString());
        verify(userDetailsRepository).findByCreatedDateTimeBetween(any(LocalDateTime.class),any(LocalDateTime.class));
    }

    @Test
    void test_fetchUsersBetweenDataRange_with_start_End_Date_and_with_departmentUnit(){
        when(commonUtils.convertToLocaDateFormat(anyString()))
                .thenReturn(LocalDate.now().minusDays(1));
        when(commonUtils.convertToLocaDateFormat(anyString()))
                .thenReturn(LocalDate.now().plusDays(1));
        when(userDetailsRepository.findUsersBetweenDateRangeAndBusinessUnit(any(LocalDateTime.class),any(LocalDateTime.class),anyString()))
                .thenReturn(Arrays.asList(getUserDetails(userDetails)));
        when(userDetailsMapper.fromUserDetailsToUserDetailsDto(any(UserDetails.class)))
                .thenReturn(userDetails);
        ReportRequestDto requestDto=reportRequestDto;
        requestDto.setDepartmentUnit("data");
        List<UserDetailsDto> users=userDetailsService.fetchUsersBetweenDataRange(requestDto);
        assertNotNull(users);
        verify(commonUtils,times(2)).convertToLocaDateFormat(anyString());
        verify(userDetailsMapper).fromUserDetailsToUserDetailsDto(any(UserDetails.class));
        verify(userDetailsRepository).findUsersBetweenDateRangeAndBusinessUnit(any(LocalDateTime.class),any(LocalDateTime.class),anyString());
    }

    @Test
    void test_getMasterData(){
//        ReflectionTestUtils.setField(userDetailsService,"masterData",);
        when(masterData.getBusinessunits()).thenReturn(new HashMap<>());
        when(masterData.getUsergroups()).thenReturn(new HashMap<>());
        userDetailsService.getMasterData();
        verify(masterData).getBusinessunits();
        verify(masterData).getUsergroups();
    }

    @Test
    void test_encryptPassword(){
        assertNotNull(userDetailsService.encryptPassword(loginDto.getPassword()));
    }

    @Test
    void test_decryptPassword(){
        assertNotNull(userDetailsService.decryptPassword(loginDto.getPassword()));
    }

    @Test
    void test_generatePassword(){
        when(commonUtils.validateSystemGeneratedPassword(anyString()))
                .thenReturn(true);
        when(passwordGenerator.generatePassword())
                .thenReturn(loginDto.getPassword());
        assertNotNull(userDetailsService.generatePassword());
    }

    @Test
    void test_updateUserDetails(){
        when(userDetailsRepository.findByEmailIdAndStatusTrue(anyString()))
                .thenReturn(Optional.ofNullable(getUserDetails(userDetails)));
        when(userDetailsRepository.save(any(UserDetails.class)))
                .thenReturn(getUserDetails(userDetails));
        when(userDetailsMapper.fromUserDetailsToUserDetailsDto(any(UserDetails.class)))
                .thenReturn(userDetails);
        UserDetailsDto userDetailsDto=userDetailsService.updateUserDetails(registerDto);
        assertNotNull(userDetailsDto);
        verify(userDetailsRepository).findByEmailIdAndStatusTrue(anyString());
        verify(userDetailsRepository).save(any(UserDetails.class));
        verify(userDetailsMapper).fromUserDetailsToUserDetailsDto(any(UserDetails.class));
        verify(userDetailsService).updateUserDetails(registerDto);
    }

    private UserDetails getUserDetails(UserDetailsDto userDetailsDto){
        UserDetails userDetails1=new UserDetails();
        BeanUtils.copyProperties(userDetailsDto,userDetails1);
        return userDetails1;
    }

    private String getRegisterMailBody(){
        return " <html><body>\n" +
                "      Dear <b><Name></b>,<br/><br/>\n" +
                "      Congratulation for being a registered user of Talent Share Portal.<br/><br/>\n" +
                "      Please click below link to update your profile & Password<br/><br/>\n" +
                "      <a href=\"<loginlink>\">Talent-share-portal</a><br/><br/>\n" +
                "      For First login, please use system Generated password. Later,<br/>\n" +
                "      please change your password as per the acceptance criteria given in<br/>\n" +
                "      the portal.<br/>\n" +
                "      System Generated Password: <b><password></b>\n" +
                "      </body></html>";
    }

    private String getForgotPasswordMailBody(){
        return " <html><body>\n" +
                "      Dear <b><Name></b>,<br/><br/>\n" +
                "      Your password has been successfully reset!.<br/><br/>\n" +
                "      Please click below link to update password<br/><br/>\n" +
                "      <a href=\"<loginlink>\">Talent-share-portal</a><br/><br/>\n" +
                "      For First login, please use system Generated password. Later,<br/>\n" +
                "      please change your password as per the acceptance criteria given in<br/>\n" +
                "      the portal.<br/>\n" +
                "      System Generated Password: <b><password></b>\n" +
                "      </body></html>";
    }

}