package com.webstore.order_service.config.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ParsedToken {

    private final Claims claims;

    public ParsedToken(Claims claims) {
        this.claims = claims;
    }

    public String getEmail() {
        return claims.getSubject();
    }

    public String getUserId() {
        return claims.get("userId", String.class);
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {

        List<String> userRolesList = claims.get("authorities", List.class);

        return userRolesList
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

}
