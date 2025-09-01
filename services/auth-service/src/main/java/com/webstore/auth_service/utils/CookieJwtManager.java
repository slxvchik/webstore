package com.webstore.auth_service.utils;

import com.webstore.auth_service.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieJwtManager {
    private final JwtUtils jwtUtils;

    public Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie("refresh-token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // for dev only
        cookie.setPath("/");
        cookie.setDomain(null);
        cookie.setAttribute("SameSite", "Lax");
        cookie.setMaxAge(jwtUtils.getREFRESH_EXPIRATION());
        return cookie;
    }

    public Cookie createExpiredRefreshTokenCookie() {
        Cookie cookie = new Cookie("refresh-token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setDomain(null);
        cookie.setMaxAge(0);
        return cookie;
    }
}