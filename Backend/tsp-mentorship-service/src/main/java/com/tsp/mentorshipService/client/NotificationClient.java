package com.tsp.mentorshipService.client;

import com.tsp.mentorshipService.dtos.CalendarBlockingInfoDto;
import com.tsp.mentorshipService.dtos.EmailRequest;
import com.tsp.mentorshipService.dtos.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notify-service")
public interface NotificationClient {
    @PostMapping("/mail/send")
    ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest);

    @PostMapping("/mail/teams/calendar/block")
    ResponseEntity<ResponseDto> blockCalendar(@RequestBody CalendarBlockingInfoDto calender);
}
