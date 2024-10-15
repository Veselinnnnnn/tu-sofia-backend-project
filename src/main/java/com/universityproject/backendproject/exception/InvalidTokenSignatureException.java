package com.universityproject.backendproject.exception;

public class InvalidTokenSignatureException extends RuntimeException {
    public InvalidTokenSignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}