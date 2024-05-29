package com.maveric.tsp.UserService.mapper;

import com.maveric.tsp.UserService.dtos.UserProjectExperienceDto;
import com.maveric.tsp.UserService.dtos.UserProjectExperienceRequestDto;
import com.maveric.tsp.UserService.entities.UserProjectExperience;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserProjectMapper {
//    List<UserProjectExperienceDto> fromUserProjectExperienceEntityToUserProjecDto(List<UserProjectExperience> projectExperiences);
//
//    List<UserProjectExperience> fromUserProjectExperienceDtoToUserProjecEntity(List<UserProjectExperienceDto> projectExperienceDtos);

    UserProjectExperienceDto fromUserProjectExperienceEntityToUserProjectExperienceDto(UserProjectExperience projectExperience);

    @IterableMapping(elementTargetType = UserProjectExperienceDto.class)
    List<UserProjectExperienceDto> fromUserProjectExperienceEntityListToUserProjectExperienceDtoList(List<UserProjectExperience> projectExperiences);

    UserProjectExperience fromUserProjectExperienceDtoToUserProjectExperienceEntity(UserProjectExperienceRequestDto projectExperienceDto);

    @IterableMapping(elementTargetType = UserProjectExperience.class)
    List<UserProjectExperience> fromUserProjectExperienceDtoListToUserProjectExperienceEntityList(List<UserProjectExperienceDto> projectExperienceDtos);
}
