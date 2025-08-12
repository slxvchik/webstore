package com.webstore.auth_service.security;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

public class CsrfTokenGenerator {
    private static final int TOKEN_LENGTH = 32;
    public static String generateCsrfToken() {
        SecureRandom random = new SecureRandom();
        byte[] tokenBytes = new byte[TOKEN_LENGTH];
        random.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
}
