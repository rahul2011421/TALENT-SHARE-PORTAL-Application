package com.maveric.tsp.UserService.controller;


import com.maveric.tsp.UserService.dtos.ResponseDto;
import com.maveric.tsp.UserService.dtos.UserProfileUpdateDto;
import com.maveric.tsp.UserService.dtos.UserStatusDto;
import com.maveric.tsp.UserService.service.UserProfileService;
import com.maveric.tsp.UserService.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;


@RestController
@RequestMapping("/profile")
@Tag(name = "UserProfileController", description = "APIs for User Profile Operations")
@Slf4j
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;


    @GetMapping("/by/email/{emailId}")
    @Operation(summary = "Get User Profile by Email ID", description = "Retrieves user profile details by email ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<ResponseDto> getUserProfileByEmailId(@PathVariable("emailId") String mailId){
        return ResponseEntity.ok(new ResponseDto("success",200,"User fetched", userProfileService.getUserProfileDetails(mailId)));
    }


    @PutMapping("/update")
    @Operation(summary = "Update User Profile", description = "Updates user profile details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ResponseDto> updateUserProfile(@RequestBody UserProfileUpdateDto userProfileUpdateRequestDto) throws Throwable {
        return ResponseEntity.ok(new ResponseDto("success",200,"Updated successfully",userProfileService.updateUserProfile(userProfileUpdateRequestDto)));
    }



    @PutMapping("/update/profileStatus")
    @Operation(summary = "Update Profile Status", description = "Updates user profile status to either available or unavailable.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ResponseDto> updateProfileStatus(@RequestBody UserStatusDto userStatusDto) {
        String result=userProfileService.updateProfileStatus(userStatusDto);
        return ResponseEntity.ok(new ResponseDto("success",200,result.equalsIgnoreCase(Constants.PROFILE_STATUS_UPDATE_SUCCESS_MESSAGE)? Constants.PROFILE_STATUS_UPDATE_SUCCESS_MESSAGE:Constants.PROFILE_NOT_FOUND,result));
    }




    @GetMapping("/searchUser/by/skill/{technicalSkill}")
    @Operation(summary = "Search user by TechnicalSkill", description = "Searches the user based on the technical skills")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<ResponseDto> searchUser(@PathVariable String technicalSkill){
        return ResponseEntity.ok(new ResponseDto("success",200,"User fetched", new ArrayList<>(userProfileService.searchUser(technicalSkill))));
    }

    @PostMapping(value = "/manager/tagging",consumes = "multipart/form-data")
    @Operation(summary = "Manager Tagging", description = "Tagging user to a Manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tagging successful",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<Resource> mangerTagging(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName="Final-Talent Share Portal1.xlsx";
        ByteArrayInputStream arrayInputStream=userProfileService.tagManager(file);
        InputStreamResource resfile=new InputStreamResource(arrayInputStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resfile);
    }

    @GetMapping("/profile/status/by/email/{emailId}")
    public ResponseEntity<ResponseDto> getUserProfileStatusByEmailId(@PathVariable("emailId") String emailId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"Returned the status",userProfileService.getUserProfileStatusByEmailId(emailId)));
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
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"Returned all master data",userProfileService.getMasterData()));
    }


    @GetMapping("/fetch/active/unavailable/users/counts")
    @Operation(summary = "Fetch the Active,unavailable users counts and userGroups count", description = "Active and unavailable users count and userGroups count")
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
    public ResponseEntity<ResponseDto> fetchUserCounts(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"Returned all active,unavailable users counts and userGroups count",userProfileService.getUsersAndUserGroupCount()));
    }
}

