package com.codelogium.exception;

public class EntityNotFoundException extends RuntimeException {
    
    public EntityNotFoundException(Long id, Class<?> entityClass) {
        super("The " + entityClass.getSimpleName().toLowerCase() + " with the id " + id + " is not found in our records");
    }
}
