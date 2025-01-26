package com.codelogium.exchangerateservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
//Explicitly loading the properties to the context 
@PropertySource("classpath:application.properties") 
public class AppConfig {

    @Value("$external-api.base-url")
    private String baseUrl;
   
    // Defines a bean to create ad configure a WebClient instance
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                    .baseUrl(baseUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();
    }    
}
