package com.tsp.UserService.mapper;

import com.tsp.UserService.dtos.RegisterDto;
import com.tsp.UserService.dtos.UserDetailsDto;
import com.tsp.UserService.dtos.UserProfileResponseDto;
import com.tsp.UserService.dtos.UserProfileUpdateDto;
import com.tsp.UserService.dtos.UserProjectExperienceDto;
import com.tsp.UserService.dtos.UserProjectExperienceRequestDto;
import com.tsp.UserService.entities.UserProfile;
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
public class ProfileMapperNewImpl implements ProfileMapperNew {

    @Override
    public UserProfileResponseDto fromEntitiesToUserProfileResponseDto(UserProfile profile, UserDetailsDto detailsDto) {
        if ( profile == null && detailsDto == null ) {
            return null;
        }

        UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto();

        if ( profile != null ) {
            userProfileResponseDto.setManagerMailId( profileManagerEmailId( profile ) );
            userProfileResponseDto.setEmailId( profile.getEmailId() );
            userProfileResponseDto.setProfessionalSummary( profile.getProfessionalSummary() );
            userProfileResponseDto.setDesignation( profile.getDesignation() );
            userProfileResponseDto.setTotalExperience( profile.getTotalExperience() );
            userProfileResponseDto.setProfileStatus( profile.isProfileStatus() );
            userProfileResponseDto.setSkillCategory( profile.getSkillCategory() );
            List<String> list = profile.getTechnicalSkillList();
            if ( list != null ) {
                userProfileResponseDto.setTechnicalSkillList( new ArrayList<String>( list ) );
            }
            userProfileResponseDto.setUserProjectExperienceList( userProjectExperienceListToUserProjectExperienceDtoList( profile.getUserProjectExperienceList() ) );
            List<String> list2 = profile.getDomainList();
            if ( list2 != null ) {
                userProfileResponseDto.setDomainList( new ArrayList<String>( list2 ) );
            }
        }
        if ( detailsDto != null ) {
            userProfileResponseDto.setEmpId( detailsDto.getEmpId() );
            userProfileResponseDto.setFirstName( detailsDto.getFirstName() );
            userProfileResponseDto.setLastName( detailsDto.getLastName() );
            userProfileResponseDto.setUserGroup( detailsDto.getUserGroup() );
        }

        return userProfileResponseDto;
    }

    @Override
    public RegisterDto fromUserProfileUpdateDtoToRegisterDto(UserProfileUpdateDto userProfileUpdateDto) {
        if ( userProfileUpdateDto == null ) {
            return null;
        }

        RegisterDto.RegisterDtoBuilder registerDto = RegisterDto.builder();

        registerDto.firstName( userProfileUpdateDto.getFirstName() );
        registerDto.lastName( userProfileUpdateDto.getLastName() );
        registerDto.emailId( userProfileUpdateDto.getEmailId() );
        registerDto.empId( userProfileUpdateDto.getEmpId() );
        registerDto.userGroup( userProfileUpdateDto.getUserGroup() );
        registerDto.businessUnit( userProfileUpdateDto.getBusinessUnit() );

        return registerDto.build();
    }

    @Override
    public UserProfile fromUserProfileUpdateDtoToUserProfile(UserProfileUpdateDto userProfileUpdateDto) {
        if ( userProfileUpdateDto == null ) {
            return null;
        }

        UserProfile userProfile = new UserProfile();

        userProfile.setEmailId( userProfileUpdateDto.getEmailId() );
        userProfile.setProfessionalSummary( userProfileUpdateDto.getProfessionalSummary() );
        userProfile.setDesignation( userProfileUpdateDto.getDesignation() );
        userProfile.setTotalExperience( userProfileUpdateDto.getTotalExperience() );
        userProfile.setProfileStatus( userProfileUpdateDto.isProfileStatus() );
        userProfile.setUserProjectExperienceList( userProjectExperienceRequestDtoListToUserProjectExperienceList( userProfileUpdateDto.getUserProjectExperienceList() ) );
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

    private String profileManagerEmailId(UserProfile userProfile) {
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

    protected UserProjectExperienceDto userProjectExperienceToUserProjectExperienceDto(UserProjectExperience userProjectExperience) {
        if ( userProjectExperience == null ) {
            return null;
        }

        UserProjectExperienceDto userProjectExperienceDto = new UserProjectExperienceDto();

        userProjectExperienceDto.setId( userProjectExperience.getId() );
        userProjectExperienceDto.setProjectName( userProjectExperience.getProjectName() );
        userProjectExperienceDto.setRole( userProjectExperience.getRole() );
        userProjectExperienceDto.setRoleDescription( userProjectExperience.getRoleDescription() );
        userProjectExperienceDto.setDuration( userProjectExperience.getDuration() );
        userProjectExperienceDto.setToolsAndFramework( userProjectExperience.getToolsAndFramework() );
        userProjectExperienceDto.setClient( userProjectExperience.getClient() );

        return userProjectExperienceDto;
    }

    protected List<UserProjectExperienceDto> userProjectExperienceListToUserProjectExperienceDtoList(List<UserProjectExperience> list) {
        if ( list == null ) {
            return null;
        }

        List<UserProjectExperienceDto> list1 = new ArrayList<UserProjectExperienceDto>( list.size() );
        for ( UserProjectExperience userProjectExperience : list ) {
            list1.add( userProjectExperienceToUserProjectExperienceDto( userProjectExperience ) );
        }

        return list1;
    }

    protected UserProjectExperience userProjectExperienceRequestDtoToUserProjectExperience(UserProjectExperienceRequestDto userProjectExperienceRequestDto) {
        if ( userProjectExperienceRequestDto == null ) {
            return null;
        }

        UserProjectExperience userProjectExperience = new UserProjectExperience();

        userProjectExperience.setProjectName( userProjectExperienceRequestDto.getProjectName() );
        userProjectExperience.setRole( userProjectExperienceRequestDto.getRole() );
        userProjectExperience.setRoleDescription( userProjectExperienceRequestDto.getRoleDescription() );
        userProjectExperience.setDuration( userProjectExperienceRequestDto.getDuration() );
        userProjectExperience.setToolsAndFramework( userProjectExperienceRequestDto.getToolsAndFramework() );
        userProjectExperience.setClient( userProjectExperienceRequestDto.getClient() );

        return userProjectExperience;
    }

    protected List<UserProjectExperience> userProjectExperienceRequestDtoListToUserProjectExperienceList(List<UserProjectExperienceRequestDto> list) {
        if ( list == null ) {
            return null;
        }

        List<UserProjectExperience> list1 = new ArrayList<UserProjectExperience>( list.size() );
        for ( UserProjectExperienceRequestDto userProjectExperienceRequestDto : list ) {
            list1.add( userProjectExperienceRequestDtoToUserProjectExperience( userProjectExperienceRequestDto ) );
        }

        return list1;
    }
}
