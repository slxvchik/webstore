package com.webstore.user_service.user;

import com.webstore.user_service.user.dto.UserRequest;
import com.webstore.user_service.user.dto.UserResponse;

import java.util.List;

public interface UserService {

    Long createUser(UserRequest request);

    void updateUser(UserRequest request);

    List<UserResponse> findAllUsers();

    UserResponse findUser(Long userId);

    void deleteUser(Long userId);

    boolean userExistsById(Long userId);
}
