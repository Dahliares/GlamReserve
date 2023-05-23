package com.glamreserve.glamreserve.entities.interceptor;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
