package com.webstore.auth_service.config.jwt;

import com.webstore.auth_service.user.Role;
import com.webstore.auth_service.user.User;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ParsedToken {
    private final Claims claims;

    public ParsedToken(Claims claims) {
        this.claims = claims;
    }

    public String getCsrfToken() {
        return claims.get("X-CSRF-TOKEN", String.class);
    }

    public String getUsername() {
        return claims.getSubject();
    }

    public User getUser() {

        List<String> userRolesList = claims.get("authorities", ArrayList.class);
        Set<Role> userRoles = userRolesList
                .stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());

        return User.builder()
                .id(claims.get("userId", Long.class))
                .username(claims.get("username", String.class))
                .roles(userRoles)
                .build();
    }
}
