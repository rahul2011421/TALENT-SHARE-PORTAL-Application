package com.maveric.tsp.registerservice.mapper;

import com.maveric.tsp.registerservice.dto.LoginDto;
import com.maveric.tsp.registerservice.dto.RegisterDto;
import com.maveric.tsp.registerservice.dto.UserDetailsDto;
import com.maveric.tsp.registerservice.entities.UserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDetailsMapper {

    UserDetails fromRegisterDtoToUserDetailsEntity(RegisterDto registerDto);
    UserDetailsDto fromUserDetailsToUserDetailsDto(UserDetails userDetails);
}
