package com.example.carsharing.exception;

public class NotEnoughCarInventoryException extends RuntimeException {
    public NotEnoughCarInventoryException(String message) {
        super(message);
    }
}
