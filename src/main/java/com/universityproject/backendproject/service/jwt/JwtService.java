package com.universityproject.backendproject.service.jwt;

import com.universityproject.backendproject.model.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String extractUsername(String token) throws Exception;

    Long extractUserId1(String token) throws Exception;

    Long extractUserId(String token) throws Exception;

    boolean isTokenValid(String token, UserDetails userDetails) throws Exception;

    String generateToken(User user) throws Exception;


    String generateToken(Map<String, Object> extraClaims, User user) throws Exception;

    User validateTokenAndGetUser(String token) throws Exception;
}
