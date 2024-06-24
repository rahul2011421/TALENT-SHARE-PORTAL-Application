package com.tsp.UserService.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
