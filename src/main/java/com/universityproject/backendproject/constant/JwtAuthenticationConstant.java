package com.universityproject.backendproject.constant;

public final class JwtAuthenticationConstant {

    public static final String HEADER_NAME = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String SECRET_KEY_PATH = "RSA";
    public static final String SECRET_KEY_FORMAT = "RSA";
    public static final String PRIVATE_SECRET_KEY_PATH = "/privateKey";

    public static final int EXPIRATION_TIME_MILLISECONDS = 1000 * 60 * 60 * 24;

    private JwtAuthenticationConstant() {
        throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
    }
}
