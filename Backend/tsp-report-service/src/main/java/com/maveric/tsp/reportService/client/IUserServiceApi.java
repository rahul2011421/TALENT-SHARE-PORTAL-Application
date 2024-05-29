package com.maveric.tsp.reportService.client;

import com.maveric.tsp.reportService.dtos.ResponseDto;
import com.maveric.tsp.reportService.dtos.UserProfileResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface IUserServiceApi {

    /**
     *
     * @param emailId
     * @return
     */
    @GetMapping("/profile/by/email/{emailId}")
    ResponseEntity<ResponseDto<UserProfileResponseDto>> getUserProfileByEmailId(@PathVariable("emailId") String emailId);
}
