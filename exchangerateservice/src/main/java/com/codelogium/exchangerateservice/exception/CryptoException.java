
package com.codelogium.exchangerateservice.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import reactor.core.publisher.Mono;

@Getter
public class CryptoException extends RuntimeException {

    private final HttpStatusCode status;

    public CryptoException(HttpStatusCode status, String message) {
        super(message);
        this.status = status;
    }

    // Factory method of creating an instance of this from a clientResponse
    public static Mono<CryptoException> from(ClientResponse response) {
        return response.bodyToMono(JsonNode.class)
                   .map(errorBody -> new CryptoException(response.statusCode(), "API Error: " + errorBody));
    }

    public static boolean isClientError(Throwable throwable) {
        return throwable instanceof CryptoException && 
        ((CryptoException) throwable).isClientError();
    }

    public static boolean is4xxServerError(Throwable throwable) {
        return throwable instanceof CryptoException &&
        ((CryptoException) throwable).isServerError();
    }

    public boolean isClientError() {
        return status.is4xxClientError();
    }

    public boolean isServerError() {
        return status.is5xxServerError();
    }
}
