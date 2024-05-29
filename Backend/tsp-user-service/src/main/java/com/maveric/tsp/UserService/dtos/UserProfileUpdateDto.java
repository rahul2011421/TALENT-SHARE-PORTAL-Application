package com.maveric.tsp.UserService.dtos;

import com.maveric.tsp.UserService.entities.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateDto implements Serializable {

    private Long empId;

    private String emailId;

    private String firstName;

    private String lastName;

    private String userGroup;

    private String businessUnit;

    private String professionalSummary;

    private String designation;

    private long totalExperience;

    private boolean profileStatus;

    private String skillCategory;

    private String managerMailId;

    private List<String> technicalSkillList;

    List<UserProjectExperienceRequestDto> userProjectExperienceList;

    private List<String> domainList;
}