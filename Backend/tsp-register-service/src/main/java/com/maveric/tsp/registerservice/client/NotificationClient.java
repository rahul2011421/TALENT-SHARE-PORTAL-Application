package com.maveric.tsp.registerservice.client;

import com.maveric.tsp.registerservice.dto.EmailRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient("notify-service")
public interface NotificationClient {
    @PostMapping("/mail/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDto emailRequest);
}