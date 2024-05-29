package com.maveric.tsp.UserService.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserProfileResponseDto implements Serializable {

    private String emailId;
    private Long empId;
    private String firstName;
    private String lastName;
    private String managerName;
    private String userGroup;
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
