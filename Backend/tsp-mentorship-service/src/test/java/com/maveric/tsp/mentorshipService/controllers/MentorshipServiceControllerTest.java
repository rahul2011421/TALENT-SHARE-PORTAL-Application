package com.maveric.tsp.mentorshipService.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.maveric.tsp.mentorshipService.dtos.*;
import org.junit.jupiter.api.Test;
import com.maveric.tsp.mentorshipService.dtos.MentorSessionDto;

import static java.lang.reflect.Array.get;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maveric.tsp.mentorshipService.service.impls.MentorshipServiceImpl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Slf4j
@WebMvcTest(MentorshipServiceController.class)
class MentorshipServiceControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MentorshipServiceImpl mentorshipService;

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static MentorshipRequestDto mentorshipRequestDto;

    private static SessionConclusionDto sessionConclusionDetails;

    private static SessionApprovalRequestDto sessionApprovalRequestDetails;

    private static MentorSessionDto mentorSessionDetails;

    private static ScheduledDateDto scheduledDateDetails;

    private static final String BASE_URI= "/mentorship/service";

    @BeforeAll
    public static void setUp() {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.registerModule(new JavaTimeModule());
        try {
            mentorshipRequestDto = objectMapper.readValue(new File("src/test/resources/mentorshipRequest.json"), MentorshipRequestDto.class);
            sessionConclusionDetails = objectMapper.readValue(new File("src/test/resources/sessionConclusion.json"), SessionConclusionDto.class);
            log.info("*****************sessionConclusionDto:" +sessionConclusionDetails);
            sessionApprovalRequestDetails = objectMapper.readValue(new File("src/test/resources/sessionApprovalRequest.json"), SessionApprovalRequestDto.class);
            mentorSessionDetails = objectMapper.readValue(new File("src/test/resources/mentorSessionDto.json"), MentorSessionDto.class);
            log.info("**********mentorSessionDto:"+mentorSessionDetails);
            scheduledDateDetails = objectMapper.readValue(new File("src/test/resources/scheduledDateDto.json"),ScheduledDateDto.class);
        } catch (IOException e) {
            log.info("Json Parsing Exception:" + e.getMessage());
        }
    }

    @Test
    void test_raiseRequestForMentorShip() throws Exception{
        String uri =BASE_URI + "/request/for/mentor-session";
        when(mentorshipService.raiseRequestForMentorship(mentorshipRequestDto))
                .thenReturn("Requested successfully for Session");
        ResponseDto<String> responseDto=new ResponseDto<>();
        responseDto.setCode(201);
        responseDto.setMessage("Requested successfully for Session");
        responseDto.setStatus("201 CREATED");
        responseDto.setPayLoad("Requested successfully for Session");
        mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mentorshipRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(mentorshipService).raiseRequestForMentorship(mentorshipRequestDto);

    }
    
    @Test
    void test_getMentorshipRequests() throws Exception{
        String uri = BASE_URI + "/mentorShip/requests/" + mentorshipRequestDto.getMentorEmailId();
        ArrayList<MentorSessionDto> mentorSessionList = new ArrayList<>();
        when(mentorshipService.getMentorshipRequests(mentorshipRequestDto.getMentorEmailId()))
                .thenReturn(mentorSessionList);
        ResponseDto<ArrayList<MentorSessionDto>> responseDto=new ResponseDto<>();
        responseDto.setCode(200);
        responseDto.setMessage("Mentorship request list fetched");
        responseDto.setStatus("success");
        responseDto.setPayLoad(mentorSessionList);
        mvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(responseDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(mentorshipService).getMentorshipRequests(mentorshipRequestDto.getMentorEmailId());
    }

    @Test
    void test_captureSession() throws Exception{
        String uri = BASE_URI + "/capture/session/conclusion";
        when(mentorshipService.sessionConclusion(sessionConclusionDetails))
                .thenReturn("Session Concluded Successfully");
        ResponseDto<String> responseDto = new ResponseDto<>();
        responseDto.setCode(200);
        responseDto.setMessage("Return Session Records");
        responseDto.setStatus("200 OK");
        responseDto.setPayLoad("Session Concluded Successfully");
        mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionConclusionDetails)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        verify(mentorshipService).sessionConclusion(sessionConclusionDetails);
    }



}