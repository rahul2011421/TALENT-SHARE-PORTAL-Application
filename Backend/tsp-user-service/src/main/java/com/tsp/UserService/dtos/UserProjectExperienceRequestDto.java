package com.tsp.UserService.dtos;

import lombok.Data;

@Data
public class UserProjectExperienceRequestDto {

    private String projectName;

    private String role;

    private String roleDescription;

    private long duration;

    private String toolsAndFramework;

    private String client;
}
