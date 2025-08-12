package com.webstore.auth_service.auth;

import com.webstore.auth_service.auth.dto.TokensResponse;
import com.webstore.auth_service.security.CsrfTokenGenerator;
import com.webstore.auth_service.user.User;
import com.webstore.auth_service.user.UserService;
import com.webstore.auth_service.user.dto.UserRequest;
import com.webstore.auth_service.user.dto.UserResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

//    @GetMapping
//    public ResponseEntity<UserResponse> getUser(
//            Authentication authentication
//    ) {
//
//    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(
            @Valid @RequestBody UserRequest userRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<TokensResponse> login(
            HttpServletResponse response,
            @Valid @RequestBody UserRequest userRequest) {

        var csrfToken = CsrfTokenGenerator.generateCsrfToken();

        TokensResponse tokens = authService.login(userRequest, csrfToken);

        Cookie refreshToken = authService.createCookieRefreshToken(tokens.refreshToken());
        Cookie csrfTokenCookie = authService.createCookieCsrfToken(csrfToken);

        response.addCookie(refreshToken);
        response.addCookie(csrfTokenCookie);

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<TokensResponse> logout(
            @AuthenticationPrincipal User user
    ) {
        return null;
    }

    @GetMapping
    public ResponseEntity<UserResponse> getAuthenticatedUser(
            @AuthenticationPrincipal User user
    ) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userService.findUser(user.getId()));
    }

    @PostMapping("/token-verify")
    public ResponseEntity<Boolean> validateToken(
            @AuthenticationPrincipal User user
    ) {

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (user != null) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
