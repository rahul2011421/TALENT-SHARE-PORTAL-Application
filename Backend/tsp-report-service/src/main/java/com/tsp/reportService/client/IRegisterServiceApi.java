package com.tsp.reportService.client;

import com.tsp.reportService.dtos.ReportRequestDto;
import com.tsp.reportService.dtos.ResponseDto;
import com.tsp.reportService.dtos.UserDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

@FeignClient(name = "register-service")
public interface IRegisterServiceApi {

    @GetMapping("/userDetails/fetch/users/between/date/range")
   ResponseEntity<ResponseDto<ArrayList<UserDetailsDto>>> fetchUsersBetweenDataRange(@RequestBody ReportRequestDto reportRequestDto);

}
