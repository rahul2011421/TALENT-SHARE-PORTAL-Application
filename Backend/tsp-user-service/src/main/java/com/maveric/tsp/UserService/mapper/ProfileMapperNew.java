package com.maveric.tsp.UserService.mapper;

import com.maveric.tsp.UserService.dtos.RegisterDto;
import com.maveric.tsp.UserService.dtos.UserDetailsDto;
import com.maveric.tsp.UserService.dtos.UserProfileResponseDto;
import com.maveric.tsp.UserService.dtos.UserProfileUpdateDto;
import com.maveric.tsp.UserService.entities.UserProfile;
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
