package com.tsp.registerservice.mapper;

import com.tsp.registerservice.dto.RegisterDto;
import com.tsp.registerservice.dto.UserDetailsDto;
import com.tsp.registerservice.entities.UserDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDetailsMapper {

    UserDetails fromRegisterDtoToUserDetailsEntity(RegisterDto registerDto);
    UserDetailsDto fromUserDetailsToUserDetailsDto(UserDetails userDetails);
}
