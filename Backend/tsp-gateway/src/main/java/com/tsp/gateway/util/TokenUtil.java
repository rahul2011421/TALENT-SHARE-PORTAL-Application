package com.tsp.gateway.util;

import com.tsp.gateway.exceptions.InvalidTokenException;
import io.jsonwebtoken.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class TokenUtil {
    @Value("${tsp.KEY}")
    private String KEY;

    @Value("${jwt.token.prefix}")
    public String TOKEN_PREFIX;

    public String isValid(String token) {
        try {
            Jws<Claims> claimsHolder = Jwts.parser()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(getActualToken(token));
            log.debug("claimsHolder " + claimsHolder);
            Claims claims = claimsHolder.getBody();
            log.debug("claims " + claims);
            String username = claims.getSubject();
            log.debug("username " + username);
            return username;
        } catch (ExpiredJwtException e) {
            log.error("Token expired");
            throw new InvalidTokenException("Token expired", e);
        }
    }

    private String getActualToken(String token){
        if (StringUtils.isBlank(token) || !token.startsWith(TOKEN_PREFIX)) {
            log.info("Invalid token");
            throw new InvalidTokenException("Invalid token");
        }
        return token.trim().replace(TOKEN_PREFIX, "").trim();
    }
}
