package com.tsp.registerservice.client;

import com.tsp.registerservice.dto.ResponseDto;
import com.tsp.registerservice.dto.UserStatusDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("user-service")
public interface UserserviceClient {

    @PutMapping("/profile/update/profileStatus")
    ResponseEntity<ResponseDto> updateProfileStatus(@RequestBody UserStatusDto userStatusDto);
}
