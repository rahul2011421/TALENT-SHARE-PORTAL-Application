package com.tsp.registerservice.service;

import com.tsp.registerservice.dto.*;
import com.tsp.registerservice.dto.*;

import java.util.List;

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
