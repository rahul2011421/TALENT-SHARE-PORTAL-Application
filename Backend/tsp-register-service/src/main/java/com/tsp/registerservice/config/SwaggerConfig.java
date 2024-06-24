package com.tsp.registerservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Talent Share Portal")
                        .description("Mentoring serves as an investment " +
                                "in the growth of employees, providing " +
                                "opportunities for them to deepen their " +
                                "understanding of their roles and technology.")
                        .version("1.0")
                        .contact(new Contact()
                                .email("help@maveric-systems.com"))
                        .license(new License().name("Maveric"))
                )
                .externalDocs(new ExternalDocumentation()
                        .url("https://maveric-systems.com/"));
    }
}
