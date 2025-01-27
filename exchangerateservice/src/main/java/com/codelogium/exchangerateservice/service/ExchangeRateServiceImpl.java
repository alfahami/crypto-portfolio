package com.codelogium.exchangerateservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.codelogium.exchangerateservice.exception.ExternalApiException;
import com.codelogium.exchangerateservice.mapper.CryptoResponseMapper;
import com.fasterxml.jackson.databind.JsonNode;

import reactor.core.publisher.Mono;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Autowired
    private WebClient webClient;

    /**
     * Fetches cryptocurrency price data using WebClient, processes the raw JSON response, and maps it to a CryptoPrice object containing the symbol, base currency, and price.
     * Handles errors during the request and data mapping gracefully.
     * 
     * @param symbol : The symbol of the cryptocurrency (e.g., BTC, ETH).
     * @param baseCurrency: The base currency for conversion (e.g., USD, EUR).
     * @return A Mono emitting a CryptoPrice object with the requeste details.
     */
    @Override
    public Mono<CryptoResponseMapper> retrivePrice(String symbol, String base) {
        return webClient.get().uri(
                uriBuilder -> uriBuilder.path("/v1/cryptocurrency/quotes/latest")
                        .queryParam("symbol", symbol)
                        .queryParam("convert", base)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnError(error -> new ExternalApiException(
                        "Error while fetching data." + error.getMessage() + "\nPlease try again")) // doOnError for general error handling accross the pipeline
                .map(response -> {
                    JsonNode quote = response.path("data").path(symbol).path("quote").path(base);
                    return new CryptoResponseMapper(
                            symbol,
                            base,
                            quote.path("price").decimalValue());
                });
    }

    @Override
    public Mono<ResponseEntity<String>> getAllData() {
        /*
        * Asynchronously fetches cryptocurrency listings using WebClient, with error handling and reactive response processing
        */
        Mono<ResponseEntity<String>> result = webClient.get()
                .uri("/v1/cryptocurrency/listings/latest")
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono.error(new ExternalApiException(
                                "Failed to retrieve latest crypto listing, please try again!"))) //intercept and handle any http status code
                .toEntity(String.class);

        return result;
    }
}
