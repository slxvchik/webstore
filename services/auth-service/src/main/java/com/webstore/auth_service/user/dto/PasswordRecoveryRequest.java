package com.webstore.auth_service.user.dto;

public record PasswordRecoveryRequest(
    String newPassword,
    String newPasswordConfirmation
) {
}
