package com.webstore.userservice.user;

import com.webstore.userservice.user.dto.UserRequest;
import com.webstore.userservice.user.dto.UserResponse;
import org.springframework.stereotype.Component;

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
                .phone(request.phone())
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
            user.getPhone()
        );
    }
}
