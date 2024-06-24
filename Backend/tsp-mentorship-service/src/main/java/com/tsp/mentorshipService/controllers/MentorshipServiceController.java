package com.tsp.mentorshipService.controllers;

import com.tsp.mentorshipService.dtos.*;
import com.tsp.mentorshipService.dtos.*;
import com.tsp.mentorshipService.service.impls.MentorshipServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mentorship/service")
public class MentorshipServiceController {

    @Autowired
    MentorshipServiceImpl mentorshipService;

    /**
    * This is the endpoint API to raise the request for mentorship session from mentee
    */

    @PostMapping("/request/for/mentor-session")
    @Operation(summary = "Mentee session request",description = "Mentee request for a session to Mentor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> raiseRequestForMentorShip(@RequestBody MentorshipRequestDto mentorshipRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto(HttpStatus.CREATED.toString(), 201, "Requested successfully for Session", mentorshipService.raiseRequestForMentorship(mentorshipRequestDto)));
    }

    /**
    * This Api is to get the session details based on date selected.*/

    @GetMapping("/session/details/{emailId}")
    @Operation(summary = "session record",description = "Get the session record of mentee based on date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Mentee Not Found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> fetchSessionRecord(@PathVariable("emailId") String emailId,
                                                          @RequestParam("fromDate") String fromDate,
                                                          @RequestParam("toDate") String toDate)

    {
        List<MentorSessionDto> sessionDetails=mentorshipService.getSessionDetails(emailId,fromDate,toDate);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"Return Session Records",new ArrayList<>(sessionDetails)));

    }

    /**
     * get all the session request based on mentor
    */

    @GetMapping("/mentorShip/requests/{emailId}")
    @Operation(summary = "Mentor and Manager session details for approval",description = "Mentor or Manager can view the list of session which is need to be approved")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> getMentorshipRequests(@PathVariable("emailId") String mentorEmail) {
        List<MentorSessionDto> mentorshipResponseDto =  mentorshipService.getMentorshipRequests(mentorEmail);
        return ResponseEntity.ok(new ResponseDto("success",200,"Mentorship request list fetched", new ArrayList<>(mentorshipResponseDto)));
    }

    /**
    * Once the session had been complete the mentee will update the session conclusion
    */

    @PostMapping("/capture/session/conclusion")
    @Operation(summary = "Capture session conclusion",description = "Once the session had been complete the mentee need to conclude the session manually")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> captureSession(@RequestBody SessionConclusionDto sessionConclusionDto) {
        String result = mentorshipService.sessionConclusion(sessionConclusionDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"Return Session Records",result));
    }

    /**
     * Mentorship approval or rejection */
    @PostMapping("mentorship/approval")
    @Operation(summary = "Mentorship session approval",description ="Mentorship session approves or rejects by mentor and manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    public  ResponseEntity<ResponseDto> mentorshipArrpoval(@RequestBody SessionApprovalRequestDto sessionApprovalRequestDto){
        String response=mentorshipService.mentorshipApproval(sessionApprovalRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"",response));
    }


    /*
        This method is for blocking calendar by mentee based on the dates given by mentor
     */
    @PostMapping("scheduled/date")
    @Operation(summary = "Mentorship session blocking",description ="From mentor suggested date, mentee should select 1 date which is feasible by mentee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    public  ResponseEntity<ResponseDto> scheduledDate(@RequestBody ScheduledDateDto scheduledDateDto){
        String response=mentorshipService.scheduleDateForSession(scheduledDateDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"Blocked Calendar Successfully",response));
    }


}


