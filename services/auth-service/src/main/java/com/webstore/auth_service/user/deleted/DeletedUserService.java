package com.webstore.auth_service.user.deleted;

import com.webstore.auth_service.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class DeletedUserService {
    private final DeletedUserRepository deletedUserRepository;
    private final JwtUtils jwtUtils;

    public void markUserAsDeleted(String userId) {
        DeletedUser deletedUser = new DeletedUser();
        deletedUser.setUserId(userId);
        deletedUser.setDeletedAt(Instant.now());
        deletedUser.setExpiresAt(Instant.now().plusSeconds(jwtUtils.getREFRESH_EXPIRATION()));

        deletedUserRepository.save(deletedUser);
    }

    public boolean isUserDeleted(String userId) {
        return deletedUserRepository.existsByUserId(userId);
    }

    @Transactional
    @Scheduled(cron = "0 0 4 * * ?")
    public void cleanupExpiredDeletedUsers() {
        deletedUserRepository.deleteAllExpired(Instant.now());
    }
}
