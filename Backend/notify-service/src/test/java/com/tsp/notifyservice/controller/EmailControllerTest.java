package com.tsp.notifyservice.controller;

import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tsp.notifyservice.dto.CalendarBlockingInfoDto;
import com.tsp.notifyservice.dto.EmailRequest;
import com.tsp.notifyservice.dto.ResponseDto;
import com.tsp.notifyservice.service.impl.EmailServiceImpl;
import com.tsp.notifyservice.service.impl.TeamsCalendarBlockServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;



@Slf4j
@WebMvcTest(EmailController.class)
class EmailControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TeamsCalendarBlockServiceImpl teamsCalendarBlockService;

    @MockBean
    private EmailServiceImpl emailService;


    private static ObjectMapper objectMapper=new ObjectMapper();

    private static CalendarBlockingInfoDto calendarBlockingInfoDto;
    private static EmailRequest emailRequest;

    private static final String BASE_URI="/mail";

    @BeforeAll
    public static void setUp(){
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.registerModule(new JavaTimeModule());
        try {
            calendarBlockingInfoDto=objectMapper.readValue(new File("src/test/resources/CalendarBlockingInfo.json"),CalendarBlockingInfoDto.class);
        } catch (IOException e) {
            log.info("Json Parsing Exception:"+e.getMessage());
        }
    }


    @Test
    void test_sendMail() throws Exception {
        String uri = BASE_URI + "/send";
        EmailRequest emailRequest = new EmailRequest();
        doNothing().when(emailService).sendMail(any(EmailRequest.class));
        mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Email Sent Successfully"));
        verify(emailService).sendMail(emailRequest);
    }


    @Test
    void test_teamsCalendarBlock() throws Exception {
        String uri=BASE_URI+"/teams/calendar/block";
        when(teamsCalendarBlockService.blockCalendar(calendarBlockingInfoDto))
                .thenReturn("event created successfully");
        ResponseDto<String> responseDto = new ResponseDto<>();
        responseDto.setCode(200);
        responseDto.setMessage("Teams calender blocked successfully");
        responseDto.setStatus("200 OK");
        responseDto.setPayLoad("event created successfully");
        mvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(calendarBlockingInfoDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
                verify(teamsCalendarBlockService).blockCalendar(calendarBlockingInfoDto);
    }

}
