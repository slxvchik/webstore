package com.webstore.auth_service.user;

import com.webstore.auth_service.user.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    void updateUser(String userId, UserRequest request);

    List<UserResponse> findAllUsers(Pageable pageable);

    UserResponse findUser(String userId);

    void deleteUser(String userId);

    void updateAuthUser(String userId, UpdateUserRequest request);

    void updatePassword(String userId, UpdatePasswordRequest request);

    void updateEmail(String userId, UpdateEmailRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void recoveryPassword(PasswordRecoveryRequest request, String confirmationToken);

    String createUser(UserRequest request);
}
