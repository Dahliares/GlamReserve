package com.glamreserve.glamreserve.entities.interceptor;

public class ForbiddenAccessException extends RuntimeException {
    public ForbiddenAccessException(String message) {
        super(message);
    }
}