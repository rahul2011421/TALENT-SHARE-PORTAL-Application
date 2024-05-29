package com.maveric.tsp.reportService.dtos;

import lombok.Data;

@Data
public class UserProjectExperienceDto {

    private long id;

    private String projectName;

    private String role;

    private String roleDescription;

    private long duration;

    private String toolsAndFramework;

    private String client;
}
