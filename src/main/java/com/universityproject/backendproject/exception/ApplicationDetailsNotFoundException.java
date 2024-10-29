package com.universityproject.backendproject.exception;

public class ApplicationDetailsNotFoundException extends RuntimeException {
    public ApplicationDetailsNotFoundException(String message) {
        super(message);
    }
}