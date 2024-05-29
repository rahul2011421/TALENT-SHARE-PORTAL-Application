package com.maveric.tsp.UserService.client;


import com.maveric.tsp.UserService.dtos.MasterDataResponseDto;
import com.maveric.tsp.UserService.dtos.RegisterDto;
import com.maveric.tsp.UserService.dtos.ResponseDto;
import com.maveric.tsp.UserService.dtos.UserDetailsDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("register-service")
public interface RegisterService {

    @GetMapping("/userDetails/fetch/status/{emailId}")
    public ResponseEntity<ResponseDto<Boolean>> getStatus(@PathVariable String emailId);

    @GetMapping("/userDetails/fetchUserByEmailId/{emailId}")
    public ResponseEntity<ResponseDto<UserDetailsDto>> fetchUserByEmailId(@PathVariable("emailId") String emailId);

    @PostMapping("/userDetails/update")
    ResponseEntity<ResponseDto<UserDetailsDto>> updateUserDetails(@RequestBody @Valid RegisterDto registerDto);

    @GetMapping("/userDetails/fetch/all/masterdata")
    ResponseEntity<ResponseDto<MasterDataResponseDto>> fetchMasterData();

    @GetMapping("/userDetails/fetch/active/users/count")
    ResponseEntity<ResponseDto<Long>> fetchAllActiveUsersCount();
}
