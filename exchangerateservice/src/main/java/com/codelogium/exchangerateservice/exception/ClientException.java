
package com.codelogium.exchangerateservice.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;

import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {

    private final HttpStatusCode status;

    public ClientException(HttpStatusCode status, String message) {
        super(message);
        this.status = status;
    }

    public static ClientException from(ClientResponse response) {
        return new ClientException(response.statusCode(), response.toEntity(String.class).toString());
    }
}
