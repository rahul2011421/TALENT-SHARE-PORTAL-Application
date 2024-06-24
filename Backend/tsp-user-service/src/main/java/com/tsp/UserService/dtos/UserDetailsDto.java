package com.tsp.UserService.dtos;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ToString
public class UserDetailsDto implements Serializable {
    private Long empId;
    private String emailId;
    private String firstName;
    private String lastName;
    private String userGroup;
    private String businessUnit;
    private boolean status;
    private boolean passwordReset;
    private String createdBy;
    private LocalDate exitDate;
    private LocalDateTime createdDateTime;
}
