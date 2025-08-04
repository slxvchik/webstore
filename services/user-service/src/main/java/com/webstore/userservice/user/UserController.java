package com.webstore.userservice.user;

import com.webstore.userservice.user.dto.UserRequest;
import com.webstore.userservice.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Long> createUser(
            @RequestBody @Valid UserRequest request
    ) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(
            @RequestBody @Valid UserRequest request
    ) {
        userService.updateUser(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable("user-id") Long userId
    ) {
        return ResponseEntity.ok(userService.findUser(userId));
    }

    @DeleteMapping("/{user-id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("user-id") Long userId
    ) {
        userService.deleteUser(userId);
        return ResponseEntity.accepted().build();
    }

}
