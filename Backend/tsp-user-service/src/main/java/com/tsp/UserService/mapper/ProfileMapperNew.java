package com.tsp.UserService.mapper;

import com.tsp.UserService.dtos.RegisterDto;
import com.tsp.UserService.dtos.UserDetailsDto;
import com.tsp.UserService.dtos.UserProfileResponseDto;
import com.tsp.UserService.dtos.UserProfileUpdateDto;
import com.tsp.UserService.entities.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapperNew {

    @Mapping(source = "profile.manager.emailId",target = "managerMailId")
    @Mapping(source = "profile.emailId",target = "emailId")
    UserProfileResponseDto fromEntitiesToUserProfileResponseDto(UserProfile profile, UserDetailsDto detailsDto);

    RegisterDto fromUserProfileUpdateDtoToRegisterDto(UserProfileUpdateDto userProfileUpdateDto);

    UserProfile fromUserProfileUpdateDtoToUserProfile(UserProfileUpdateDto userProfileUpdateDto);

}
