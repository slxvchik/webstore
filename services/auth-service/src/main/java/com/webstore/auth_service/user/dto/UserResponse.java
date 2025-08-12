package com.webstore.auth_service.user.dto;

public record UserResponse(
    Long id,
    String username,
    String fullname,
    String email,
    String phone
) {
}
