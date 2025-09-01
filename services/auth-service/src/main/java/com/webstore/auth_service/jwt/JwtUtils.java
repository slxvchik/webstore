package com.webstore.auth_service.jwt;

import com.webstore.auth_service.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.publicKey}")
    private String PUBLIC_KEY;
    @Value("${jwt.privateKey}")
    private String PRIVATE_KEY;
    @Value("${jwt.accessExpiration}")
    private int ACCESS_EXPIRATION;
    @Value("${jwt.refreshExpiration}")
    @Getter
    private int REFRESH_EXPIRATION;


    public String createAccessToken(User user) {

        Set<String> userRoles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getEmail())
                .claim("authorities", userRoles)
                .claim("userId", user.getId())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(ACCESS_EXPIRATION)))
                .signWith(getPrivateKey())
                .compact();
    }

    public String createRefreshToken(User user) {

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(REFRESH_EXPIRATION)))
                .signWith(getPrivateKey())
                .compact();
    }

    public Optional<ParsedToken> extractAllClaims(String token) {
        try {
            Claims parsedTokenClaims = Jwts.parser()
                    .verifyWith(getPublicKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return Optional.of(
                    new ParsedToken(parsedTokenClaims)
            );
        } catch (Exception e) {
            log.error("JWT extract claims error -> Message: ", e);
        }
        return Optional.empty();
    }

    private PrivateKey getPrivateKey() {
        try {
            String privateKeyStr = PRIVATE_KEY.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replace("-----END RSA PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] keyBytes = Decoders.BASE64.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(keySpec);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("RSA algorithm not available", e);
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException("Invalid private key specification", e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load private key", e);
        }
    }

    private PublicKey getPublicKey() {
        try {
            String publicKeyStr = PUBLIC_KEY.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] keyBytes = Decoders.BASE64.decode(publicKeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("RSA algorithm not available", e);
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException("Invalid public key specification", e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load public key", e);
        }
    }
}
