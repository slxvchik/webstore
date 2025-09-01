package com.webstore.auth_service.user.deleted;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface DeletedUserRepository extends JpaRepository<DeletedUser, String> {
    boolean existsByUserId(String userId);

    @Modifying
    @Query("DELETE FROM DeletedUser d WHERE d.expiresAt < :now")
    void deleteAllExpired(@Param("now") Instant now);

    Optional<DeletedUser> findByUserId(String userId);
}
