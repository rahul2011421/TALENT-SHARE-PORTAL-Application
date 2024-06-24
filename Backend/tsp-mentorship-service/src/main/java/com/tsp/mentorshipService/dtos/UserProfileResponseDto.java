package com.tsp.mentorshipService.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class UserProfileResponseDto implements Serializable {
        @Email
        private String emailId;
        private Long empId;
        private String firstName;
        private String lastName;
        private String managerName;
        private String userGroup;
        @NotNull
        private String professionalSummary;
        @NotNull
        private String designation;

        private long totalExperience;

        private boolean profileStatus;

        private String skillCategory;
        @NotNull
        @Email
        private String managerMailId;

        private List<String> technicalSkillList;

        List<UserProjectExperienceDto> userProjectExperienceList;

        private List<String> domainList;
}
