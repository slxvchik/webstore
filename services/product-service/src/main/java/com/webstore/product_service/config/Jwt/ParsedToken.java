package com.webstore.product_service.config.Jwt;

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

    public String getUsername() {
        return claims.getSubject();
    }

    public Long getUserId() {
        return Long.valueOf(String.valueOf(claims.get("userId"))); // todo: beautify
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        List<String> authorities = claims.get("authorities", List.class);
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
