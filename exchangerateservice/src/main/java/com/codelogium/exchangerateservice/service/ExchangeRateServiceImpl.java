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
                .onStatus( 
                        HttpStatusCode::isError, 
                        clientResponse -> 
                                clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                        //Wrap the WebClient error into ClientException
                                        HttpStatusCode statusCode = clientResponse.statusCode();
                                        return Mono.error(new CryptoException(statusCode, errorBody));
                                }) //transform the HTTP error response into a custom exception (ClientException).
                        )
                .bodyToMono(JsonNode.class)
                .map(response -> {
                    JsonNode quote = response.path("data").path(symbol).path("quote").path(base);
                    BigDecimal price = quote.has("price") ? quote.get("price").decimalValue() : BigDecimal.ZERO;
                    if(price.compareTo(BigDecimal.ZERO) == 0) throw new CryptoException(HttpStatus.BAD_REQUEST, "Invalid Symbol: " + symbol);

                    return new CryptoResponseMapper(
                            symbol,
                            base,
                            price);
                });
    }

        @Override
        public Mono<ResponseEntity<String>> getAllData() {
                /*
                 * Asynchronously fetches cryptocurrency listings using WebClient, with error
                 * handling and reactive response processing
                 */
                Mono<ResponseEntity<String>> result = webClient.get()
                                .uri("/v1/cryptocurrency/listings/latest")
                                .retrieve()
                                .onStatus( 
                        HttpStatusCode::isError, 
                        clientResponse -> 
                                clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                        //Wrap the WebClient error into ClientException
                                        HttpStatusCode statusCode = clientResponse.statusCode();
                                        return Mono.error(new CryptoException(statusCode, errorBody));
                                }) //transform the HTTP error response into a custom exception (ClientException).
                        )
                        .toEntity(String.class)
                        .timeout(Duration.ofMillis(10000));

                return result;
        }
}
