package com.webstore.auth_service.jwt;

import com.webstore.auth_service.user.Role;
import com.webstore.auth_service.user.User;
import io.jsonwebtoken.Claims;

import java.util.*;
import java.util.stream.Collectors;

public class ParsedToken {
    private final Claims claims;

    public ParsedToken(Claims claims) {
        this.claims = claims;
    }

    public String getUserId() {
        return claims.get("userId", String.class);
    }

    public String getTokenId() {
        return claims.getId();
    }

    public User getUser() {

        List<String> userRolesList = claims.get("authorities", ArrayList.class);
        Set<Role> userRoles = userRolesList
                .stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());

        return User.builder()
                .id(claims.get("userId", String.class))
                .roles(userRoles)
                .build();
    }

    public Date getExpiration() {
        return claims.getExpiration();
    }
}
