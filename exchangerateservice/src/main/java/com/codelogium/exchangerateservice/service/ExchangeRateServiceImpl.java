package com.codelogium.exchangerateservice.service;

import java.math.BigDecimal;
import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.codelogium.exchangerateservice.exception.CryptoException;
import com.codelogium.exchangerateservice.mapper.CryptoResponseMapper;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final WebClient webClient;

    /**
     * Fetches cryptocurrency price data using WebClient, processes the raw JSON
     * response, and maps it to a CryptoPrice object containing the symbol, base
     * currency, and price.
     * Handles errors during the request and data mapping gracefully using
     * Mono.just() to propagate them in the reactive pipeline.
     * 
     * @param symbol        : symbol of the cryptocurrency (e.g., BTC, ETH).
     * @param baseCurrency: base currency for conversion (e.g., USD, EUR).
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
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> CryptoException.handleErrorResponse(clientResponse, symbol, base))
                .bodyToMono(JsonNode.class)
                .map(response -> {
                    // Check if the symbol exists in the response
                    JsonNode symbolNode = response.path("data").path(symbol);
                    if (symbolNode.isMissingNode()) {
                        // If the symbol is not found, throw an error indicating it's invalid
                        throw new CryptoException(HttpStatus.BAD_REQUEST, "Invalid symbol: " + symbol);
                    }

                    JsonNode quote = symbolNode.path("quote").path(base);

                    BigDecimal price = quote.has("price") ? quote.get("price").decimalValue() : BigDecimal.ZERO;

                    // Explicitly handle base error as CMC may give a 0 price value if the base currency is not supported for the symbol
                    if (price.compareTo(BigDecimal.ZERO) == 0) {
                        throw new CryptoException(HttpStatus.BAD_REQUEST,
                                "Base currency " + base + " is not supported for symbol " + symbol);
                    }

                    return new CryptoResponseMapper(symbol, base, price);
                });
    }

    /*
     * Asynchronously fetches cryptocurrency listings using WebClient, with error
     * handling and reactive response processing
     */
    @Override
    public Mono<ResponseEntity<String>> getAllData() {
        Mono<ResponseEntity<String>> result = webClient.get()
                .uri("/v1/cryptocurrency/listings/latest")
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        clientResponse -> CryptoException.handleErrorResponse(clientResponse, null, null))
                .toEntity(String.class)
                .timeout(Duration.ofMillis(10000));
        return result;
    }
}
