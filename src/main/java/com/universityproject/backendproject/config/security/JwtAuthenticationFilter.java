package com.universityproject.backendproject.config.security;

import com.universityproject.backendproject.model.entity.User;
import com.universityproject.backendproject.service.jwt.JwtService;
import com.universityproject.backendproject.service.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.universityproject.backendproject.constant.JwtAuthenticationConstant.HEADER_NAME;
import static com.universityproject.backendproject.constant.JwtAuthenticationConstant.TOKEN_PREFIX;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(HEADER_NAME);

        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = extractJwtFromHeader(authHeader);
        Long userId = extractUserIdFromJwt(jwt);
        System.out.println("Error!");
        System.out.println(userId);
        if (userId != null && isAuthenticationAbsent()) {
            authenticateUser(request, jwt, userId);
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromHeader(String authHeader) {
        return authHeader.substring(TOKEN_PREFIX.length());
    }

    private Long extractUserIdFromJwt(String jwt) {
        try {
            return jwtService.extractUserId1(jwt);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract user ID from JWT", e);
        }
    }

    private boolean isAuthenticationAbsent() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void authenticateUser(HttpServletRequest request, String jwt, Long userId) {
        try {
            User user = userService.findUserById(userId);
            if (jwtService.isTokenValid(jwt, user)) {
                setAuthentication(user, request);
            }
        } catch (Exception e) {
            throw new RuntimeException("User authentication failed", e);
        }
    }

    private void setAuthentication(User user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
