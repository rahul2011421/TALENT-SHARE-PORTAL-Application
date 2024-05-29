package com.maveric.tsp.mentorshipService.dtos;

import com.maveric.tsp.mentorshipService.enums.BusinessUnit;
import com.maveric.tsp.mentorshipService.enums.UserGroup;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ToString
public class UserDetailsDto implements Serializable {
    private Long empId;
    private String emailId;
    private String firstName;
    private String lastName;
    private UserGroup userGroup;
    private BusinessUnit businessUnit;
    private boolean status;
    private boolean passwordReset;
    private String createdBy;
    private LocalDateTime createdDateTime;
}
