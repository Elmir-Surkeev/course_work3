package com.example.carsharing.exception;

public class NoSuchSessionException extends RuntimeException {
    public NoSuchSessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
