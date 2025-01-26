package com.codelogium.exchangerateservice.exception;

public class ExternalApiException extends RuntimeException {

    public ExternalApiException(String data) {
        super(data);
    }
}
