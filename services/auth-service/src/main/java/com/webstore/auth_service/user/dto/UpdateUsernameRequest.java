package com.webstore.auth_service.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUsernameRequest(
        @NotBlank(message = "Username must not be blank")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{4,32}$", message = "The username can only contain letters and underscores")
        String newUsername,
        String password,
        String passwordConfirmation
) {
}
