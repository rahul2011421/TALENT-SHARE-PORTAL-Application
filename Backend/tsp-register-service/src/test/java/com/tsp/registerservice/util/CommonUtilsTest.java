package com.tsp.registerservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tsp.registerservice.config.MasterData;
import com.maveric.tsp.registerservice.dto.*;
import com.tsp.registerservice.dto.ReportRequestDto;
import com.tsp.registerservice.exception.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class CommonUtilsTest {

    @Mock
    private MasterData masterData;

    private static ReportRequestDto reportRequestDto;

    private static ObjectMapper objectMapper=new ObjectMapper();

    @BeforeAll
    public static void setUp(){
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.registerModule(new JavaTimeModule());
        try {
            reportRequestDto=objectMapper.readValue(new File("src/test/resources/talent-share-report-request.json"),ReportRequestDto.class);
        } catch (IOException e) {
            log.info("Json Parsing Exception:"+e.getMessage());
        }
    }

    @InjectMocks
    @Spy
    CommonUtils commonUtils;

    @Test
    void test_validateMasterData_when_given_userGroup_not_available_then_throw_InvalidInputException(){
        ReflectionTestUtils.setField(commonUtils,"masterData",getMasterData());
        Executable executable=()->{
            commonUtils.validateMasterData("mentors","CORE");
        };
        assertThrows(InvalidInputException.class,executable);
    }

    @Test
    void test_validateMasterData_when_given_businessUnit_not_available_then_throw_InvalidInputException(){
        ReflectionTestUtils.setField(commonUtils,"masterData",getMasterData());
        Executable executable=()->{
            commonUtils.validateMasterData("mentor","CORES");
        };
        assertThrows(InvalidInputException.class,executable);
    }

    @Test
    void test_convertToLocaDateFormat(){
        LocalDate date=commonUtils.convertToLocaDateFormat(reportRequestDto.getLocalStartDate());
        assertEquals(date.toString(),reportRequestDto.getLocalStartDate());
    }

    @Test
    void test_validateSystemGeneratedPassword(){
        ReflectionTestUtils.setField(commonUtils,"passwordPattern","^(?=.*[a-zA-Z])(?=.*[0-9].*[0-9])(?=.*[!@#$%^&*()-+=])(?=\\S+$).{8,15}$");
        assertFalse(commonUtils.validateSystemGeneratedPassword("password"));
    }

    private MasterData getMasterData(){
        MasterData masterData1=new MasterData();
        Map<String, List<String>> buinessUnits=new HashMap<>();
        Map<String,List<String>> userGroups=new HashMap<>();
        buinessUnits.put("businessUnits", Arrays.asList("DATA","DIGITAL","QE","CORE"));
        userGroups.put("userGroups",Arrays.asList("ADMIN","MANAGER","MENTOR","MENTEE"));
        masterData1.setBusinessunits(buinessUnits);
        masterData1.setUsergroups(userGroups);
        return masterData1;
    }

}