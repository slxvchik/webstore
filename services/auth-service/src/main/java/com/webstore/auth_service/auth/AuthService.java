package com.webstore.auth_service.auth;

import com.webstore.auth_service.auth.dto.TokensResponse;
import com.webstore.auth_service.exception.UserValidateException;
import com.webstore.auth_service.security.CsrfTokenGenerator;
import com.webstore.auth_service.user.User;
import com.webstore.auth_service.user.UserDetailService;
import com.webstore.auth_service.config.jwt.JwtUtils;
import com.webstore.auth_service.user.UserRepository;
import com.webstore.auth_service.user.dto.UserRequest;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TokensResponse login(UserRequest userRequest, String csrfToken) {
        User user = userRepository.findByUsername(userRequest.username())
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + userRequest.username() + " not found."));

        if (!passwordEncoder.matches(userRequest.password(), user.getPassword())) {
            throw new UserValidateException("Wrong username or password.");
        }

        var accessToken = jwtUtils.createAccessToken(user);
        var refreshToken = jwtUtils.createRefreshToken(user, csrfToken);

        return new TokensResponse(accessToken, refreshToken);
    }

    public Cookie createCookieRefreshToken(String refreshToken) {

        var refreshTokenCookie = new Cookie("refresh-token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // for dev only
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setDomain(null);
        refreshTokenCookie.setAttribute("SameSite", "Lax");
        refreshTokenCookie.setMaxAge(jwtUtils.getREFRESH_EXPIRATION());

        return refreshTokenCookie;
    }

    public Cookie createCookieCsrfToken(String csrfToken) {
        var refreshTokenCookie = new Cookie("X-CSRF-TOKEN", csrfToken);
        refreshTokenCookie.setHttpOnly(false);
        refreshTokenCookie.setSecure(false); // for dev only
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setDomain(null);
        refreshTokenCookie.setAttribute("SameSite", "Lax");
        refreshTokenCookie.setMaxAge(jwtUtils.getREFRESH_EXPIRATION());

        return refreshTokenCookie;
    }
}
