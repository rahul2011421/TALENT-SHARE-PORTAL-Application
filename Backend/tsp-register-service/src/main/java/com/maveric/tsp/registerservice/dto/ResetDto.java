package com.maveric.tsp.registerservice.dto;

import com.maveric.tsp.registerservice.validation.PasswordValidation;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResetDto {
    @NotEmpty(message = "EmailId is mandatory and should not be null or empty")
    private String emailId;

    @Column(nullable = false)
    @PasswordValidation
    private String newPassword;

    @Column(nullable = false)
    @PasswordValidation
    private  String confirmPassword;
}
