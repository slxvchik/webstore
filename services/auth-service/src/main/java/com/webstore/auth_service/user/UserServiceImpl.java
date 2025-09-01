package com.webstore.auth_service.user;

import com.webstore.auth_service.exception.UserNotFoundException;
import com.webstore.auth_service.exception.UserValidateException;
import com.webstore.auth_service.user.deleted.DeletedUserService;
import com.webstore.auth_service.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final DeletedUserService deletedUserService;

    @Override
    public void updateUser(String userId, UserRequest request) {
        User user = findUserById(userId);

        validateUserUniques(userMapper.toUser(request));

        user.setFirstName(request.firstName());
        user.setSecondName(request.secondName());
        user.setEmail(request.email());
        user.setPhone(request.phone());

        userRepo.save(user);
    }

    @Override
    public List<UserResponse> findAllUsers(Pageable pageable) {
        return userRepo.findAll(pageable)
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse findUser(String userId) {
        return userMapper.toUserResponse(findUserById(userId));
    }

    @Override
    public void deleteUser(String userId) {
        if (!userRepo.existsById(userId)) {
            throw new UserNotFoundException("User " + userId + "not found");
        }

        userRepo.deleteById(userId);

        deletedUserService.markUserAsDeleted(userId);
    }

    @Override
    public void updateAuthUser(String userId, UpdateUserRequest request) {
        User user = findUserById(userId);

        user.setFirstName(request.firstName());
        user.setSecondName(request.secondName());

        userRepo.save(user);
    }

    @Override
    public void updatePassword(String userId, UpdatePasswordRequest request) {
        User user = findUserById(userId);
        if (!passwordEncoder.matches(user.getPassword(), request.oldPassword())) {
            throw new UserValidateException("Old password does not match");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepo.save(user);
    }

    @Override
    public void updateEmail(String userId, UpdateEmailRequest request) {
        User user = findUserById(userId);
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
                .firstName(request.firstName())
                .secondName(request.secondName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phone(request.phone())
                .roles(request.roles())
                .build();

        validateUserUniques(user);

        return userRepo.save(user).getId();
    }

    private void validateUserUniques(User user) {
        List<String> errors = new ArrayList<>();
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

    private User findUserById(String userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found."));
    }
}
