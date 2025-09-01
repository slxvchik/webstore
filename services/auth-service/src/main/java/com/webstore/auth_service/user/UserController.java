package com.webstore.auth_service.user;

import com.webstore.auth_service.jwt.blacklist.JwtBlacklistService;
import com.webstore.auth_service.user.dto.*;
import com.webstore.auth_service.utils.CookieJwtManager;
import com.webstore.auth_service.utils.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final ServletUtil servletUtil;
    private final JwtBlacklistService jwtBlacklistService;
    private final CookieJwtManager cookieJwtManager;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getAuthUser(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(userService.findUser(user.getId()));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @PutMapping("/me")
    public ResponseEntity<Void> updateAuthUser(
            @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal User user
    ) {
        userService.updateAuthUser(user.getId(), request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAuthUser(
            @AuthenticationPrincipal User user,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        userService.deleteUser(user.getId());

        jwtBlacklistService.addToBlacklist(servletUtil.getAccessTokenFromHeader(request));
        jwtBlacklistService.addToBlacklist(servletUtil.getRefreshTokenFromCookie(request));

        response.addCookie(cookieJwtManager.createExpiredRefreshTokenCookie());

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @PatchMapping("/me/password")
    public ResponseEntity<Void> updateAuthUserPassword(
            @RequestBody @Valid UpdatePasswordRequest request,
            @AuthenticationPrincipal User user
    ) {
        userService.updatePassword(user.getId(), request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER', 'USER')")
    @PatchMapping("/me/email")
    public ResponseEntity<Void> updateAuthUserEmail(
            @RequestBody @Valid UpdateEmailRequest request,
            @AuthenticationPrincipal User user
    ) {
        userService.updateEmail(user.getId(), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<Void> forgotPassword(
            @RequestBody @Valid ForgotPasswordRequest request
    ) {
        userService.forgotPassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/recovery/{confirmation-token}")
    public ResponseEntity<Void> recoveryPassword(
            @RequestBody @Valid PasswordRecoveryRequest request,
            @PathVariable("confirmation-token") String confirmationToken
    ) {
        userService.recoveryPassword(request, confirmationToken);
        return ResponseEntity.ok().build();
    }

    /***
     * ==========================ADMIN & PM RIGHTS==========================
      */

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(userService.findAllUsers(pageable));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<String> createUser(
            @RequestBody @Valid UserRequest request
    ) {
        return ResponseEntity.accepted().body(userService.createUser(request));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PRODUCT_MANAGER')")
    @GetMapping("/{user-id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable("user-id") String userId
    ) {
        return ResponseEntity.ok(userService.findUser(userId));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{user-id}")
    public ResponseEntity<Void> updateUser(
            @RequestBody UserRequest request,
            @PathVariable("user-id") String userId
    ) {
        userService.updateUser(userId, request);
        return ResponseEntity.accepted().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{user-id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("user-id") String userId
    ) {
        userService.deleteUser(userId);
        return ResponseEntity.accepted().build();
    }

}
