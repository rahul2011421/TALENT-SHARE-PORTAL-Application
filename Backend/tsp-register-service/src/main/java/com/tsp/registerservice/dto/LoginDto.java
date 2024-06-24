package com.tsp.registerservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDto {
    @NotEmpty(message = "emailId is mandatory, should not be empty")
    private String emailId;

    @NotEmpty(message = "password is mandatory, should not be empty")
    private String password;
}
