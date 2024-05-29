package com.maveric.tsp.mediaService.controller;

import com.maveric.tsp.mediaService.dtos.ResponseDto;
import com.maveric.tsp.mediaService.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    MediaService mediaService;

    /**
     * @param emailId
     * @param file
     * @return
     * @throws IOException
     */

    @PutMapping(value = "upload/profilePhoto/{emailId}", consumes = "multipart/form-data")
    @Operation(summary = "Update Profile Photo", description = "Updates user profile photo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    public ResponseEntity<ResponseDto> updateProfilePhoto(@PathVariable("emailId") String emailId, @NotNull @RequestParam("file") MultipartFile file) throws IOException {
        mediaService.updateProfilePhoto(emailId, file);
        return ResponseEntity.ok(new ResponseDto("success", 200, "Profile Photo updated successfully", "Photo uploaded"));
    }

    /**
     * @param emailId
     * @return
     * @throws Exception
     */

    @GetMapping(value = "/getProfilePhoto/{emailId}", produces = MediaType.IMAGE_PNG_VALUE)
    @Operation(summary = "Download Profile Photo", description = "Downloaded user profile photo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.IMAGE_PNG_VALUE,
                            schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<Resource> downloadProfilePhoto(@PathVariable("emailId") String emailId) throws Exception {
        ByteArrayResource byteArrayResource = mediaService.downloadProfilePhoto(emailId);
        Resource resource = new ByteArrayResource(byteArrayResource.getByteArray());
        return ResponseEntity
                .ok()
                .body(resource);

    }
}
