package com.webstore.user_service.user.dto;

public record UserResponse(
    Long id,
    String username,
    String fullname,
    String email,
    String phone
) {
}
