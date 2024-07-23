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
import static com.universityproject.backendproject.constant.JwtAuthenticationConstant.TOKEN_BEGINNING;

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
        final String jwt;
        final Long userId;

        if (authHeader == null || !authHeader.startsWith(TOKEN_BEGINNING)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(TOKEN_BEGINNING.length());

        try {
            userId = this.jwtService.extractUserId1(jwt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = this.userService.findUserById(userId);

            try {
                if (this.jwtService.isTokenValid(jwt, user)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
