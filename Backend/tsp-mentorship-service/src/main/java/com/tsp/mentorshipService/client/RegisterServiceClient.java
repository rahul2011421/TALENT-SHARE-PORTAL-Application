package com.tsp.mentorshipService.client;

import com.tsp.mentorshipService.dtos.ResponseDto;
import com.tsp.mentorshipService.dtos.UserDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "register-service")
public interface RegisterServiceClient {

    @GetMapping("/userDetails/fetchUserByEmailId/{emailId}")
    ResponseEntity<ResponseDto<UserDetailsDto>> fetchUserByEmailId(@PathVariable String emailId);
}
