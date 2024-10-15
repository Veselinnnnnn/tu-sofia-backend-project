package com.universityproject.backendproject.exception;

public class AdminUserNotFoundException extends RuntimeException {
    public AdminUserNotFoundException(String message) {
        super(message);
    }
}