package com.codelogium.exchangerateservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.codelogium.exchangerateservice.exception.CryptoException;
import com.codelogium.exchangerateservice.exception.ErrorResponse;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(CryptoException.class)
    public ResponseEntity<Object> handleCryptoException(CryptoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(error, ex.getStatus());   
    }
}
