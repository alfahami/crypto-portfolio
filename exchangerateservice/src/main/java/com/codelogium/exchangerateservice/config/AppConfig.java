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

    /*
     * As we will configure Spring context manually (e.g., using AnnotationConfigApplicationContext), we need to declare WebClient.Builder bean to avoid Spring may not have enabled the automatic configuration for WebClient.Builder.
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
    // This bean is necessary to resolve placeholders in @Value annotations
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                    .baseUrl(baseUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public String getBaseUrl() { return this.baseUrl; }
    
}
