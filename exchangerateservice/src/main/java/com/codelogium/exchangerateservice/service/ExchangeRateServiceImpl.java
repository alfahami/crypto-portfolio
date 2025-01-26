package com.codelogium.exchangerateservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.codelogium.exchangerateservice.exception.ExternalApiException;
import com.codelogium.exchangerateservice.mapper.CryptoResponseMapper;

import reactor.core.publisher.Mono;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Autowired
    private WebClient webClient;

    @Override
    public Mono<CryptoResponseMapper> retrivePrice() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Mono<ResponseEntity<String>> getAllData() {
        // Asynchronously fetches cryptocurrency listings using WebClient, with error handling and reactive response processing
        Mono<ResponseEntity<String>> result = webClient.get() 
                .uri("/v1/cryptocurrency/listings/latest")
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono.error(new ExternalApiException(
                                "Failed to retrieve latest crypto listing, please try again!")))
                .toEntity(String.class);

        return result;
    }
}
