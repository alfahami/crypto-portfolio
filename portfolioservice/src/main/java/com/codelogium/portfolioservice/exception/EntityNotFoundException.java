package com.codelogium.portfolioservice.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
    public EntityNotFoundException(Long id, Class<?> entityClass) {
        super("The " + entityClass.getSimpleName().toLowerCase() + " with the id " + id + " is not found.");
    }
    
}
