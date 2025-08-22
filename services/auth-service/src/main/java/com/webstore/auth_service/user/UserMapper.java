package com.webstore.auth_service.user;

import com.webstore.auth_service.user.dto.UserRequest;
import com.webstore.auth_service.user.dto.UserResponse;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toUser(UserRequest request) {
        if (request == null) {
            return null;
        }
        return User.builder()
                .id(request.id())
                .username(request.username())
                .email(request.email())
                .fullname(request.fullname())
                .password(request.password())
                .phone(request.phone())
                .roles(new HashSet<>(request.roles()))
                .build();
    }

    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getFullname(),
            user.getEmail(),
            user.getPhone(),
            "",
            user.getRoles()
        );
    }
}
