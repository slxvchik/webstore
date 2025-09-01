package com.webstore.auth_service.user.dto;

import com.webstore.auth_service.user.Role;

import java.util.Set;

public record UserResponse(
    String id,
    String firstName,
    String secondName,
    String email,
    String phone,
    Set<Role> roles
) {
}
