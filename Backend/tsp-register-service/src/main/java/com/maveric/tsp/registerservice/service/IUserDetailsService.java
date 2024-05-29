package com.maveric.tsp.registerservice.service;

import com.maveric.tsp.registerservice.config.MasterData;
import com.maveric.tsp.registerservice.dto.*;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IUserDetailsService {
     UserDetailsDto loginUser(LoginDto loginDto);
     UserDetailsDto registerUser(RegisterDto registerDto);
    String forgotPassword(String emailId);
    String deleteUser(List<DeleteRequestDto> emailIds);

    String resetPassword(ResetDto resetDto);

    UserDetailsDto fetchUserByEmailId(String emailId);

    boolean getUserStatus(String emailId);

    List<UserDetailsDto> getAllUsers();

    List<UserDetailsDto> fetchUsersBetweenDataRange(ReportRequestDto reportRequestDto);

    MasterDataResponseDto getMasterData();

    UserDetailsDto updateUserDetails(RegisterDto registerDto);

    List<UserDetailsDto> fetchAllActiveUsers();

    long fetchActiveUsersCount();
}
