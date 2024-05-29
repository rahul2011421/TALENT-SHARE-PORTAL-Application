package com.maveric.tsp.UserService.service;

import com.maveric.tsp.UserService.config.MasterData;
import com.maveric.tsp.UserService.dtos.UserProfileResponseDto;
import com.maveric.tsp.UserService.dtos.UserProfileUpdateDto;
import com.maveric.tsp.UserService.dtos.UserStatusDto;
import com.maveric.tsp.UserService.dtos.UsersCount;
import com.maveric.tsp.UserService.entities.UserProfile;
import com.maveric.tsp.UserService.entities.UserProjectExperience;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public interface UserProfileService {

    UserProfileResponseDto getUserProfileDetails(@NotBlank String emailId);


    UserProfileResponseDto updateUserProfile(UserProfileUpdateDto userProfileUpdateRequestDto) throws IOException;


    String updateProfileStatus(UserStatusDto userStatusDto);

    List<UserProfileResponseDto> searchUser(String technicalSkill);

    public List<UserProfile> projectExperience(UserProjectExperience userProjectExperience);


    ByteArrayInputStream tagManager(MultipartFile file);

    UserProfileResponseDto getUserProfileStatusByEmailId(String emailId);

    MasterData getMasterData();

    UsersCount getUsersAndUserGroupCount();
}
