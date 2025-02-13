package com.codelogium.exchangerateservice;

import java.util.Arrays;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebExchange;

import com.codelogium.exchangerateservice.exception.CryptoException;
import com.codelogium.exchangerateservice.exception.ErrorResponse;

import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CryptoException.class)
    public ResponseEntity<Object> handleCryptoException(CryptoException ex) {
        ErrorResponse error = new ErrorResponse(Arrays.asList(ex.getLocalizedMessage()));
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @SuppressWarnings("null")
    @Override
    protected Mono<ResponseEntity<Object>> handleMissingRequestValueException(@NonNull MissingRequestValueException ex,
            @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull ServerWebExchange exchange) {

        
        ErrorResponse error = new ErrorResponse(Arrays.asList(ex.getReason()));

        return Mono.just(ResponseEntity.badRequest().body(error));
    }
}
