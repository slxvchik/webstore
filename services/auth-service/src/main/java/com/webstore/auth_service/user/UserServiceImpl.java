package com.webstore.auth_service.user;

import com.webstore.auth_service.exception.UserNotFoundException;
import com.webstore.auth_service.exception.UserValidateException;
import com.webstore.auth_service.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updateUser(UserRequest request) {
        User user = userRepo.findById(request.id())
                .orElseThrow(() -> new UserNotFoundException("User with id " + request.id() + " not found."));
        validateUserUniques(userMapper.toUser(request));
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        if (StringUtils.isNotBlank(request.fullname())) {
            user.setFullname(request.fullname());
        }
        userRepo.save(user);
    }

    @Override
    public List<UserResponse> findAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse findUser(String userId) {
        return userRepo.findById(userId)
                .map(userMapper::toUserResponse)
                .orElseThrow(
                () -> new UserNotFoundException("User with id " + userId + " not found.")
        );
    }

    @Override
    public void deleteUser(String userId) {
        if (!userRepo.existsById(userId)) {
            throw new UserNotFoundException("Category " + userId + "not found");
        }
        userRepo.deleteById(userId);
    }

    @Override
    public void updateAuthUser(String userId, UpdateUserRequest request) {
        User user = userRepo.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User with id " + userId + " not found.")
        );

        user.setFullname(request.fullname());

        userRepo.save(user);
    }

    @Override
    public void updatePassword(String userId, UpdatePasswordRequest request) {
        User user = userRepo.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User with id " + userId + " not found")
        );
        if (!passwordEncoder.matches(user.getPassword(), request.oldPassword())) {
            throw new UserValidateException("Old password does not match");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepo.save(user);
    }

    @Override
    public void updateUsername(String userId, UpdateUsernameRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User with id " + userId + " not found"));
        if (!passwordEncoder.matches(user.getPassword(), request.password())) {
            throw new UserValidateException("Password does not match");
        }
        user.setUsername(request.newUsername());
        userRepo.save(user);
    }

    @Override
    public void updateEmail(String userId, UpdateEmailRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        if (!passwordEncoder.matches(user.getPassword(), request.password())) {
            throw new UserValidateException("Password does not match");
        }

        // todo: create confirmation token for update email

        // todo: kafka create user-email-update-topic

    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepo.findByEmail(request.email()).orElseThrow(() ->
                new UserNotFoundException("User with email " + request.email() + " not found.")
        );

        // todo: create confirmation token for password recovery

        // todo: kafka create user-email-update-topic

    }

    @Override
    public void recoveryPassword(PasswordRecoveryRequest request, String confirmationToken) {

        // todo: get confirmation token from conf. token repo

        // todo: set new password for user

    }

    @Override
    public String createUser(UserRequest request) {

        User user = User.builder()
                .id(request.id())
                .username(request.username())
                .email(request.email())
                .fullname(request.fullname())
                .password(request.password())
                .phone(request.phone())
                .roles(request.roles())
                .build();

        validateUserUniques(user);

        return userRepo.save(user).getId();
    }

    private void validateUserUniques(User user) {
        List<String> errors = new ArrayList<>();
        if (userRepo.existsByUsername(user.getUsername())) {
            errors.add("The username " + user.getUsername() + " already exists.");
        }
        if (userRepo.existsByEmail(user.getEmail())) {
            errors.add("The email " + user.getEmail() + " already exists.");
        }
        if (user.getPhone() != null && userRepo.existsByPhone(user.getPhone())) {
            errors.add("The phone " + user.getPhone() + " already exists.");
        }
        if (!errors.isEmpty()) {
            throw new UserValidateException("User creation failed: " + String.join(" ", errors));
        }
    }
}
