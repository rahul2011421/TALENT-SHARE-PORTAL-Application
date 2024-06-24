package com.tsp.registerservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DeleteRequestDto {
    @NotEmpty(message = "mail Id is mandatory to delete the user, shouldn't be null or empty")
    private String userMailId;

    @NotEmpty(message = "Exit date should not be null or empty")
    private String exitDate;
}
