package com.webstore.auth_service.user;

import com.webstore.auth_service.exception.UserNotFoundException;
import com.webstore.auth_service.exception.UserValidateException;
import com.webstore.auth_service.user.dto.UserRequest;
import com.webstore.auth_service.user.dto.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public Long createUser(UserRequest request) {
        var user = userMapper.toUser(request);
        validateUserUniques(user);
        return userRepo.save(user).getId();
    }

    @Override
    public void updateUser(UserRequest request) {
        User user = userRepo.findById(request.id())
                .orElseThrow(() -> new UserNotFoundException("User with id " + request.id() + " not found."));
        validateUserUniques(userMapper.toUser(request));
        mergeUsers(user, request);
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
    public UserResponse findUser(Long userId) {
        return userRepo.findById(userId)
                .map(userMapper::toUserResponse)
                .orElseThrow(
                () -> new UserNotFoundException("User with id " + userId + " not found.")
        );
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new UserNotFoundException("Category " + userId + "not found");
        }
        userRepo.deleteById(userId);
    }

    @Override
    public boolean userExistsById(Long userId) {
        return userRepo.existsById(userId);
    }

    private void mergeUsers(User user, UserRequest userRequest) {
        user.setUsername(userRequest.username());
        user.setEmail(userRequest.email());
        user.setPhone(userRequest.phone());
        if (StringUtils.isNotBlank(userRequest.fullname())) {
            user.setFullname(userRequest.fullname());
        }
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
