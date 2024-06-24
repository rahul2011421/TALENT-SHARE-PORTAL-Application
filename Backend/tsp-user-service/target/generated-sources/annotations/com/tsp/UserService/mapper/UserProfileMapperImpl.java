package com.tsp.UserService.mapper;

import com.tsp.UserService.dtos.UserProfileUpdateDto;
import com.tsp.UserService.dtos.UserProjectExperienceRequestDto;
import com.tsp.UserService.entities.UserProfile;
import com.tsp.UserService.entities.UserProjectExperience;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-24T18:09:44+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class UserProfileMapperImpl implements UserProfileMapper {

    @Autowired
    private UserProjectMapper userProjectMapper;

    @Override
    public UserProfile fromUserProfileUpdateDtoToUserProfileEntity(UserProfileUpdateDto userProfileUpdateDto) {
        if ( userProfileUpdateDto == null ) {
            return null;
        }

        UserProfile userProfile = new UserProfile();

        userProfile.setUserProjectExperienceList( userProjectExperienceRequestDtoListToUserProjectExperienceList( userProfileUpdateDto.getUserProjectExperienceList() ) );
        userProfile.setEmailId( userProfileUpdateDto.getEmailId() );
        userProfile.setProfessionalSummary( userProfileUpdateDto.getProfessionalSummary() );
        userProfile.setDesignation( userProfileUpdateDto.getDesignation() );
        userProfile.setTotalExperience( userProfileUpdateDto.getTotalExperience() );
        userProfile.setProfileStatus( userProfileUpdateDto.isProfileStatus() );
        List<String> list1 = userProfileUpdateDto.getTechnicalSkillList();
        if ( list1 != null ) {
            userProfile.setTechnicalSkillList( new ArrayList<String>( list1 ) );
        }
        userProfile.setSkillCategory( userProfileUpdateDto.getSkillCategory() );
        List<String> list2 = userProfileUpdateDto.getDomainList();
        if ( list2 != null ) {
            userProfile.setDomainList( new ArrayList<String>( list2 ) );
        }

        return userProfile;
    }

    @Override
    public UserProfileUpdateDto fromUserProfileEntityToUserProfileUpdateDto(UserProfile userProfile) {
        if ( userProfile == null ) {
            return null;
        }

        UserProfileUpdateDto userProfileUpdateDto = new UserProfileUpdateDto();

        userProfileUpdateDto.setManagerMailId( userProfileManagerEmailId( userProfile ) );
        userProfileUpdateDto.setUserProjectExperienceList( userProjectExperienceListToUserProjectExperienceRequestDtoList( userProfile.getUserProjectExperienceList() ) );
        userProfileUpdateDto.setEmailId( userProfile.getEmailId() );
        userProfileUpdateDto.setProfessionalSummary( userProfile.getProfessionalSummary() );
        userProfileUpdateDto.setDesignation( userProfile.getDesignation() );
        userProfileUpdateDto.setTotalExperience( userProfile.getTotalExperience() );
        userProfileUpdateDto.setProfileStatus( userProfile.isProfileStatus() );
        userProfileUpdateDto.setSkillCategory( userProfile.getSkillCategory() );
        List<String> list1 = userProfile.getTechnicalSkillList();
        if ( list1 != null ) {
            userProfileUpdateDto.setTechnicalSkillList( new ArrayList<String>( list1 ) );
        }
        List<String> list2 = userProfile.getDomainList();
        if ( list2 != null ) {
            userProfileUpdateDto.setDomainList( new ArrayList<String>( list2 ) );
        }

        return userProfileUpdateDto;
    }

    protected List<UserProjectExperience> userProjectExperienceRequestDtoListToUserProjectExperienceList(List<UserProjectExperienceRequestDto> list) {
        if ( list == null ) {
            return null;
        }

        List<UserProjectExperience> list1 = new ArrayList<UserProjectExperience>( list.size() );
        for ( UserProjectExperienceRequestDto userProjectExperienceRequestDto : list ) {
            list1.add( userProjectMapper.fromUserProjectExperienceDtoToUserProjectExperienceEntity( userProjectExperienceRequestDto ) );
        }

        return list1;
    }

    private String userProfileManagerEmailId(UserProfile userProfile) {
        if ( userProfile == null ) {
            return null;
        }
        UserProfile manager = userProfile.getManager();
        if ( manager == null ) {
            return null;
        }
        String emailId = manager.getEmailId();
        if ( emailId == null ) {
            return null;
        }
        return emailId;
    }

    protected UserProjectExperienceRequestDto userProjectExperienceToUserProjectExperienceRequestDto(UserProjectExperience userProjectExperience) {
        if ( userProjectExperience == null ) {
            return null;
        }

        UserProjectExperienceRequestDto userProjectExperienceRequestDto = new UserProjectExperienceRequestDto();

        userProjectExperienceRequestDto.setProjectName( userProjectExperience.getProjectName() );
        userProjectExperienceRequestDto.setRole( userProjectExperience.getRole() );
        userProjectExperienceRequestDto.setRoleDescription( userProjectExperience.getRoleDescription() );
        userProjectExperienceRequestDto.setDuration( userProjectExperience.getDuration() );
        userProjectExperienceRequestDto.setToolsAndFramework( userProjectExperience.getToolsAndFramework() );
        userProjectExperienceRequestDto.setClient( userProjectExperience.getClient() );

        return userProjectExperienceRequestDto;
    }

    protected List<UserProjectExperienceRequestDto> userProjectExperienceListToUserProjectExperienceRequestDtoList(List<UserProjectExperience> list) {
        if ( list == null ) {
            return null;
        }

        List<UserProjectExperienceRequestDto> list1 = new ArrayList<UserProjectExperienceRequestDto>( list.size() );
        for ( UserProjectExperience userProjectExperience : list ) {
            list1.add( userProjectExperienceToUserProjectExperienceRequestDto( userProjectExperience ) );
        }

        return list1;
    }
}
