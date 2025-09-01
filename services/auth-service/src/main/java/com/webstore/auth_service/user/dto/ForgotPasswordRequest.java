package com.webstore.auth_service.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ForgotPasswordRequest(
        @NotBlank
        @Size(max = 50)
        @Email(message = "Input must be in Email format")
        String email
) {
}
