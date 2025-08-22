package com.webstore.product_service.config.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Optional;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public Optional<ParsedToken> extractAllClaims(String token) {
        try {
            Claims tokenClaims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return Optional.of(
                    new ParsedToken(tokenClaims)
            );
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
