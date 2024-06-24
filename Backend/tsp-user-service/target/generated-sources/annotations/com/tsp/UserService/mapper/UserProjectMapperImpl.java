package com.tsp.UserService.mapper;

import com.tsp.UserService.dtos.UserProjectExperienceDto;
import com.tsp.UserService.dtos.UserProjectExperienceRequestDto;
import com.tsp.UserService.entities.UserProjectExperience;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-24T18:09:44+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class UserProjectMapperImpl implements UserProjectMapper {

    @Override
    public UserProjectExperienceDto fromUserProjectExperienceEntityToUserProjectExperienceDto(UserProjectExperience projectExperience) {
        if ( projectExperience == null ) {
            return null;
        }

        UserProjectExperienceDto userProjectExperienceDto = new UserProjectExperienceDto();

        userProjectExperienceDto.setId( projectExperience.getId() );
        userProjectExperienceDto.setProjectName( projectExperience.getProjectName() );
        userProjectExperienceDto.setRole( projectExperience.getRole() );
        userProjectExperienceDto.setRoleDescription( projectExperience.getRoleDescription() );
        userProjectExperienceDto.setDuration( projectExperience.getDuration() );
        userProjectExperienceDto.setToolsAndFramework( projectExperience.getToolsAndFramework() );
        userProjectExperienceDto.setClient( projectExperience.getClient() );

        return userProjectExperienceDto;
    }

    @Override
    public List<UserProjectExperienceDto> fromUserProjectExperienceEntityListToUserProjectExperienceDtoList(List<UserProjectExperience> projectExperiences) {
        if ( projectExperiences == null ) {
            return null;
        }

        List<UserProjectExperienceDto> list = new ArrayList<UserProjectExperienceDto>( projectExperiences.size() );
        for ( UserProjectExperience userProjectExperience : projectExperiences ) {
            list.add( fromUserProjectExperienceEntityToUserProjectExperienceDto( userProjectExperience ) );
        }

        return list;
    }

    @Override
    public UserProjectExperience fromUserProjectExperienceDtoToUserProjectExperienceEntity(UserProjectExperienceRequestDto projectExperienceDto) {
        if ( projectExperienceDto == null ) {
            return null;
        }

        UserProjectExperience userProjectExperience = new UserProjectExperience();

        userProjectExperience.setProjectName( projectExperienceDto.getProjectName() );
        userProjectExperience.setRole( projectExperienceDto.getRole() );
        userProjectExperience.setRoleDescription( projectExperienceDto.getRoleDescription() );
        userProjectExperience.setDuration( projectExperienceDto.getDuration() );
        userProjectExperience.setToolsAndFramework( projectExperienceDto.getToolsAndFramework() );
        userProjectExperience.setClient( projectExperienceDto.getClient() );

        return userProjectExperience;
    }

    @Override
    public List<UserProjectExperience> fromUserProjectExperienceDtoListToUserProjectExperienceEntityList(List<UserProjectExperienceDto> projectExperienceDtos) {
        if ( projectExperienceDtos == null ) {
            return null;
        }

        List<UserProjectExperience> list = new ArrayList<UserProjectExperience>( projectExperienceDtos.size() );
        for ( UserProjectExperienceDto userProjectExperienceDto : projectExperienceDtos ) {
            list.add( userProjectExperienceDtoToUserProjectExperience( userProjectExperienceDto ) );
        }

        return list;
    }

    protected UserProjectExperience userProjectExperienceDtoToUserProjectExperience(UserProjectExperienceDto userProjectExperienceDto) {
        if ( userProjectExperienceDto == null ) {
            return null;
        }

        UserProjectExperience userProjectExperience = new UserProjectExperience();

        userProjectExperience.setId( userProjectExperienceDto.getId() );
        userProjectExperience.setProjectName( userProjectExperienceDto.getProjectName() );
        userProjectExperience.setRole( userProjectExperienceDto.getRole() );
        userProjectExperience.setRoleDescription( userProjectExperienceDto.getRoleDescription() );
        userProjectExperience.setDuration( userProjectExperienceDto.getDuration() );
        userProjectExperience.setToolsAndFramework( userProjectExperienceDto.getToolsAndFramework() );
        userProjectExperience.setClient( userProjectExperienceDto.getClient() );

        return userProjectExperience;
    }
}
