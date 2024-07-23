package com.universityproject.backendproject.constant;

public class JwtAuthenticationConstant {

    public static final String HEADER_NAME = "Authorization";

    public static final String TOKEN_BEGINNING = "Bearer ";

    public static final String SECRET_KEY_PATH = "RSA";

    public static final String SECRET_KEY_FORMAT = "RSA";

    public static final String PRIVATE_SECRET_KEY = "/privateKey";

    public static final Integer EXPIRATION_TIME = 1000 * 60 * 60 * 24;
}
