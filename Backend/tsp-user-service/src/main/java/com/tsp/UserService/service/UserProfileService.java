package com.tsp.UserService.service;

import com.tsp.UserService.config.MasterData;
import com.tsp.UserService.dtos.UserProfileResponseDto;
import com.tsp.UserService.dtos.UserProfileUpdateDto;
import com.tsp.UserService.dtos.UserStatusDto;
import com.tsp.UserService.dtos.UsersCount;
import com.tsp.UserService.entities.UserProfile;
import com.tsp.UserService.entities.UserProjectExperience;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayInputStream;
import java.io.IOException;
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
