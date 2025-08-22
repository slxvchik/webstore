package com.webstore.auth_service.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Username must not be blank")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{4,32}$", message = "The username can only contain letters and underscores")
        String username,

        @NotNull(message = "Password must not be null")
        @Size(min = 6)
        String password
) {
}
