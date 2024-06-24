package com.tsp.registerservice.util;


import com.tsp.registerservice.dto.UserDetailsDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Slf4j
public class TokenUtil {
    @Value("${tsp.KEY}")
    private String KEY;
    @Value("${tsp.token.validity}")
    private long TOKEN_VALIDITY;

    @Value("${tsp.algorithm}")
    private String EncryptionAlgorithm;


    public String generateToken(UserDetailsDto userDetails) {
        log.debug("encode KEY: " + KEY);
        LocalDateTime expiryTime = LocalDateTime.now().plus(Duration.of(TOKEN_VALIDITY, ChronoUnit.MINUTES));
        Date expiryDate = Date.from(expiryTime.atZone(ZoneId.systemDefault()).toInstant());
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.forName(EncryptionAlgorithm), KEY)
                .setSubject(userDetails.getEmailId())
                .setExpiration(expiryDate)
                .compact();
        return token;
    }
}
