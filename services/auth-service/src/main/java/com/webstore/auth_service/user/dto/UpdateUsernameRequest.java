package com.webstore.auth_service.user.dto;

public record UpdateUsernameRequest(
        String newUsername,
        String password,
        String passwordConfirmation
) {
}
