
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

    public static Mono<Throwable> handleErrorResponse(ClientResponse clientResponse, String base) {
        // Extract the body of the error response as a Json
        return clientResponse.bodyToMono(JsonNode.class)
                .flatMap(errorBody -> {

                    // Extract error details dynamically
                    JsonNode statusNode = errorBody.path("status");
                    String errorMessage = statusNode.path("error_message").asText();
                    //Check if invalid base
                    if (errorMessage.toLowerCase().contains("convert")) {
                        return Mono.error(new CryptoException(
                                clientResponse.statusCode(),
                                "Invalid Base Currency: " + base + " is not supported."));
                    }
                    // Return a Mono error with a custom exception that contains the error message
                    return Mono.error(new CryptoException(clientResponse.statusCode(), errorMessage));
                });
    }
}
