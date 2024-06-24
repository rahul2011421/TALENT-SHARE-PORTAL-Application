package com.tsp.mediaService.service.Impl;

import com.tsp.mediaService.client.IUserService;
import com.tsp.mediaService.dtos.ResponseDto;
import com.tsp.mediaService.dtos.UserProfileResponseDto;
import com.tsp.mediaService.entities.Media;
import com.tsp.mediaService.exceptions.MediaNotFoundException;
import com.tsp.mediaService.repositories.MediaRepository;
import com.tsp.mediaService.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class MediaServiceImpl implements MediaService {

    @Autowired
    IUserService userService;

    @Autowired
    MediaRepository mediaRepository;

    /**
     * @param emailId
     * @param file
     * @throws IOException
     * This method is used for uploading photo
     */
    @Override
    public void updateProfilePhoto(String emailId, MultipartFile file) throws IOException {
        String mailId = getEmailId(emailId);
        log.info("EmailId:" + mailId);
        byte[] imageData = file.getBytes();
        Optional<Media> mediaOptional=mediaRepository.findByEmailId(emailId);
        if(mediaOptional.isPresent()){
            Media media = mediaOptional.get();
            media.setPhoto(imageData);
            mediaRepository.save(media);
        }else{
            Media media = new Media();
            media.setPhoto(imageData);
            media.setEmailId(mailId);
            mediaRepository.save(media);
        }

    }

    /**
     * @param emailId
     * @return
     * @throws MediaNotFoundException
     * This method is used for downloading photo
     */
    @Override
    public ByteArrayResource downloadProfilePhoto(String emailId) throws MediaNotFoundException {
        Optional<Media> optional = mediaRepository.findByEmailId(emailId);
        Media media = optional.orElseThrow(
                () -> new MediaNotFoundException(""));
        return new ByteArrayResource(media.getPhoto());

    }

    /**
     * @param emailId
     * @return
     */
    public String getEmailId(String emailId) {
        ResponseEntity<ResponseDto<UserProfileResponseDto>> userResponseDto = userService.getUserProfileByEmailId(emailId);
        UserProfileResponseDto userProfileResponseDto = userResponseDto.getBody().getPayLoad();
        return userProfileResponseDto.getEmailId();
    }


}
