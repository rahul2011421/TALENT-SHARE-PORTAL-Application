package com.tsp.UserService.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tsp.UserService.config.MasterData;
import com.tsp.UserService.controller.UserProfileController;
import com.tsp.UserService.dtos.ResponseDto;
import com.tsp.UserService.dtos.UserProfileResponseDto;
import com.tsp.UserService.dtos.UserProfileUpdateDto;
import com.tsp.UserService.dtos.UserStatusDto;
import com.tsp.UserService.service.UserProfileService;
import com.tsp.UserService.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserProfileController.class)
@Slf4j
class UserProfileControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private UserProfileService userProfileService;

    private static UserProfileResponseDto userProfileResponseDto;

    private static UserProfileUpdateDto userProfileUpdateDto;
    private static UserStatusDto userStatusDto;

    private static final ObjectMapper objectMapper=new ObjectMapper();

    private final String BASE_URI="/profile";

    @BeforeAll
    public static void setUp(){
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.registerModule(new JavaTimeModule());
        try {
            userProfileResponseDto=objectMapper.readValue(new File("src/test/resources/profile-response.json"),UserProfileResponseDto.class);
            userProfileUpdateDto=objectMapper.readValue(new File("src/test/resources/user-profile-update.json"),UserProfileUpdateDto.class);
            userStatusDto=objectMapper.readValue(new File("src/test/resources/profile-status-update.json"),UserStatusDto.class);
        } catch (IOException e) {
            log.info("Json Parsing Exception:"+e.getMessage());
        }
    }

    @Test
    void test_getUserProfileByEmailId() throws Exception {
        String uri=BASE_URI+"/by/email/"+userProfileUpdateDto.getEmailId();
        when(userProfileService.getUserProfileDetails(anyString()))
                .thenReturn(userProfileResponseDto);
        ResponseDto<UserProfileResponseDto> responseDto=new ResponseDto<>();
        responseDto.setStatus("success");
        responseDto.setCode(200);
        responseDto.setMessage("User fetched");
        responseDto.setPayLoad(userProfileResponseDto);
        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(userProfileService).getUserProfileDetails(anyString());
    }


    @Test
    void test_updateUserProfile() throws Exception {
        String uri=BASE_URI+"/update";
        when(userProfileService.updateUserProfile(any(UserProfileUpdateDto.class)))
                .thenReturn(userProfileResponseDto);
        ResponseDto<UserProfileResponseDto> responseDto=new ResponseDto<>();
        responseDto.setStatus("success");
        responseDto.setCode(200);
        responseDto.setMessage("Updated successfully");
        responseDto.setPayLoad(userProfileResponseDto);
        mvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userProfileUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(userProfileService).updateUserProfile(any(UserProfileUpdateDto.class));
    }

    @Test
    void test_updateProfileStatus() throws Exception {
        String uri=BASE_URI+"/update/profileStatus";
        when(userProfileService.updateProfileStatus(any(UserStatusDto.class)))
                .thenReturn(Constants.PROFILE_STATUS_UPDATE_SUCCESS_MESSAGE);
        ResponseDto<String> responseDto=new ResponseDto<>();
        responseDto.setStatus("success");
        responseDto.setCode(200);
        responseDto.setMessage(Constants.PROFILE_STATUS_UPDATE_SUCCESS_MESSAGE);
        responseDto.setPayLoad(Constants.PROFILE_STATUS_UPDATE_SUCCESS_MESSAGE);
        mvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userStatusDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(userProfileService).updateProfileStatus(any(UserStatusDto.class));
    }

    @Test
    void test_searchUser() throws Exception {
        String uri=BASE_URI+"/searchUser/by/skill/"+"java";
        ArrayList<UserProfileResponseDto> responseDtos=new ArrayList<>();
        responseDtos.add(userProfileResponseDto);
        when(userProfileService.searchUser(anyString()))
                .thenReturn(responseDtos);
        ResponseDto<ArrayList<UserProfileResponseDto>> responseDto=new ResponseDto<>();
        responseDto.setStatus("success");
        responseDto.setCode(200);
        responseDto.setMessage("User fetched");
        responseDto.setPayLoad(responseDtos);
        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(userProfileService).searchUser(anyString());
    }

    @Test
    void test_getUserProfileStatusByEmailId() throws Exception {
        String uri=BASE_URI+"/profile/status/by/email/"+userStatusDto.getEmailId();
        when(userProfileService.getUserProfileStatusByEmailId(anyString()))
                .thenReturn(userProfileResponseDto);
        ResponseDto<UserProfileResponseDto> responseDto=new ResponseDto<>();
        responseDto.setStatus(HttpStatus.OK.toString());
        responseDto.setCode(200);
        responseDto.setMessage("Returned the status");
        responseDto.setPayLoad(userProfileResponseDto);
        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(userProfileService).getUserProfileStatusByEmailId(anyString());
    }

    @Test
    public void test_mangerTagging() throws Exception {
        String URI=BASE_URI+"/manager/tagging";
        Workbook workbook=mock(Workbook.class);
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        workbook.write(outputStream);
        when(userProfileService.tagManager(any()))
                .thenReturn(new ByteArrayInputStream(outputStream.toByteArray()));
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World".getBytes()
        );
        mvc.perform(multipart(URI).file(file)).andExpect(status().isOk());
    }

    @Test
    void test_fetchMasterData() throws Exception {
        String url=BASE_URI+"/fetch/all/masterdata";
        MasterData masterData=mock(MasterData.class);
        when(userProfileService.getMasterData())
                .thenReturn(masterData);
        ResponseDto<MasterData> responseDto=new ResponseDto<>();
        responseDto.setStatus(HttpStatus.OK.toString());
        responseDto.setCode(200);
        responseDto.setMessage("Returned all master data");
        responseDto.setPayLoad(masterData);
        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(userProfileService).getMasterData();

    }

}
