package com.maveric.tsp.mediaService.controller;

import com.maveric.tsp.mediaService.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MediaController.class)
@Slf4j
public class MediaControllerTest {

    @MockBean
    private MediaService mediaService;

    private static final String BASE_URI = "/media";
    @Autowired
    private MockMvc mvc;

    @Test
    void upload_photo_Test() throws Exception {
        doNothing().when(mediaService).updateProfilePhoto(anyString(), any(MultipartFile.class));
        String emailId = "test@example.com";
        Path imagePath = Paths.get("src/test/resources/image/download.jpg");
        byte[] imageBytes = Files.readAllBytes(imagePath);
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "download.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                imageBytes
        );
        mvc.perform(MockMvcRequestBuilders.multipart(BASE_URI+"/upload/profilePhoto/{emailId}", emailId)
                .file(file)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                })) .andExpect(status().isOk());
        verify(mediaService, times(1)).updateProfilePhoto(eq(emailId), any(MultipartFile.class));
    }

    @Test
    void download_profile_photo_Test() throws Exception {
        String emailId = "test@example.com";
        byte[] photoBytes = new byte[20];
        ByteArrayResource byteArrayResource = new ByteArrayResource(photoBytes);

        // Mocking the mediaService behavior
        when(mediaService.downloadProfilePhoto(anyString())).thenReturn(byteArrayResource);

        // Perform the GET request
        mvc.perform(get(BASE_URI+"/getProfilePhoto/{emailId}", emailId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(content().bytes(photoBytes));

    }


}
