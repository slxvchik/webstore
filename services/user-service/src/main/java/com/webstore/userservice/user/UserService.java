package com.webstore.userservice.user;

import com.webstore.userservice.user.dto.UserRequest;
import com.webstore.userservice.user.dto.UserResponse;

import java.util.List;

public interface UserService {

    Long createUser(UserRequest request);

    void updateUser(UserRequest request);

    List<UserResponse> findAllUsers();

    UserResponse findUser(Long userId);

    void deleteUser(Long userId);

    boolean existsById(Long userId);
}
