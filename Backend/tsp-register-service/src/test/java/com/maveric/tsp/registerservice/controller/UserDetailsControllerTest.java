package com.maveric.tsp.registerservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maveric.tsp.registerservice.dto.*;
import com.maveric.tsp.registerservice.service.IUserDetailsService;
import com.maveric.tsp.registerservice.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
@WebMvcTest(UserDetailsController.class)
class UserDetailsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IUserDetailsService userDetailsService;

    @MockBean
    TokenUtil tokenUtil;

    private static ObjectMapper objectMapper=new ObjectMapper();

    private static UserDetailsDto userDetails;
    private static LoginDto loginDto;
    private static RegisterDto registerDto;
    private static ResetDto resetDto;
    private static DeleteRequestDto deleteRequestDto;
    private static ReportRequestDto reportRequestDto;


    private static final String BASE_URI="/userDetails";

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

    @Test
    void test_login() throws Exception {
        String uri=BASE_URI+"/login";
        when(userDetailsService.loginUser(loginDto))
                .thenReturn(userDetails);
        ResponseDto<UserDetailsDto> responseDto=new ResponseDto<>();
        responseDto.setCode(200);
        responseDto.setMessage("Logged in successfully");
        responseDto.setStatus("200 OK");
        responseDto.setPayLoad(userDetails);
        mvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(userDetailsService).loginUser(loginDto);
    }

    @Test
    void test_register() throws Exception {
        String uri=BASE_URI+"/register";
        when(userDetailsService.registerUser(registerDto))
                .thenReturn(userDetails);
        ResponseDto<UserDetailsDto> responseDto=new ResponseDto<>();
        responseDto.setCode(201);
        responseDto.setMessage("Registered Successfully");
        responseDto.setStatus(HttpStatus.CREATED.toString());
        responseDto.setPayLoad(userDetails);
        mvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(userDetailsService).registerUser(registerDto);

    }

    @Test
    void test_forgotPassword() throws Exception {
        String uri=BASE_URI+"/forgotPassword/"+userDetails.getEmailId();
        when(userDetailsService.forgotPassword(userDetails.getEmailId()))
                .thenReturn("Password Reset Successfully");
        ResponseDto<String> responseDto=new ResponseDto<>();
        responseDto.setCode(200);
        responseDto.setMessage("New Password had been sent to your emailID:"+userDetails.getEmailId());
        responseDto.setStatus(HttpStatus.OK.toString());
        responseDto.setPayLoad("Password Reset Successfully");
        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(userDetailsService).forgotPassword(userDetails.getEmailId());
    }

    @Test
    void test_resetPassword() throws Exception {
        String uri=BASE_URI+"/reset";
        when(userDetailsService.resetPassword(resetDto))
                .thenReturn("New Password has been reset successfully");
        ResponseDto<String> responseDto=new ResponseDto<>();
        responseDto.setCode(200);
        responseDto.setMessage("Successfully Reset Password");
        responseDto.setStatus(HttpStatus.OK.toString());
        responseDto.setPayLoad("New Password has been reset successfully");
        mvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resetDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(userDetailsService).resetPassword(resetDto);
    }

    @Test
    void test_deleteUsers() throws Exception {
        String uri=BASE_URI+"/delete/Users";
        List<DeleteRequestDto> userToDelete=new ArrayList<>();
        userToDelete.add(deleteRequestDto);
        when(userDetailsService.deleteUser(userToDelete))
                .thenReturn("User’s Successfully Deleted");
        ResponseDto<String> responseDto=new ResponseDto<>();
        responseDto.setCode(200);
        responseDto.setMessage("User Deletion Successfully Completed");
        responseDto.setStatus(HttpStatus.OK.toString());
        responseDto.setPayLoad("User’s Successfully Deleted");
        mvc.perform(delete(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToDelete)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(userDetailsService).deleteUser(userToDelete);
    }

    @Test
    void test_fetchUserByEmailId() throws Exception {
        String uri=BASE_URI+"/fetchUserByEmailId/"+userDetails.getEmailId();
        when(userDetailsService.fetchUserByEmailId(userDetails.getEmailId()))
                .thenReturn(userDetails);
        ResponseDto<UserDetailsDto> responseDto=new ResponseDto<>();
        responseDto.setCode(200);
        responseDto.setMessage("User fetched by EmailID");
        responseDto.setStatus(HttpStatus.OK.toString());
        responseDto.setPayLoad(userDetails);
        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(userDetailsService).fetchUserByEmailId(userDetails.getEmailId());
    }

    @Test
    void test_fetchUserStatus() throws Exception {
        String uri=BASE_URI+"/fetch/status/"+userDetails.getEmailId();
        when(userDetailsService.getUserStatus(userDetails.getEmailId()))
                .thenReturn(true);
        ResponseDto<Boolean> responseDto=new ResponseDto<>();
        responseDto.setCode(200);
        responseDto.setMessage("User status fetched by EmailID");
        responseDto.setStatus(HttpStatus.OK.toString());
        responseDto.setPayLoad(true);
        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(userDetailsService).getUserStatus(userDetails.getEmailId());
    }

    @Test
    void test_fetchMasterData() throws Exception {
        String uri=BASE_URI+"/fetch/all/masterdata";
        when(userDetailsService.getMasterData())
                .thenReturn(mock(MasterDataResponseDto.class));
        ResponseDto<MasterDataResponseDto> responseDto=new ResponseDto<>();
        responseDto.setCode(200);
        responseDto.setMessage("Returned all master data");
        responseDto.setStatus(HttpStatus.OK.toString());
        responseDto.setPayLoad(mock(MasterDataResponseDto.class));
        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(userDetailsService).getMasterData();
    }

    @Test
    void test_fetchUsersBetweenDataRange() throws Exception {
        String uri=BASE_URI+"/fetch/users/between/date/range";
        when(userDetailsService.fetchUsersBetweenDataRange(reportRequestDto))
                .thenReturn(Arrays.asList(userDetails));
        ResponseDto<ArrayList<UserDetailsDto>> responseDto=new ResponseDto();
        responseDto.setCode(200);
        responseDto.setMessage("returened users");
        responseDto.setStatus(HttpStatus.OK.toString());
        responseDto.setPayLoad(new ArrayList(Arrays.asList(userDetails)));
        mvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void test_updateUserDetails() throws Exception {
        String url=BASE_URI+"/update";
        when(userDetailsService.updateUserDetails(any(RegisterDto.class)))
                .thenReturn(userDetails);
        ResponseDto<UserDetailsDto> responseDto=new ResponseDto();
        responseDto.setCode(200);
        responseDto.setMessage("updated User details successfully.");
        responseDto.setStatus(HttpStatus.OK.toString());
        responseDto.setPayLoad(userDetails);
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(userDetailsService).updateUserDetails(any(RegisterDto.class));
    }
}