package com.webstore.auth_service.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
    @Size(min = 2, max = 64)
    String firstName,
    @Size(min = 2, max = 64)
    String secondName
) {
}
