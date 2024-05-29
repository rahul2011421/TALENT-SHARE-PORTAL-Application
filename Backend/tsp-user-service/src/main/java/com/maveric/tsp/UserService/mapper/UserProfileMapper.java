package com.maveric.tsp.UserService.mapper;


import com.maveric.tsp.UserService.dtos.UserDetailsDto;
import com.maveric.tsp.UserService.dtos.UserProfileResponseDto;
import com.maveric.tsp.UserService.dtos.UserProfileUpdateDto;
import com.maveric.tsp.UserService.entities.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {UserProjectMapper.class})
public interface UserProfileMapper {

//    @Mapping(source = "managerMailId",target = "manager.emailId")
    @Mapping(target = "userProjectExperienceList", source = "userProfileUpdateDto.userProjectExperienceList")
    UserProfile fromUserProfileUpdateDtoToUserProfileEntity(UserProfileUpdateDto userProfileUpdateDto);


    @Mapping(source = "manager.emailId",target = "managerMailId")
    @Mapping(target = "userProjectExperienceList", source = "userProfile.userProjectExperienceList")
    UserProfileUpdateDto fromUserProfileEntityToUserProfileUpdateDto(UserProfile userProfile);

//    @Mapping(source = "userProfile.manager.emailId",target = "managerMailId")
//    @Mapping(target = "userProjectExperienceList", source = "userProfile.userProjectExperienceList")
//    UserProfileResponseDto fromUserProfileEntityToUserProfileResponseDto(UserProfile userProfile, UserDetailsDto userDetailsDto);
}
