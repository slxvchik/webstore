package com.webstore.auth_service.user;

import com.webstore.auth_service.user.dto.*;

import java.util.List;

public interface UserService {

    void updateUser(UserRequest request);

    List<UserResponse> findAllUsers();

    UserResponse findUser(String userId);

    void deleteUser(String userId);

    void updateAuthUser(String userId, UpdateUserRequest request);

    void updatePassword(String userId, UpdatePasswordRequest request);

    void updateUsername(String userId, UpdateUsernameRequest request);

    void updateEmail(String userId, UpdateEmailRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void recoveryPassword(PasswordRecoveryRequest request, String confirmationToken);

    String createUser(UserRequest request);
}
