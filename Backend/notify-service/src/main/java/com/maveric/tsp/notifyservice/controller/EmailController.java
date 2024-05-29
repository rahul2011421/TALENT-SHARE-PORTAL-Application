package com.maveric.tsp.notifyservice.controller;

import com.maveric.tsp.notifyservice.dto.CalendarBlockingInfoDto;
import com.maveric.tsp.notifyservice.dto.EmailRequest;
import com.maveric.tsp.notifyservice.dto.ResponseDto;
import com.maveric.tsp.notifyservice.service.impl.EmailServiceImpl;
import com.maveric.tsp.notifyservice.service.impl.TeamsCalendarBlockServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@Slf4j
public class EmailController {

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    TeamsCalendarBlockServiceImpl teamsCalendarBlockService;


    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest){
        log.info("REST API invoked to send mail");
        emailService.sendMail(emailRequest);
        return ResponseEntity.ok("Email Sent Successfully");
    }

    @PostMapping("/teams/calendar/block")
    public ResponseEntity<ResponseDto> blockCalendar(@RequestBody CalendarBlockingInfoDto calender){
//        teamsCalendarBlockService.blockCalendar(calender);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(),200,"Teams calender blocked successfully",teamsCalendarBlockService.blockCalendar(calender)));
    }
}
