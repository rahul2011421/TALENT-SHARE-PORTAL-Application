package com.tsp.mediaService.service.Impl;

import com.tsp.mediaService.client.IUserService;
import com.tsp.mediaService.dtos.ResponseDto;
import com.tsp.mediaService.dtos.UserProfileResponseDto;
import com.tsp.mediaService.entities.Media;
import com.tsp.mediaService.exceptions.MediaNotFoundException;
import com.tsp.mediaService.repositories.MediaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class MediaServiceImplTest {

    @Mock
    private MediaRepository mediaRepository;

    @InjectMocks
    @Spy
    private MediaServiceImpl mediaService;

    @Mock
    private IUserService userService;

    @Test
    void update_profile_photo_test() throws IOException {
        String emailId = "test@example.com";
        UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto();
        userProfileResponseDto.setEmailId(emailId);
        ResponseDto<UserProfileResponseDto> responseDto = new ResponseDto<>();
        responseDto.setPayLoad(userProfileResponseDto);
        ResponseEntity<ResponseDto<UserProfileResponseDto>> responseEntity = ResponseEntity.ok(responseDto);
        when(userService.getUserProfileByEmailId(emailId)).thenReturn(responseEntity);
        Media savedMedia = new Media();
        savedMedia.setEmailId(emailId);
        savedMedia.setPhoto(new byte[0]);
        when(mediaRepository.findByEmailId(emailId)).thenReturn(Optional.of(savedMedia));
        when(mediaRepository.save(any(Media.class))).thenReturn(savedMedia);

        // Creating a mock MultipartFile
        byte[] content = new byte[0]; // or any byte array you prefer
        MultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", content);

        // Calling the method under test
        mediaService.updateProfilePhoto(emailId, file);

        // Verifying interactions
        verify(userService, times(1)).getUserProfileByEmailId(emailId);
        verify(mediaRepository, times(1)).findByEmailId(emailId);
        verify(mediaRepository, times(1)).save(ArgumentMatchers.any(Media.class));
    }

    @Test
    void testDownloadProfilePhoto_Success() throws MediaNotFoundException {
        String emailId = "test@example.com";
        byte[] photoData = {0, 1, 2, 3};

        // Mocking the behavior of mediaRepository.findByEmailId()
        Media media = new Media();
        media.setPhoto(photoData);
        when(mediaRepository.findByEmailId(emailId)).thenReturn(Optional.of(media));

        // Calling the method under test
        ByteArrayResource resource = mediaService.downloadProfilePhoto(emailId);

        // Verifying the result
        assertArrayEquals(photoData, resource.getByteArray());
    }

    @Test
    void testDownloadProfilePhoto_MediaNotFoundException() {
        String emailId = "test@example.com";

        // Mocking the behavior of mediaRepository.findByEmailId()
        when(mediaRepository.findByEmailId(emailId)).thenReturn(Optional.empty());

        // Calling the method under test and expecting an exception
        assertThrows(MediaNotFoundException.class, () -> mediaService.downloadProfilePhoto(emailId));
    }

}
