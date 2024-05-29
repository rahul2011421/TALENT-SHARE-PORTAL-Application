package com.maveric.tsp.mentorshipService.dtos;

import lombok.Data;


@Data
public class EmailRequest {

    private String subject;
    private String message;
    private String recipient;
}
