package com.webstore.auth_service.user;

import com.webstore.auth_service.user.dto.*;

import java.util.List;

public interface UserService {

    void updateUser(UserRequest request);

    List<UserResponse> findAllUsers();

    UserResponse findUser(Long userId);

    void deleteUser(Long userId);

    void updateAuthUser(Long userId, UpdateUserRequest request);

    void updatePassword(Long userId, UpdatePasswordRequest request);

    void updateUsername(Long userId, UpdateUsernameRequest request);

    void updateEmail(Long userId, UpdateEmailRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void recoveryPassword(PasswordRecoveryRequest request, String confirmationToken);

    Long createUser(UserRequest request);
}
