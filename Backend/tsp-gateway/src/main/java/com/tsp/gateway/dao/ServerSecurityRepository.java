package com.tsp.gateway.dao;

import com.tsp.gateway.exceptions.InvalidTokenException;
import com.tsp.gateway.util.TokenUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ServerSecurityRepository implements ServerSecurityContextRepository {

    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private UserDetailsService userService;

    @Value("${jwt.header.string}")
    public String HEADER_STRING;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        throw new UnsupportedOperationException("save not implemented yet");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request=exchange.getRequest();
        String token = request.getHeaders().getFirst(HEADER_STRING);
        try {
            String uri=request.getURI().getPath();
            log.debug("request parameter=", request.getQueryParams().get("username"), " uri="+uri+" method=",request.getMethod());
            if( uri.startsWith("/userDetails/login"))
            {
                return Mono.empty();
            }
            String username = tokenUtil.isValid(token);
            log.debug("username::"+ username);
            UserDetails user = userService.loadUserByUsername(username);
            log.debug("user details fetched from token::"+user);
            UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(username, user.getPassword(), user.getAuthorities());
            return Mono.just(newAuthentication).map(SecurityContextImpl::new);
        } catch (UsernameNotFoundException | InvalidTokenException | JwtException e) {
            ServerHttpResponse response=exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            log.error("Exception in processing token::", e.getMessage());
            return Mono.empty();
        }
    }
}
