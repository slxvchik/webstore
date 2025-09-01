package com.webstore.auth_service.auth.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(

        @NotNull(message = "Password must not be null")
        @Size(min = 6)
        String password,

        @NotBlank(message = "First name must not be blank")
        @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
        String firstName,

        @NotBlank(message = "First name must not be blank")
        @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
        String secondName,

        @NotBlank
        @Size(max = 50)
        @Email(message = "Input must be in Email format")
        String email,

        @Pattern(regexp = "^\\+7[0-9]{10}$", message = "The phone number is not in the correct format")
        @Size(min = 12, max = 12, message = "Phone number must be 12 characters")
        String phone
) {
}
