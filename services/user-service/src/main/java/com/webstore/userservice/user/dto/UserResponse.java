package com.webstore.userservice.user.dto;

public record UserResponse(
    Long id,
    String username,
    String fullname,
    String email,
    String phone
) {
}
