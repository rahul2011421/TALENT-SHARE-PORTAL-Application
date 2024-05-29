package com.maveric.tsp.reportService.controller;

import com.maveric.tsp.reportService.dtos.ReportRequestDto;
import com.maveric.tsp.reportService.dtos.ResponseDto;
import com.maveric.tsp.reportService.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

@RestController
@RequestMapping("/report")
@Slf4j
public class ReportController {

    @Autowired
    ReportService reportService;

    /**
     *
     * @param reportRequestDto
     * @return
     * @throws IOException
     */
    @PostMapping("/fetchAllUsers")
    @Operation(summary = "Fetch User Active and Inactive", description = "Getting both Active and Inactive Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "204", description = "NO CONTENT",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class)))
    })
    private ResponseEntity<ResponseDto> getAllUsersStatus(@RequestBody ReportRequestDto reportRequestDto) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(),200,"Users Data fetched",reportService.getDataDownloaded(reportRequestDto)));
    }
}
