package com.webstore.auth_service.user.dto;

import com.webstore.auth_service.user.Role;

import java.util.Set;

public record UserResponse(
    Long id,
    String username,
    String fullname,
    String email,
    String phone,
    String avatar,
    Set<Role> roles
) {
}
