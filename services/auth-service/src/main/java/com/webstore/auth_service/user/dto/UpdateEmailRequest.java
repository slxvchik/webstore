package com.webstore.auth_service.user.dto;

public record UpdateEmailRequest(
        String newEmail,
        String password,
        String passwordConfirmation
) {
}
