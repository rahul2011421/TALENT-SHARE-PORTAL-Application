package com.maveric.tsp.UserService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProjectExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String projectName;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String roleDescription;

    @Column(nullable = false)
    private long duration;

    @Column(nullable = false)
    private String toolsAndFramework;

    @Column(nullable = false)
    private String client;

}
