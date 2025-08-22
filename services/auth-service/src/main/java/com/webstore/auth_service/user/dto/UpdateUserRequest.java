package com.webstore.auth_service.user.dto;

public record UpdateUserRequest(
    String fullname,
    String avatar
) {
}
