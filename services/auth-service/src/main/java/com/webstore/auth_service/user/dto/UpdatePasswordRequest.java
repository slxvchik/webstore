package com.webstore.auth_service.user.dto;

public record UpdatePasswordRequest(
        String oldPassword,
        String newPassword,
        String confirmNewPassword
) {
}
