package com.glamreserve.glamreserve.entities.interceptor;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
