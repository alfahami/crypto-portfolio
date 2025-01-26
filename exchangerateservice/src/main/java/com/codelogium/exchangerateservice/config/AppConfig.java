package com.codelogium.exchangerateservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${external-api.base-url}")
    private String baseUrl;
    
    @Value("${external-api.key}")
    private String apiKey;
   
    // Defines a bean to create ad configure a WebClient instance
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                    .baseUrl(baseUrl)
                    .defaultHeader("X-CMC_PRO_API_KEY", apiKey)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();
    }    
}
