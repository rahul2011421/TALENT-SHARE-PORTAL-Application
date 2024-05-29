package com.maveric.tsp.mentorshipService.client;

import com.maveric.tsp.mentorshipService.dtos.ResponseDto;
import com.maveric.tsp.mentorshipService.dtos.UserProfileResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "user-service")
public interface UserSeriveClient {

    @GetMapping("/profile/by/email/{emailId}")
    public ResponseEntity<ResponseDto<UserProfileResponseDto>> getUserProfileByEmailId(@PathVariable("emailId") String mailId);
}
