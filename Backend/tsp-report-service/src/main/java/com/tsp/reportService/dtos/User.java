package com.tsp.reportService.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class User {
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
    private String managerName;
    private String professionalSummary;
    private String designation;
    private long totalExperience;
    private boolean profileStatus;
    private String skillCategory;
    private String managerMailId;
    private List<String> technicalSkillList;
    List<UserProjectExperienceDto> userProjectExperienceList;
    private List<String> domainList;
}
