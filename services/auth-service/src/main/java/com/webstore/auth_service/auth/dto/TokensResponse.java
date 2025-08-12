package com.webstore.auth_service.auth.dto;

public record TokensResponse(
        String accessToken,
        String refreshToken
) {
}
