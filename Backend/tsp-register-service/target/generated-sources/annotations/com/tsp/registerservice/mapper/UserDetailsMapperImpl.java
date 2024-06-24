package com.tsp.registerservice.mapper;

import com.tsp.registerservice.dto.RegisterDto;
import com.tsp.registerservice.dto.UserDetailsDto;
import com.tsp.registerservice.entities.UserDetails;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-24T17:58:10+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class UserDetailsMapperImpl implements UserDetailsMapper {

    @Override
    public UserDetails fromRegisterDtoToUserDetailsEntity(RegisterDto registerDto) {
        if ( registerDto == null ) {
            return null;
        }

        UserDetails userDetails = new UserDetails();

        userDetails.setEmpId( registerDto.getEmpId() );
        userDetails.setEmailId( registerDto.getEmailId() );
        userDetails.setFirstName( registerDto.getFirstName() );
        userDetails.setLastName( registerDto.getLastName() );
        userDetails.setUserGroup( registerDto.getUserGroup() );
        userDetails.setBusinessUnit( registerDto.getBusinessUnit() );
        userDetails.setCreatedBy( registerDto.getCreatedBy() );

        return userDetails;
    }

    @Override
    public UserDetailsDto fromUserDetailsToUserDetailsDto(UserDetails userDetails) {
        if ( userDetails == null ) {
            return null;
        }

        UserDetailsDto userDetailsDto = new UserDetailsDto();

        userDetailsDto.setEmpId( userDetails.getEmpId() );
        userDetailsDto.setEmailId( userDetails.getEmailId() );
        userDetailsDto.setFirstName( userDetails.getFirstName() );
        userDetailsDto.setLastName( userDetails.getLastName() );
        userDetailsDto.setUserGroup( userDetails.getUserGroup() );
        userDetailsDto.setBusinessUnit( userDetails.getBusinessUnit() );
        userDetailsDto.setStatus( userDetails.isStatus() );
        userDetailsDto.setPasswordReset( userDetails.isPasswordReset() );
        userDetailsDto.setCreatedBy( userDetails.getCreatedBy() );
        userDetailsDto.setExitDate( userDetails.getExitDate() );
        userDetailsDto.setCreatedDateTime( userDetails.getCreatedDateTime() );

        return userDetailsDto;
    }
}
