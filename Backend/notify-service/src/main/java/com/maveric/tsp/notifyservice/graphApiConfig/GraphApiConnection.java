package com.maveric.tsp.notifyservice.graphApiConfig;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.requests.GraphServiceClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Configuration
@Data
public class GraphApiConnection {
    private static ClientSecretCredential clientSecretCredential;
    private static GraphServiceClient<Request> appClient;

    @Value("${spring.security.oauth2.client.registration.azure.client-id}")
    private String clientId;

    @Value("${microsoft.azure.tenant-id}")
    private String tenantId;

    @Value("${spring.security.oauth2.client.registration.azure.client-secret}")
    private String clientSecret;

    @Value("${spring.mail.username}")
    private String sender;


    @Bean
    public GraphServiceClient<Request> intializeGraphForAppOnlyAuth() {
        log.info("initializing graph for user auth");

        clientSecretCredential=new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .tenantId(tenantId)
                .clientSecret(clientSecret)
                .build();
        if (appClient==null){
            //Use the .default scope when using app-only auth
            final TokenCredentialAuthProvider authProvider=new TokenCredentialAuthProvider(
                    List.of("https://graph.microsoft.com/.default"),clientSecretCredential);

            appClient=GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();

        }
        return appClient;
    }

}
