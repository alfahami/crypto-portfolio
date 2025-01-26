package com.codelogium.exchangerateservice.service;

import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Mono;

public interface ExchangeRateService {
    Mono<ResponseEntity<String>> getAllData();
}
