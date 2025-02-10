package com.codelogium.portfolioservice.exception;

public class OwnershipException extends RuntimeException {
    
    public OwnershipException(Class<?> entityClass) {
        super("No onwership of " + entityClass.getSimpleName().toLowerCase());
    }
}
