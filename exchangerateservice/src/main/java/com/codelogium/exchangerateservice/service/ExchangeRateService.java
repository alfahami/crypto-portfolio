package com.codelogium.exchangerateservice.service;

import reactor.core.publisher.Mono;

public interface ExchangeRateService {
    Mono<String> getAllData();
}
