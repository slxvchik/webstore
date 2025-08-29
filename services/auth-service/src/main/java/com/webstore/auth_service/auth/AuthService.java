package com.webstore.auth_service.auth;

import com.webstore.auth_service.auth.dto.LoginRequest;
import com.webstore.auth_service.auth.dto.RegisterRequest;
import com.webstore.auth_service.auth.dto.TokensResponse;
import com.webstore.auth_service.exception.UserValidateException;
import com.webstore.auth_service.user.Role;
import com.webstore.auth_service.user.User;
import com.webstore.auth_service.config.jwt.JwtUtils;
import com.webstore.auth_service.user.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;

    @Transactional
    public String register(RegisterRequest registerRequest) {

        List<String> errors = new ArrayList<>();
        if (userRepo.existsByUsername(registerRequest.username())) {
            errors.add("The username " + registerRequest.phone() + " already exists.");
        }
        if (userRepo.existsByEmail(registerRequest.email())) {
            errors.add("The email " + registerRequest.phone() + " already exists.");
        }
        if (registerRequest.phone() != null && userRepo.existsByPhone(registerRequest.phone())) {
            errors.add("The phone " + registerRequest.phone() + " already exists.");
        }
        if (!errors.isEmpty()) {
            throw new UserValidateException("User creation failed: " + String.join(" ", errors));
        }

        var roles = new HashSet<Role>();
        roles.add(Role.USER);

        var user = User.builder()
                .username(registerRequest.username())
                .fullname(registerRequest.fullname())
                .phone(registerRequest.phone())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .roles(roles)
                .build();

        String userId = userRepo.save(user).getId();

        // todo: send notification to confirm email

        return userId;
    }

    public TokensResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + loginRequest.username() + " not found."));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new UserValidateException("Wrong username or password.");
        }

        var accessToken = jwtUtils.createAccessToken(user);
        var refreshToken = jwtUtils.createRefreshToken(user);

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
}
