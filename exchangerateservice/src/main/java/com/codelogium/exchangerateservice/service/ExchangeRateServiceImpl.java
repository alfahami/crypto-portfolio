package com.codelogium.exchangerateservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private WebClient webClient;

    @Override
    public Mono<ResponseEntity<String>> getAllData() {

        Mono<ResponseEntity<String>> result = webClient.get()
                .uri("/v1/cryptocurrency/listings/latest")
                .retrieve().toEntity(String.class);

        return result;
    }
}
