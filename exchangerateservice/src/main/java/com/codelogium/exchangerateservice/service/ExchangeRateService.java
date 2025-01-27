package com.codelogium.exchangerateservice.service;

import org.springframework.http.ResponseEntity;

import com.codelogium.exchangerateservice.mapper.CryptoResponseMapper;

import reactor.core.publisher.Mono;

public interface ExchangeRateService {
    Mono<CryptoResponseMapper> retrivePrice(String symbol, String baseCurrency);
    Mono<ResponseEntity<String>> getAllData();
}
