package com.tsp.mentorshipService.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserProjectExperienceDto implements Serializable {
    private long id;

    private String projectName;

    private String role;

    private String roleDescription;

    private long duration;

    private String toolsAndFramework;

    private String client;
}
