package com.maveric.tsp.registerservice.controller;

import com.maveric.tsp.registerservice.dto.*;
import com.maveric.tsp.registerservice.service.IUserDetailsService;
import com.maveric.tsp.registerservice.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/userDetails")
@Tag(name = "UserDetailsController", description = "APIs for User Operations")
public class UserDetailsController {
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    IUserDetailsService userDetailsService;

    @PostMapping("/login")
    @Operation(summary = "User Login",description = "Logs in a user with the credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User Not Found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto){
        UserDetailsDto userDetailsDto = userDetailsService.loginUser(loginDto);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new LoginResponseDto(HttpStatus.OK.toString(),200,"Logged in successfully", tokenUtil.generateToken(userDetailsDto), userDetailsDto));
    }
    @PostMapping("/register")
    @Operation(summary = "User Registration",
            description = "Registers a new user with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid Registration Data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflict - User Already Exists",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> register(@RequestBody @Valid RegisterDto registerDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(HttpStatus.CREATED.toString(),201,"Registered Successfully", userDetailsService.registerUser(registerDto)));
    }

    @GetMapping("/forgotPassword/{emailId}")
    @Operation(summary = "Forget Password", description = "Sends a new password to the user's email ID for password recovery.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid Email ID",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User Not Found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> forgetPassword(@PathVariable("emailId") String emailId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"New Password had been sent to your emailID:"+emailId,userDetailsService.forgotPassword(emailId)));
    }
    @PostMapping("/reset")
    @Operation(summary = "Reset Password",description = "Resets the password for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid Reset Data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User Not Found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> resetPassword(@RequestBody @Valid ResetDto resetDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"Successfully Reset Password",userDetailsService.resetPassword(resetDto)));

    }
    @DeleteMapping("/delete/Users")
    @Operation(summary = "Delete User",description = "Deletes a user or many users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid Email ID",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User Not Found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> deleteUsers(@RequestBody @Valid List<DeleteRequestDto> deleteRequestDtos){
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK.toString(),200,"User Deletion Successfully Completed",userDetailsService.deleteUser(deleteRequestDtos)));
    }



    @GetMapping("/fetchUserByEmailId/{emailId}")
    @Operation(summary = "Fetch User By EmailId", description = "Using emailId fetching User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid Email ID",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User Not Found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> fetchUserByEmailId(@PathVariable("emailId") String emailId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"User fetched by EmailID",userDetailsService.fetchUserByEmailId(emailId)));
    }

    @GetMapping("/fetch/status/{emailId}")
    @Operation(summary = "Fetch User's status By EmailId", description = "Using emailId fetching User's status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> fetchUserStatus(@PathVariable("emailId") String emailId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"User status fetched by EmailID",userDetailsService.getUserStatus(emailId)));
    }

    @GetMapping("/fetch/all/masterdata")
    @Operation(summary = "Fetch master data", description = "Master data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> fetchMasterData(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"Returned all master data",userDetailsService.getMasterData()));
    }

    @PostMapping("/fetch/users/between/date/range")
    @Operation(summary = "Fetch all users between given date range and BusinessUnit", description = "Get all users data between date range for archival purposes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched users successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    private ResponseEntity<ResponseDto> fetchUsersBetweenDataRange(@RequestBody @Valid ReportRequestDto reportRequestDto) throws IOException {
        List<UserDetailsDto> users = userDetailsService.fetchUsersBetweenDataRange(reportRequestDto);
        log.info("RESPONSE FROM SERVICE LAYER:"+users);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"returened users",new ArrayList(users)));
    }

    @PostMapping("/update")
    @Operation(summary = "Update the User details", description = "Update the user details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched users successful",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })

    private ResponseEntity<ResponseDto> updateUserDetails(@RequestBody @Valid RegisterDto registerDto) throws IOException {
        UserDetailsDto user = userDetailsService.updateUserDetails(registerDto);
        log.info("RESPONSE FROM SERVICE LAYER:"+user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"updated User details successfully.",user));
    }


    @GetMapping("/fetch/all/users")
    @Operation(summary = "Fetch all active users", description = "Fetch All Active users in the portal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched all users successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    private ResponseEntity<ResponseDto> fetchAllActiveUsers() throws IOException {
        List<UserDetailsDto> users = userDetailsService.fetchAllActiveUsers();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"Fetched all active users successfully.",new ArrayList(users)));
    }

    @GetMapping("/fetch/active/users/count")
    @Operation(summary = "Fetch active users Count", description = "Fetch Active users Count in the portal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched users Count successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    private ResponseEntity<ResponseDto> fetchAllActiveUsersCount() throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"Fetched active users count successfully.",userDetailsService.fetchActiveUsersCount()));
    }

}
