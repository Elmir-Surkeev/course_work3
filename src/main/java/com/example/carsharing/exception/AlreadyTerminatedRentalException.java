package com.example.carsharing.exception;

public class AlreadyTerminatedRentalException extends RuntimeException {
    public AlreadyTerminatedRentalException() {
    }

    public AlreadyTerminatedRentalException(String message) {
        super(message);
    }

    public AlreadyTerminatedRentalException(String message, Throwable cause) {
        super(message, cause);
    }
}
