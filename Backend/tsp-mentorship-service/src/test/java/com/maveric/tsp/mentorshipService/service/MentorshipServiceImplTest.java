package com.maveric.tsp.mentorshipService.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maveric.tsp.mentorshipService.client.UserSeriveClient;
import com.maveric.tsp.mentorshipService.dtos.*;
import com.maveric.tsp.mentorshipService.entities.SessionDetails;
import com.maveric.tsp.mentorshipService.exceptions.NoDetailsFoundException;
import com.maveric.tsp.mentorshipService.exceptions.NoManagerFoundException;
import com.maveric.tsp.mentorshipService.mappers.MentorshipRequestMapper;
import com.maveric.tsp.mentorshipService.repositories.MentorshipServiceRepo;
import com.maveric.tsp.mentorshipService.service.impls.MentorshipServiceImpl;
import com.maveric.tsp.mentorshipService.utils.CommonUtils;
import com.maveric.tsp.mentorshipService.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class MentorshipServiceImplTest {
    @Mock
    private MentorshipServiceRepo mentorshipServiceRepo;

    @Mock
    private CommonUtils commonUtils;

    @Mock
    private UserSeriveClient userSeriveClient;

    @InjectMocks
    private MentorshipServiceImpl mentorshipService;

    @Mock
    private MentorshipRequestMapper mentorshipRequestMapper;

    private static ObjectMapper objectMapper=new ObjectMapper();
    private static MentorshipRequestDto mentorshipRequestDto;
    private static SessionConclusionDto sessionConclusionDto;
    public static String jsonData;
    public static JsonNode jsonNode;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeAll
    public static void setUp(){
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.registerModule(new JavaTimeModule());
        try{
            jsonData = new String(Files.readAllBytes(Paths.get("src/test/resources/sessionHistory.json")));
            jsonNode = objectMapper.readTree(jsonData);
            mentorshipRequestDto=objectMapper.readValue(new File("src/test/resources/requestMentorSession.json"),MentorshipRequestDto.class);
            sessionConclusionDto=objectMapper.readValue(new File("src/test/resources/sessionConclusion.json"),SessionConclusionDto.class);
        } catch (IOException e) {
            log.info("Json Parsing Exception:"+e.getMessage());
        }
    }

    @Test
    void test_getSessionDetails_ForMentee(){
        String emailId = jsonNode.get("mentee_emailId").asText();
        String fromDate = jsonNode.get("fromDate").asText();
        String toDate = jsonNode.get("toDate").asText();
        LocalDate localStartDate = LocalDate.parse(fromDate);
        LocalDate localToDate = LocalDate.parse(toDate);
        // Mocking
        UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto();
        userProfileResponseDto.setUserGroup(Constants.MENTEE);
        ResponseDto<UserProfileResponseDto> responseDto = new ResponseDto<>();
        responseDto.setPayLoad(userProfileResponseDto);
        when(userSeriveClient.getUserProfileByEmailId(emailId)).thenReturn(ResponseEntity.ok(responseDto));
        when(commonUtils.sessionDetailsHistoryDate(anyString())).thenReturn(localStartDate);
        when(commonUtils.sessionDetailsHistoryDate(anyString())).thenReturn(localToDate);
        when(mentorshipServiceRepo.findByMenteeEmailIdAndFromDateBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        // Method call
        List<MentorSessionDto> result = mentorshipService.getSessionDetails(emailId, fromDate, toDate);
        // Verification
        verify(userSeriveClient, times(1)).getUserProfileByEmailId(emailId);
        verify(commonUtils, times(1)).sessionDetailsHistoryDate(fromDate);
        verify(commonUtils, times(1)).sessionDetailsHistoryDate(toDate);
        verify(mentorshipServiceRepo, times(1)).findByMenteeEmailIdAndFromDateBetween(anyString(),any(LocalDateTime.class),any(LocalDateTime.class));
    }

    @Test
    void test_getSessionDetails_ForMentor(){
        String emailId = jsonNode.get("mentor_emailId").asText();
        String fromDate = jsonNode.get("fromDate").asText();
        String toDate = jsonNode.get("toDate").asText();
        LocalDate localStartDate = LocalDate.parse(fromDate);
        LocalDate localToDate = LocalDate.parse(toDate);
        // Mocking
        UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto();
        userProfileResponseDto.setUserGroup(Constants.MENTEE);
        ResponseDto<UserProfileResponseDto> responseDto = new ResponseDto<>();
        responseDto.setPayLoad(userProfileResponseDto);
        when(userSeriveClient.getUserProfileByEmailId(emailId)).thenReturn(ResponseEntity.ok(responseDto));
        when(commonUtils.sessionDetailsHistoryDate(anyString())).thenReturn(localStartDate);
        when(commonUtils.sessionDetailsHistoryDate(anyString())).thenReturn(localToDate);
        when(mentorshipServiceRepo.findByMenteeEmailIdAndFromDateBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());
        // Method call
        List<MentorSessionDto> result = mentorshipService.getSessionDetails(emailId, fromDate, toDate);
        // Verification
        verify(userSeriveClient, times(1)).getUserProfileByEmailId(emailId);
        verify(commonUtils, times(1)).sessionDetailsHistoryDate(fromDate);
        verify(commonUtils, times(1)).sessionDetailsHistoryDate(toDate);
        verify(mentorshipServiceRepo, times(1)).findByMenteeEmailIdAndFromDateBetween(anyString(),any(LocalDateTime.class),any(LocalDateTime.class));
    }

    @Test
    void test_raiseRequestForMentorship(){

        UserProfileResponseDto userProfileResponseDto = mock(UserProfileResponseDto.class);
        ResponseDto<UserProfileResponseDto> responseDto = new ResponseDto();
        when(userProfileResponseDto.getManagerMailId()).thenReturn("manager@example.com");
        responseDto.setPayLoad(userProfileResponseDto);
        SessionDetails sessionDetails=new SessionDetails();
        when(mentorshipRequestMapper.fromMentorshipRequestDtoToSessionDetails(mentorshipRequestDto))
                .thenReturn(sessionDetails);
        when(mentorshipServiceRepo.save(any(SessionDetails.class)))
                .thenReturn(sessionDetails);
        when(userSeriveClient.getUserProfileByEmailId(anyString()))
                .thenReturn(ResponseEntity.ok(responseDto));
        mentorshipService.raiseRequestForMentorship(mentorshipRequestDto);
        verify(mentorshipServiceRepo).save(sessionDetails);
        verify(userSeriveClient).getUserProfileByEmailId(anyString());
    }


    @Test
    void sessionConclusion_Positive_Scenario(){
        SessionDetails sessionDetails = new SessionDetails();

        when(mentorshipServiceRepo.findById(anyLong())).thenReturn(Optional.of(sessionDetails));
        String result = mentorshipService.sessionConclusion(sessionConclusionDto);
        verify(mentorshipServiceRepo).findById(anyLong());
        verify(mentorshipServiceRepo).save(sessionDetails);

    }

    @Test
    void sessionConclusion_Negative_Scenario(){
        doThrow(new NoDetailsFoundException(Constants.NO_DETAILS_FOUND_EXCEPTION))
                .when(mentorshipServiceRepo).findById(anyLong());
        assertThrows(NoDetailsFoundException.class,()->{
            mentorshipService.sessionConclusion(sessionConclusionDto);
        });
        verify(mentorshipServiceRepo).findById(anyLong());

    }

}
