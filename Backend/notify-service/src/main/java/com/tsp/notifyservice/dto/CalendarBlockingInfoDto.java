package com.tsp.notifyservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalendarBlockingInfoDto implements Serializable {
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
