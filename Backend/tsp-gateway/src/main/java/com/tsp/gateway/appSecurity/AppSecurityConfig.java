package com.tsp.gateway.appSecurity;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@EnableWebFluxSecurity
@Configuration
@Slf4j
public class AppSecurityConfig {
    @Autowired
    private ServerSecurityContextRepository securityContextRepository;

    @Bean
    protected SecurityWebFilterChain configure(ServerHttpSecurity http) throws Exception {
        return http
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrf -> csrf.disable())
                .cors((cors)->corsConfiguration())
                .formLogin(form -> form.disable())
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/userDetails/login","/userDetails/forgotPassword/**").permitAll()
                        .pathMatchers("/**").authenticated()
                )
                .securityContextRepository(securityContextRepository)
                .build();
    }

     @Bean
    public CorsConfigurationSource corsConfiguration() {
        UrlBasedCorsConfigurationSource configuration = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://192.168.97.43:3002");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        configuration.registerCorsConfiguration("/**", corsConfiguration);
        return configuration;
    }
}
