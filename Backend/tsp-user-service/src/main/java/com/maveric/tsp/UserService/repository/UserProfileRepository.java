package com.maveric.tsp.UserService.repository;

import com.maveric.tsp.UserService.entities.UserProjectExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import com.maveric.tsp.UserService.entities.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {

    Optional<UserProfile> findByEmailIdAndProfileStatusTrue(String emailId);

    Optional<UserProfile> findByEmailId(String emailId);

    List<UserProfile> findAllByTechnicalSkillList(String technicalSkill);

    List<UserProfile> findAllByUserProjectExperienceList(UserProjectExperience userProjectExperience);

//    UserProjectExperience findByIdAndUserProjectExperienceListProjectName(long profileId, String projectName);

    UserProfile findByUserProjectExperienceListProjectNameAndId(String projectName, Long id);

    List<UserProfile> findByTechnicalSkillListContaining(String technicalSkill);

    List<UserProfile> findByTechnicalSkillListLikeAndManagerNotNull(String technicalSkill);

    List<UserProfile> findByProfileStatusTrue();

    long countByProfileStatusFalse();

    List<UserProfile> findByTechnicalSkillListContainingIgnoreCaseAndManagerNotNullAndProfileStatusTrue(String technicalSkill);
}
