package com.tsp.UserService.service.impl;

import com.tsp.UserService.client.RegisterService;
import com.tsp.UserService.config.MasterData;
import com.tsp.UserService.dtos.*;
import com.tsp.UserService.entities.*;
import com.tsp.UserService.dtos.*;
import com.tsp.UserService.entities.UserProfile;
import com.tsp.UserService.entities.UserProjectExperience;
import com.tsp.UserService.exception.UserProfileNotFoundException;
import com.tsp.UserService.mapper.ProfileMapperNew;
import com.tsp.UserService.mapper.UserProfileMapper;
import com.tsp.UserService.mapper.UserProjectMapper;
import com.tsp.UserService.repository.UserProfileRepository;
import com.tsp.UserService.service.UserProfileService;
import com.tsp.UserService.utils.CommonUtils;
import com.tsp.UserService.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private UserProfileMapper profileMapper;

    @Autowired
    private UserProjectMapper projectMapper;

    @Autowired
    private ProfileMapperNew profileMapperNew;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private MasterData masterData;
    

    @Override
    public UserProfileResponseDto getUserProfileDetails(String emailId) throws IllegalStateException{
        log.info("fetching profile for mail Id:"+emailId);
        ResponseEntity<ResponseDto<UserDetailsDto>> userDetails =null;
        userDetails = registerService.fetchUserByEmailId(emailId);
        if (!Objects.requireNonNull(userDetails.getBody()).getPayLoad().isStatus()){
            throw new UserProfileNotFoundException("User is Not Available:"+emailId);
        }
        Optional<UserProfile> user = userProfileRepository.findByEmailId(emailId);
        UserProfileResponseDto userProfileResponseDto=null;
        if (user.isEmpty()) {
            user=Optional.ofNullable(new UserProfile());
            user.get().setEmailId(emailId);
            return profileMapperNew.fromEntitiesToUserProfileResponseDto(user.get(), userDetails.getBody().getPayLoad());

        }
        userProfileResponseDto= profileMapperNew.fromEntitiesToUserProfileResponseDto(user.get(),userDetails.getBody().getPayLoad());
        userProfileResponseDto.setManagerName(getManagerName(userProfileResponseDto));
        return userProfileResponseDto;
    }

    private String getManagerName( UserProfileResponseDto userProfileResponseDto) {
        log.info("Fetching manager name for the user:"+userProfileResponseDto.getEmailId());
        ResponseEntity<ResponseDto<UserDetailsDto>> managerDetails =null;
        String managerName="";
        if (userProfileResponseDto.getManagerMailId()!=null) {
              managerDetails = registerService.fetchUserByEmailId(userProfileResponseDto.getManagerMailId());
              if (Objects.requireNonNull(managerDetails.getBody()).getPayLoad()!=null){
                  managerName=managerDetails.getBody().getPayLoad().getFirstName().concat(" ").concat(managerDetails.getBody().getPayLoad().getLastName());
                  userProfileResponseDto.setManagerName(managerName);
              }
        }
        return managerName;
    }

    private List<UserProjectExperience> getProjects(List<UserProjectExperienceRequestDto> newProjects, List<UserProjectExperience> existedProjects) {
        List<UserProjectExperience> userProjects=new ArrayList<>();
        for (UserProjectExperienceRequestDto newProject : newProjects){
            boolean flag=false;
            for (UserProjectExperience existedProj:existedProjects){
                if (newProject.getProjectName().equalsIgnoreCase(existedProj.getProjectName())){
                    userProjects.add(existedProj);
                    flag=true;
                }
            }
            if (!flag)
                userProjects.add(projectMapper.fromUserProjectExperienceDtoToUserProjectExperienceEntity(newProject));
        }
        return userProjects;
    }

    @Override
    public String updateProfileStatus(UserStatusDto userStatusDto) {
        log.info("updating profile status of the user:"+userStatusDto.getEmailId());
        UserProfile userProfile = userProfileRepository.findByEmailId(userStatusDto.getEmailId())
                .orElse(null);
        if(userProfile==null){
            return Constants.PROFILE_NOT_FOUND+" with emailId: "+userStatusDto.getEmailId();
        }
        userProfile.setProfileStatus(userStatusDto.getStatus().equalsIgnoreCase(Constants.PROFILE_STATUS));
        userProfileRepository.save(userProfile);
        return Constants.PROFILE_STATUS_UPDATE_SUCCESS_MESSAGE;
    }

    @Override
    public List<UserProfileResponseDto> searchUser(String technicalSkill) {
        log.info("Fetching mentors with technical skill:"+technicalSkill);
            List<UserProfileResponseDto> mentors=userProfileRepository.findByTechnicalSkillListContainingIgnoreCaseAndManagerNotNullAndProfileStatusTrue(technicalSkill)
                    .stream()
                    .map(profile->{
                        ResponseEntity<ResponseDto<UserDetailsDto>> user=null;
                        user=registerService.fetchUserByEmailId(profile.getEmailId());
                        if (!user.getStatusCode().is2xxSuccessful())
                            return new UserProfileResponseDto();
                        log.info("USER FROM REGISTER SERVICE:"+user.getBody().getPayLoad());
                        UserProfileResponseDto responseDto= profileMapperNew.fromEntitiesToUserProfileResponseDto(profile,user.getBody().getPayLoad());
                        responseDto.setManagerName(getManagerName(responseDto));
                        return responseDto;
                    })
                    .filter(profile->profile!=null&&profile.getUserGroup().equalsIgnoreCase("mentor"))
                    .collect(Collectors.toList());
            if (mentors.isEmpty())
                throw new UserProfileNotFoundException("Mentors not found with skill:"+technicalSkill);
            return mentors;

    }

    @Override
    public List<UserProfile> projectExperience(UserProjectExperience userProjectExperience){
        return userProfileRepository.findAllByUserProjectExperienceList(userProjectExperience);
    }

    @Override
    public ByteArrayInputStream tagManager(MultipartFile file) {
        log.info("Inside Manager Tagging");
        try(Workbook workbook= WorkbookFactory.create(file.getInputStream());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.getSheetAt(0);
            List<UserProfile> users = userProfileRepository.findAll();
            for (UserProfile user : users) {
                StreamSupport.stream(sheet.spliterator(), false)
                        .filter(row -> row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().equalsIgnoreCase(user.getEmailId()))
                        .map(row -> {
                            String managerMailId = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
                            if (managerMailId == null || managerMailId.isBlank() || managerMailId.isEmpty()) {
                                row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue("No Manager Info");
                                return row;
                            }
                            return mapManager(row, managerMailId, user);
                        }).collect(Collectors.toList());
            }
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        }catch (Exception e){
            log.error("Exception occurred while reading data from file:"+e.getMessage());
        }
        return null;
    }

    @Override
    public UserProfileResponseDto getUserProfileStatusByEmailId(String emailId) {
        log.info("Fetching the profile status of the user:"+emailId);
        UserProfile userProfile=userProfileRepository.findByEmailId(emailId)
                .orElseThrow(()->new UserProfileNotFoundException("Profile is not updated for the user:"+emailId));
        UserProfileResponseDto userProfileResponseDto=new UserProfileResponseDto();
        userProfileResponseDto.setEmailId(userProfile.getEmailId());
        return userProfileResponseDto;
    }

    @Override
    public MasterData getMasterData() {
        return masterData;
    }

    private Row mapManager(Row row,String managerMailId,UserProfile user) {
        Optional<UserProfile> manager=userProfileRepository.findByEmailId(managerMailId);
        if (manager.isEmpty()) {
            row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue("Manager Not Found In DB");
            return row;
        }
        ResponseEntity<ResponseDto<UserDetailsDto>> managerInfo=registerService.fetchUserByEmailId(managerMailId);
        if (managerInfo.getBody()!=null && !managerInfo.getBody().getPayLoad().getUserGroup().equalsIgnoreCase("manager")){
            row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue("Not a manager");
            return row;
        }
        if (user.getManager()!=null && user.getManager().getEmailId().equals(managerMailId))
        {
            row.getCell(4,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue("Already mapped");
            return row;
        }
        if (user.getManager()==null) {
            user.setManager(manager.get());
            row.getCell(4,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue("Added");
        }
        if (!user.getManager().getEmailId().equals(managerMailId)){
            user.setManager(manager.get());
            row.getCell(4,Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue("Manager is updated");
        }
        userProfileRepository.save(user);
        return row;
    }


    @Override
    public UserProfileResponseDto updateUserProfile(UserProfileUpdateDto userProfileUpdateRequestDto) throws IOException {
        log.info("updating profile for the user:"+userProfileUpdateRequestDto.getEmailId());
        ResponseEntity<ResponseDto<UserDetailsDto>> userDetails=null;
        RegisterDto registerDto=profileMapperNew.fromUserProfileUpdateDtoToRegisterDto(userProfileUpdateRequestDto);
        registerDto.setCreatedBy("admin");
        userDetails=registerService.updateUserDetails(registerDto);
        if (userDetails.getBody().getPayLoad()!=null){
            Optional<UserProfile> userProfile = userProfileRepository.findByEmailId(userProfileUpdateRequestDto.getEmailId());
            boolean userStatus=userDetails.getBody().getPayLoad().isStatus();
            commonUtils.validateData(userProfileUpdateRequestDto);
            if (userStatus&&userProfile.isEmpty()){
                UserProfile profile=profileMapperNew.fromUserProfileUpdateDtoToUserProfile(userProfileUpdateRequestDto);
                userProfileRepository.save(profile);
                UserProfileResponseDto responseDto= profileMapperNew.fromEntitiesToUserProfileResponseDto(profile,userDetails.getBody().getPayLoad());
                responseDto.setManagerName(getManagerName(responseDto));
                return responseDto;
            }
            if (userStatus&&userProfile.isPresent()) {
                long profileId=userProfile.get().getId();
                List<UserProjectExperience> projects= getProjects(userProfileUpdateRequestDto.getUserProjectExperienceList(),userProfile.get().getUserProjectExperienceList());
                UserProfile mappedManager=userProfile.get().getManager();
                userProfile=Optional.ofNullable(profileMapperNew.fromUserProfileUpdateDtoToUserProfile(userProfileUpdateRequestDto));
                userProfile.get().setId(profileId);
                userProfile.get().setManager(mappedManager);
                userProfile.get().setUserProjectExperienceList(projects);
                userProfileRepository.save(userProfile.get());
                UserProfileResponseDto responseDto= profileMapperNew.fromEntitiesToUserProfileResponseDto(userProfile.get(),userDetails.getBody().getPayLoad());
                responseDto.setManagerName(getManagerName(responseDto));
                return responseDto;
            }
        }
        return null;
    }

    @Override
    public UsersCount getUsersAndUserGroupCount() {
        UsersCount counts=new UsersCount();
        ResponseEntity<ResponseDto<MasterDataResponseDto>> response=registerService.fetchMasterData();
        counts.setUserGroupsCount(response.getBody().getPayLoad().getUserGroups().stream().count());
        ResponseEntity<ResponseDto<Long>> activeUserCount=registerService.fetchAllActiveUsersCount();
        long unavailableUsers=userProfileRepository.countByProfileStatusFalse();
        long activeUsers=activeUserCount.getBody().getPayLoad();
        counts.setActiveUsers(activeUsers);
        counts.setUnavailableUsers(unavailableUsers);
        return counts;
    }


}







