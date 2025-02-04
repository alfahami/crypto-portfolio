package com.codelogium.exchangerateservice;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
