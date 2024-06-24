package com.tsp.mentorshipService.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class CalendarBlockingInfoDto {
    private LocalDateTime fromDateAndTime;
    private String fromTimeZone;
    private LocalDateTime endDateAndTime;
    private String endTimeZone;
    private String mentorEmail;
    private String menteeEmail;
    private String sessionTopic;
    private String location;
    private String content;
}
