package com.codelogium.exchangerateservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Autowired
    private WebClient webClient;

    @Override
    public Mono<ResponseEntity<String>> getAllData() {

        Mono<ResponseEntity<String>> result = webClient.get()
                .uri("/v1/cryptocurrency/listings/latest")
                .retrieve().toEntity(String.class);

        return result;
    }
}
