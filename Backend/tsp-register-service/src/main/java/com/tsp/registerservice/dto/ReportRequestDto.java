package com.tsp.registerservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ReportRequestDto {

    @NotEmpty(message = "Start date should not be null or empty")
    String localStartDate;

    @NotEmpty(message = "End date should not be null or empty")
    String localEndDate;
    String departmentUnit;

}
