package com.codelogium.portfolioservice;

import java.util.Arrays;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.server.ServerWebExchange;

import com.codelogium.portfolioservice.exception.ResourceNotFoundException;

import reactor.core.publisher.Mono;

import com.codelogium.portfolioservice.exception.ErrorResponse;

@RestControllerAdvice
public class ApplicationExceptionHanlder extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {

        ErrorResponse error = new ErrorResponse(Arrays.asList(ex.getMessage()));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Override
    protected Mono<ResponseEntity<Object>> handleMissingRequestValueException(MissingRequestValueException ex,
            HttpHeaders headers, HttpStatusCode status, ServerWebExchange exchange) {
        ErrorResponse error = new ErrorResponse(Arrays.asList(ex.getReason()));

        return Mono.just(ResponseEntity.badRequest().body(error));
    }
    
}
