package com.tsp.notifyservice.dto;

import lombok.Data;

@Data
public class EmailRequest {
    private String subject;
    private String message;
    private String recipient;
}
