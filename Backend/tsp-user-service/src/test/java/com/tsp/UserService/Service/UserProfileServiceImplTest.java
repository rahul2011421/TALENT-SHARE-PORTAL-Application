package com.tsp.UserService.Service;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tsp.UserService.client.RegisterService;
import com.tsp.UserService.config.MasterData;
import com.tsp.UserService.dtos.*;
import com.tsp.UserService.entities.UserProfile;
import com.tsp.UserService.entities.UserProjectExperience;
import com.tsp.UserService.exception.UserProfileNotFoundException;
import com.tsp.UserService.mapper.ProfileMapperNew;
import com.tsp.UserService.mapper.UserProfileMapper;
import com.tsp.UserService.mapper.UserProjectMapper;
import com.tsp.UserService.repository.UserProfileRepository;
import com.tsp.UserService.service.impl.UserProfileServiceImpl;
import com.tsp.UserService.utils.CommonUtils;
import com.tsp.UserService.dtos.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@Slf4j
class UserProfileServiceImplTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private RegisterService registerService;

    @Mock
    private UserProfileMapper profileMapper;

    @Mock
    private UserProjectMapper projectMapper;

    @Mock
    private ProfileMapperNew profileMapperNew;

    @Mock
    private CommonUtils commonUtils;

    @Mock
    private MasterData masterData;

    @InjectMocks
    @Spy
    private UserProfileServiceImpl userProfileService;

    private static UserProfileResponseDto userProfileResponseDto;

    private static UserProfileUpdateDto userProfileUpdateDto;
    private static UserStatusDto userStatusDto;
    private static UserProfile userProfile;
    private static UserDetailsDto userDetails;
    private static RegisterDto registerDto;

    private static final ObjectMapper objectMapper=new ObjectMapper();


    @BeforeAll
    public static void setUp(){
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.registerModule(new JavaTimeModule());
        try {
            userProfileResponseDto=objectMapper.readValue(new File("src/test/resources/profile-response.json"),UserProfileResponseDto.class);
            userProfileUpdateDto=objectMapper.readValue(new File("src/test/resources/user-profile-update.json"),UserProfileUpdateDto.class);
            userStatusDto=objectMapper.readValue(new File("src/test/resources/profile-status-update.json"),UserStatusDto.class);
            userProfile=objectMapper.readValue(new File("src/test/resources/user-profile-update.json"),UserProfile.class);
            userDetails=objectMapper.readValue(new File("src/test/resources/UserDetails.json"),UserDetailsDto.class);
            registerDto=objectMapper.readValue(new File("src/test/resources/register.json"),RegisterDto.class);
        } catch (IOException e) {
            log.info("Json Parsing Exception:"+e.getMessage());
        }
    }


    @Test
    void test_getUserProfileDetails_when_user_is_exist_with_given_emailId(){
        when(userProfileRepository.findByEmailId(anyString()))
                .thenReturn(Optional.ofNullable(userProfile));
        ResponseDto<UserDetailsDto> responseDto=new ResponseDto();
        userDetails.setStatus(true);
        responseDto.setPayLoad(userDetails);
        when(registerService.fetchUserByEmailId(anyString()))
                .thenReturn(ResponseEntity.ok(responseDto));
        when(profileMapperNew.fromEntitiesToUserProfileResponseDto(any(UserProfile.class),any(UserDetailsDto.class)))
                .thenReturn(userProfileResponseDto);
        when( registerService.fetchUserByEmailId(anyString()))
                .thenReturn(ResponseEntity.ok(responseDto));
        UserProfileResponseDto profileResponseDto=userProfileService.getUserProfileDetails(userStatusDto.getEmailId());
        Assertions.assertNotNull(profileResponseDto);
        verify(userProfileRepository).findByEmailId(anyString());
        verify(registerService,times(2)).fetchUserByEmailId(anyString());
        verify(profileMapperNew).fromEntitiesToUserProfileResponseDto(any(UserProfile.class),any(UserDetailsDto.class));
    }





    @Test
    void test_updateProfileStatus_when_input_as_available_set_profileStatus_true(){
        when(userProfileRepository.findByEmailId(anyString()))
                .thenReturn(Optional.ofNullable(userProfile));
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(userProfile);
        userProfileService.updateProfileStatus(userStatusDto);
        verify(userProfileRepository).findByEmailId(anyString());
        verify(userProfileRepository).save(any(UserProfile.class));
    }

    @Test
    void test_searchUser_with_given_skill_if_match_return_list_of_mentors(){
        ArrayList<UserProfile> userProfiles=new ArrayList<>();
        userProfiles.add(userProfile);
        when(userProfileRepository.findByTechnicalSkillListContainingIgnoreCaseAndManagerNotNullAndProfileStatusTrue(anyString()))
                .thenReturn(userProfiles);
        ResponseDto<UserDetailsDto> responseDto=new ResponseDto();
        responseDto.setPayLoad(userDetails);
        when(registerService.fetchUserByEmailId(anyString()))
                .thenReturn(ResponseEntity.ok(responseDto));
        when(profileMapperNew.fromEntitiesToUserProfileResponseDto(any(UserProfile.class),any(UserDetailsDto.class)))
                .thenReturn(userProfileResponseDto);
        userProfileService.searchUser(userProfileUpdateDto.getTechnicalSkillList().get(0));
        verify(userProfileRepository).findByTechnicalSkillListContainingIgnoreCaseAndManagerNotNullAndProfileStatusTrue(anyString());
        verify(registerService,times(2)).fetchUserByEmailId(anyString());
        verify(profileMapperNew).fromEntitiesToUserProfileResponseDto(any(UserProfile.class),any(UserDetailsDto.class));
    }

    @Test
    void test_searchUser_with_given_skill_if_no_mentor_with_given_skill_throw_exception(){
        when(userProfileRepository.findByTechnicalSkillListContainingIgnoreCaseAndManagerNotNullAndProfileStatusTrue(anyString()))
                .thenReturn(new ArrayList<>());
       Executable executable=()->{
         userProfileService.searchUser(userProfileUpdateDto.getTechnicalSkillList().get(0));
       };
       Assertions.assertThrows(UserProfileNotFoundException.class,executable);
        verify(userProfileRepository).findByTechnicalSkillListContainingIgnoreCaseAndManagerNotNullAndProfileStatusTrue(anyString());
    }

    @Test
    void test_projectExperience(){
        ArrayList<UserProfile> userProfiles=new ArrayList<>();
        userProfiles.add(userProfile);
        UserProjectExperience projectExperience=new UserProjectExperience();
        when(userProfileRepository.findAllByUserProjectExperienceList(any(UserProjectExperience.class)))
                .thenReturn(userProfiles);
        userProfileService.projectExperience(projectExperience);
        verify(userProfileRepository).findAllByUserProjectExperienceList(any(UserProjectExperience.class));
    }

    @Test
    void test_getUserProfileStatusByEmailId_if_user_exist_return_status(){
        when(userProfileRepository.findByEmailId(anyString()))
                .thenReturn(Optional.ofNullable(userProfile));
        UserProfileResponseDto responseDto=userProfileService.getUserProfileStatusByEmailId(userProfile.getEmailId());
        Assertions.assertNotNull(responseDto);
        verify(userProfileRepository).findByEmailId(anyString());
    }

    @Test
    void test_getUserProfileStatusByEmailId_if_user_Not_exist_throw_Exception(){
        when(userProfileRepository.findByEmailId(anyString()))
                .thenReturn(Optional.empty());
        Executable executable=()->{
            userProfileService.getUserProfileStatusByEmailId(userProfile.getEmailId());
        };
        Assertions.assertThrows(UserProfileNotFoundException.class,executable);
        verify(userProfileRepository).findByEmailId(anyString());
    }

    @Test
    void test_tagManager_when_No_Manager_Info_is_available_for_the_user() throws IOException {
        String inputFilePath = "src/test/resources/managers-tagging.xlsx";
        Workbook existingWorkbook = readWorkbook(inputFilePath);
        Sheet sheet = existingWorkbook.getSheetAt(0);
        ArrayList<UserProfile> userProfiles=new ArrayList<>();
        UserProfile userProfile1=userProfile;
        userProfile.setEmailId("rajesh@maveric-systems.com");
        userProfiles.add(userProfile1);
        String outputFilePath = "src/test/resources/managers-tagging.xlsx";
        writeWorkbook(existingWorkbook, outputFilePath);
        MultipartFile multipartFile = createMockMultipartFile(outputFilePath, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        when(userProfileRepository.findAll())
                .thenReturn(userProfiles);
        userProfileService.tagManager(multipartFile);
        verify(userProfileRepository).findAll();
    }

    @Test
    void test_tagManager_when_User_is_already_tagged_with_same_Manager() throws IOException {
        String inputFilePath = "src/test/resources/managers-tagging.xlsx";
        Workbook existingWorkbook = readWorkbook(inputFilePath);
        Sheet sheet = existingWorkbook.getSheetAt(0);
        ArrayList<UserProfile> userProfiles=new ArrayList<>();
        UserProfile userProfile1=userProfile;
        UserProfile managerDetails=new UserProfile();
        managerDetails.setEmailId("praveen@maveric-systems.com");
        userProfile1.setManager(managerDetails);
        userProfiles.add(userProfile1);
        System.out.println("SENDING MAIL ID:"+userProfile1.getEmailId());
        String outputFilePath = "src/test/resources/managers-tagging.xlsx";
        writeWorkbook(existingWorkbook, outputFilePath);
        MultipartFile multipartFile = createMockMultipartFile(outputFilePath, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        when(userProfileRepository.findAll())
                .thenReturn(userProfiles);
        when(userProfileRepository.findByEmailId(anyString()))
                .thenReturn(Optional.ofNullable(userProfile1.getManager()));
        userProfileService.tagManager(multipartFile);
        verify(userProfileRepository).findAll();
    }

    @Test
    void test_tagManager_when_User_is_not_tagged_and_new_manager_has_to_be_mapped() throws IOException {
        String inputFilePath = "src/test/resources/managers-tagging.xlsx";
        Workbook existingWorkbook = readWorkbook(inputFilePath);
        Sheet sheet = existingWorkbook.getSheetAt(0);
        ArrayList<UserProfile> userProfiles=new ArrayList<>();
//        UserProfile userProfile1=userProfile;
//        userProfile1.setManager(new UserProfile());
        UserProfile managerDetails=new UserProfile();
        managerDetails.setEmailId("praveen@gmail.com");
        userProfiles.add(userProfile);
        String outputFilePath = "src/test/resources/managers-tagging.xlsx";
        writeWorkbook(existingWorkbook, outputFilePath);
        MultipartFile multipartFile = createMockMultipartFile(outputFilePath, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        when(userProfileRepository.findAll())
                .thenReturn(userProfiles);
        when(userProfileRepository.findByEmailId(anyString()))
                .thenReturn(Optional.ofNullable(managerDetails));
        userProfileService.tagManager(multipartFile);
        verify(userProfileRepository).findAll();
    }

    @Test
    void test_updateUserProfile_when_profile_is_already_updated() throws IOException {
        when(profileMapperNew.fromUserProfileUpdateDtoToRegisterDto(any(UserProfileUpdateDto.class)))
                .thenReturn(registerDto);
        ResponseDto<UserDetailsDto> responseDto=new ResponseDto();
        responseDto.setPayLoad(userDetails);
        when(registerService.updateUserDetails(any(RegisterDto.class)))
                .thenReturn(ResponseEntity.ok(responseDto));
        when(userProfileRepository.findByEmailId(anyString()))
                .thenReturn(Optional.ofNullable(userProfile));
        doNothing().when(commonUtils).validateData(any(UserProfileUpdateDto.class));
        when(profileMapperNew.fromUserProfileUpdateDtoToUserProfile(any(UserProfileUpdateDto.class)))
                .thenReturn(userProfile);
        when(userProfileRepository.save(any(UserProfile.class)))
                .thenReturn(userProfile);
        when(profileMapperNew.fromEntitiesToUserProfileResponseDto(any(UserProfile.class),any(UserDetailsDto.class)))
                .thenReturn(userProfileResponseDto);
        when(registerService.fetchUserByEmailId(anyString()))
                .thenReturn(ResponseEntity.ok(responseDto));
        userProfileService.updateUserProfile(userProfileUpdateDto);
        verify(profileMapperNew).fromUserProfileUpdateDtoToRegisterDto(any(UserProfileUpdateDto.class));
        verify(registerService).updateUserDetails(any(RegisterDto.class));
        verify(userProfileRepository).findByEmailId(anyString());
        verify(commonUtils).validateData(any(UserProfileUpdateDto.class));
        verify(profileMapperNew).fromUserProfileUpdateDtoToUserProfile(any(UserProfileUpdateDto.class));
        verify(userProfileRepository).save(any(UserProfile.class));
        verify(profileMapperNew).fromEntitiesToUserProfileResponseDto(any(UserProfile.class),any(UserDetailsDto.class));
    }

    @Test
    void test_updateUserProfile_when_profile_is_updateding_for_first_time() throws IOException {
        when(profileMapperNew.fromUserProfileUpdateDtoToRegisterDto(any(UserProfileUpdateDto.class)))
                .thenReturn(registerDto);
        ResponseDto<UserDetailsDto> responseDto=new ResponseDto();
        responseDto.setPayLoad(userDetails);
        when(registerService.updateUserDetails(any(RegisterDto.class)))
                .thenReturn(ResponseEntity.ok(responseDto));
        when(userProfileRepository.findByEmailId(anyString()))
                .thenReturn(Optional.empty());
        doNothing().when(commonUtils).validateData(any(UserProfileUpdateDto.class));
        when(profileMapperNew.fromUserProfileUpdateDtoToUserProfile(any(UserProfileUpdateDto.class)))
                .thenReturn(userProfile);
        when(userProfileRepository.save(any(UserProfile.class)))
                .thenReturn(userProfile);
        when(profileMapperNew.fromEntitiesToUserProfileResponseDto(any(UserProfile.class),any(UserDetailsDto.class)))
                .thenReturn(userProfileResponseDto);
        when(registerService.fetchUserByEmailId(anyString()))
                .thenReturn(ResponseEntity.ok(responseDto));
//        when(registerService.fetchUserByEmailId(anyString()))
//                .thenReturn(ResponseEntity.ok(responseDto));
        userProfileService.updateUserProfile(userProfileUpdateDto);
        verify(profileMapperNew).fromUserProfileUpdateDtoToRegisterDto(any(UserProfileUpdateDto.class));
        verify(registerService).updateUserDetails(any(RegisterDto.class));
        verify(userProfileRepository).findByEmailId(anyString());
        verify(commonUtils).validateData(any(UserProfileUpdateDto.class));
        verify(profileMapperNew).fromUserProfileUpdateDtoToUserProfile(any(UserProfileUpdateDto.class));
        verify(userProfileRepository).save(any(UserProfile.class));
        verify(profileMapperNew).fromEntitiesToUserProfileResponseDto(any(UserProfile.class),any(UserDetailsDto.class));
    }

    @Test
    void test_getMasterData(){
        Assertions.assertNotNull(userProfileService.getMasterData());
    }


    private Workbook readWorkbook(String filePath) throws IOException {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            return WorkbookFactory.create(inputStream);
        }
    }

    private void writeWorkbook(Workbook workbook, String filePath) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            workbook.write(fileOutputStream);
        }
    }

    private MultipartFile createMockMultipartFile(String filePath, String contentType) throws IOException {
        File file = new File(filePath);
        Path path = file.toPath();
        String originalFilename = file.getName();

        byte[] content = Files.readAllBytes(path);

        return new MockMultipartFile("file", originalFilename, contentType, content);
    }

    private ArrayList<UserProfile> getUserProfileList(){
        ArrayList<UserProfile> userProfiles=new ArrayList<>();
        UserProfile userProfile1=userProfile;
        userProfile1.setEmailId("rajesh@gmail.com");
        UserProfile userProfile2=userProfile;
        userProfile2.setEmailId("srini@gmail.com");
        userProfile2.setManager(userProfile1);
        UserProfile userProfile3=userProfile;
        userProfile3.setEmailId("praveen@gmail.com");
        userProfile3.setManager(userProfile2);
        UserProfile userProfile4=userProfile;
        userProfile4.setEmailId("vineet@gmial.com");
        UserProfile userProfile6=userProfile;
        userProfile6.setEmailId("daisy@gmail.com");
        UserProfile userProfile5=userProfile;
        userProfile5.setEmailId("prakash@gmail.com");
        userProfiles.add(userProfile);
        userProfiles.add(userProfile1);
        userProfiles.add(userProfile2);
        userProfile4.setManager(userProfile3);
        userProfiles.add(userProfile4);
        userProfile5.setManager(userProfile6);
        return userProfiles;
    }

}
