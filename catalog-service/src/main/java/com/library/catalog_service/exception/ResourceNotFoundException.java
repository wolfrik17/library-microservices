package com.library.catalog_service.exception; // Change 'user_service' to match the specific microservice

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}