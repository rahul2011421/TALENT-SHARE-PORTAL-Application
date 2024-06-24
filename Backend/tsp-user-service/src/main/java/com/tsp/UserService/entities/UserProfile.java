package com.tsp.UserService.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String emailId;

  @Column(nullable = false)
  private String professionalSummary;

  @Column(nullable = false)
  private String designation;

  @Column(nullable = false)
  private long totalExperience;

  @Column(nullable = false)
  private boolean profileStatus;

  @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},orphanRemoval = true)
  private List<UserProjectExperience> userProjectExperienceList;

  @ElementCollection
  @Column(name = "technical_skill")
  private List<String> technicalSkillList;

  @Column(name = "skill_category")
  private String skillCategory;

  @ElementCollection
  private List<String> domainList;

  @ManyToOne
  @JoinColumn(name = "manager_id",nullable = true)
  private UserProfile manager;

  @PrePersist
  public void prePersist(){
    profileStatus=true;
  }
}
