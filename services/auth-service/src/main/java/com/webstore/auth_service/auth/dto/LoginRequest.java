package com.webstore.auth_service.auth.dto;

import jakarta.validation.constraints.*;

public record LoginRequest(
        @NotBlank
        @Size(max = 50)
        @Email(message = "Input must be in Email format")
        String email,

        @NotNull(message = "Password must not be null")
        @Size(min = 6)
        String password
) {
}
