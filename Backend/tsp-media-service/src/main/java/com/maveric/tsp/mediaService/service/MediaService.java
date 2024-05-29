package com.maveric.tsp.mediaService.service;

import com.maveric.tsp.mediaService.exceptions.MediaNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.Serializable;

public interface MediaService {

    void updateProfilePhoto(@NotBlank String emailId, @NotNull MultipartFile file) throws IOException;

    ByteArrayResource downloadProfilePhoto(@NotBlank String emailId) throws MediaNotFoundException;

}
