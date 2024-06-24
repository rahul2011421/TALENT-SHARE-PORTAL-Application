package com.tsp.mediaService.client;

import com.tsp.mediaService.dtos.ResponseDto;
import com.tsp.mediaService.dtos.UserProfileResponseDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-service")
public interface IUserService {
    /**
     *
     * @param emailId
     * @return
     */
    @GetMapping("/profile/by/email/{emailId}")
    ResponseEntity<ResponseDto<UserProfileResponseDto>> getUserProfileByEmailId(@PathVariable String emailId);

}
