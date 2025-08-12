package com.webstore.order_service.config.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ParsedToken {

    private final Claims claims;

    public ParsedToken(Claims claims) {
        this.claims = claims;
    }

    public String getUsername() {
        return claims.getSubject();
    }

    public Long getUserId() {
        return (Long) claims.get("userId");
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {

        List<String> userRolesList = claims.get("authorities", List.class);

        return userRolesList
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

}
