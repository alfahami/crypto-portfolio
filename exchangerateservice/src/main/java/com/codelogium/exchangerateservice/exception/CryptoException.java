
package com.codelogium.exchangerateservice.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;

import lombok.Getter;

@Getter
public class CryptoException extends RuntimeException {

    private final HttpStatusCode status;

    public CryptoException(HttpStatusCode status, String message) {
        super(message);
        this.status = status;
    }

    // Factory method of creating an instance of this from a clientResponse
    public static CryptoException from(ClientResponse response) {
        return new CryptoException(response.statusCode(), response.toEntity(String.class).toString());
    }
}
