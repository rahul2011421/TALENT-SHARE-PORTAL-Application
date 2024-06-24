package com.tsp.reportService.service.Impl;


import com.tsp.reportService.client.IRegisterServiceApi;
import com.tsp.reportService.client.IUserServiceApi;
import com.tsp.reportService.dtos.*;
import com.tsp.reportService.dtos.*;
import com.tsp.reportService.exception.NoDataFoundException;
import com.tsp.reportService.service.ReportService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private IRegisterServiceApi registerServiceApi;

    @Autowired
    private IUserServiceApi userServiceApi;

    /**
     *
     * @param reportRequestDto
     * @return
     * @throws IOException
     */

    @Override
    public ArrayList<User> getDataDownloaded(ReportRequestDto reportRequestDto) throws IOException {
        List<UserDetailsDto> userDetailsDtos = getAllUsersFromRegisterService(reportRequestDto);
        ArrayList<User> userDtoList = new ArrayList<>();
        for (UserDetailsDto userDetailsDto:userDetailsDtos) {
            UserProfileResponseDto userProfileUpdateDto = getUserProfileFromUserProfileService(userDetailsDto.getEmailId());
            User userDto=new User();
            BeanUtils.copyProperties(userProfileUpdateDto,userDto);
            BeanUtils.copyProperties(userDetailsDto,userDto);
            log.info("User DTO::"+userDto);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    /**
     *
     * @param emailId
     * @return
     */

    private UserProfileResponseDto getUserProfileFromUserProfileService(String emailId) {
        ResponseEntity<ResponseDto<UserProfileResponseDto>> userProfileUpdateDto;
        try{
            userProfileUpdateDto = userServiceApi.getUserProfileByEmailId(emailId);
        }catch (FeignException exception){
            return new UserProfileResponseDto();
        }
        return Objects.requireNonNull(userProfileUpdateDto.getBody()).getPayLoad();
    }

    /**
     *
     * @param reportRequestDto
     * @return
     */
    private List<UserDetailsDto> getAllUsersFromRegisterService(ReportRequestDto reportRequestDto) {
        ResponseEntity<ResponseDto<ArrayList<UserDetailsDto>>> userDetailsDtoList;
        try{
            userDetailsDtoList = registerServiceApi.fetchUsersBetweenDataRange(reportRequestDto);
        }catch (FeignException exception){
            throw new FeignException.BadRequest(exception.getMessage(),exception.request(),null,null);
        }
        if (Objects.requireNonNull(userDetailsDtoList.getBody()).getPayLoad().isEmpty()) {
            throw new NoDataFoundException("No Users Found between given date range : " + reportRequestDto.getLocalStartDate() + " to " + reportRequestDto.getLocalEndDate());
        }
        return Objects.requireNonNull(userDetailsDtoList.getBody()).getPayLoad();
    }


}
