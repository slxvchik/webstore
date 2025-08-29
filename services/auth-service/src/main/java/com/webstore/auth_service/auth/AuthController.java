package com.webstore.auth_service.auth;

import com.webstore.auth_service.auth.dto.LoginRequest;
import com.webstore.auth_service.auth.dto.RegisterRequest;
import com.webstore.auth_service.auth.dto.TokensResponse;
import com.webstore.auth_service.user.User;
import com.webstore.auth_service.user.UserService;
import com.webstore.auth_service.user.dto.UserResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<TokensResponse> login(
            HttpServletResponse response,
            @Valid @RequestBody LoginRequest loginRequest) {

        TokensResponse tokens = authService.login(loginRequest);

        Cookie refreshToken = authService.createCookieRefreshToken(tokens.refreshToken());

        response.addCookie(refreshToken);

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<TokensResponse> logout(
            @AuthenticationPrincipal User user
    ) {
        return null;
    }

//    @GetMapping
//    public ResponseEntity<UserResponse> getAuthenticatedUser(
//            @AuthenticationPrincipal User user
//    ) {
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        return ResponseEntity.ok(userService.findUser(user.getId()));
//    }

    @PostMapping("/token-verify")
    public ResponseEntity<Void> validateToken(
            @AuthenticationPrincipal User user
    ) {
        if (user != null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
