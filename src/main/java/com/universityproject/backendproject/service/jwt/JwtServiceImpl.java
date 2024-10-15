package com.universityproject.backendproject.service.jwt;

import com.universityproject.backendproject.exception.InvalidTokenSignatureException;
import com.universityproject.backendproject.exception.TokenValidationException;
import com.universityproject.backendproject.exception.UserNotFoundException;
import com.universityproject.backendproject.model.entity.User;
import com.universityproject.backendproject.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.universityproject.backendproject.constant.JwtAuthenticationConstant.*;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public User validateTokenAndGetUser(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = Long.parseLong(claims.getSubject());
            return userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
        } catch (SignatureException e) {
            log.error("Invalid token signature", e);
            throw new InvalidTokenSignatureException("Invalid token signature", e);
        } catch (Exception e) {
            log.error("Token validation failed", e);
            throw new TokenValidationException("Token validation failed", e);
        }
    }

    @Override
    public String extractUsername(String token) throws Exception {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Long extractUserId1(String token) throws Exception {
        return 0L;
    }

    @Override
    public Long extractUserId(String token) throws Exception {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) throws Exception {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    @Override
    public String generateToken(User user) throws Exception {
        return generateToken(new HashMap<>(), user);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, User user) throws Exception {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLISECONDS))
                .signWith(getSignInKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws Exception {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) throws Exception {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) throws Exception {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) throws Exception {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private PrivateKey getSignInKey() throws Exception {
        var keySpec = new PKCS8EncodedKeySpec(decodeKey(SECRET_KEY_PATH.concat(PRIVATE_SECRET_KEY_PATH)));
        return KeyFactory.getInstance(SECRET_KEY_FORMAT).generatePrivate(keySpec);
    }

    private byte[] decodeKey(final String path) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + path);
            }
            byte[] keyBytes = inputStream.readAllBytes();
            return Base64.getDecoder().decode(keyBytes);
        }
    }
}
