package com.maveric.tsp.registerservice.dto;

import lombok.Data;

@Data
public class EmailRequestDto {
    private String subject;
    private String message;
    private String recipient;
}
