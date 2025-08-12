package com.webstore.auth_service.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
@Getter
public enum Role implements GrantedAuthority {
    ADMIN,
    PRODUCT_MANAGER,
    USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
