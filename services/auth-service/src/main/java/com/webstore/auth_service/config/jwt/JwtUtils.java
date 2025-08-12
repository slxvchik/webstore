package com.webstore.auth_service.config.jwt;

import com.webstore.auth_service.user.Role;
import com.webstore.auth_service.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.accessExpiration}")
    private int ACCESS_EXPIRATION;
    @Value("${jwt.refreshExpiration}")
    @Getter
    private int REFRESH_EXPIRATION;


    public String createAccessToken(User user) {

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getUsername())
                .claim("authorities", user.getAuthorities())
                .claim("userId", user.getId())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(ACCESS_EXPIRATION)))
                .signWith(getSigningKey())
                .compact();
    }

    public String createRefreshToken(User user, String csrfToken) {

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("X-CSRF-TOKEN", csrfToken)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(REFRESH_EXPIRATION)))
                .signWith(getSigningKey())
                .compact();
    }

    public Optional<Claims> extractAllClaims(String token) {
        try {
            return Optional.ofNullable(Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload());
        } catch (Exception e) {
            log.error("JWT extract claims error -> Message: ", e);
        }
        return Optional.empty();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
