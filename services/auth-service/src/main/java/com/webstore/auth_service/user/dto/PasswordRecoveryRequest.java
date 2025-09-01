package com.webstore.auth_service.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PasswordRecoveryRequest(
    @NotNull(message = "New password must not be null")
    @Size(min = 6)
    String newPassword,
    @NotNull(message = "Password confirmation must not be null")
    @Size(min = 6)
    String newPasswordConfirmation
) {
}
