
package com.codelogium.exchangerateservice.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import reactor.core.publisher.Mono;

@Getter
public class CryptoException extends RuntimeException {

    private final HttpStatusCode status;
    private String message;

    public CryptoException(HttpStatusCode status, String message) {
        this.message = message;
        this.status = status;
    }

    public static Mono<Throwable> handleErrorResponse(ClientResponse clientResponse, String symbol, String base) {
        // Check for 4xx Client Error
        if (clientResponse.statusCode().is4xxClientError()) {
            return clientResponse.bodyToMono(JsonNode.class)
                    .flatMap(errorBody -> {
                        // Extract error details dynamically
                        String errorMessage = errorBody.path("status").path("error_message").asText();
                        // Check if invalid base
                        if (errorBody.path("data").isEmpty() && errorMessage.toLowerCase().contains("invalid value for \"convert\"")) {
                            return Mono.error(new CryptoException(
                                    clientResponse.statusCode(),
                                    "Invalid base currency: " + base + " is not supported."));
                        }
                        // Return a Mono error with a custom exception that contains the error message
                        return Mono.error(new CryptoException(clientResponse.statusCode(),
                                errorMessage));
                    });
        }
        // Handle 5xx Internal Server Error
        else if (clientResponse.statusCode().is5xxServerError()) {
            return Mono.error(new CryptoException(clientResponse.statusCode(),
                    "Error while fetching data, please try again later."));
        }

        // For non-4xx and non-5xx errors, return Mono.empty to indicate no custom error handling is needed
        return Mono.empty();
    }
}
