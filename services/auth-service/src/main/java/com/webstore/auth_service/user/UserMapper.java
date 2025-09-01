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
                .firstName(request.firstName())
                .secondName(request.secondName())
                .email(request.email())
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
            user.getFirstName(),
            user.getSecondName(),
            user.getEmail(),
            user.getPhone(),
            user.getRoles()
        );
    }
}
