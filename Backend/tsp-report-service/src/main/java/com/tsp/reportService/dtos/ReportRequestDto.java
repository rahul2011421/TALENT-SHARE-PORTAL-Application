package com.tsp.reportService.dtos;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Data
public class ReportRequestDto {

    String localStartDate;
    String localEndDate;
    String departmentUnit;

}
