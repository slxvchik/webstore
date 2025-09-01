package com.webstore.auth_service.jwt.blacklist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, String> {
    Optional<JwtBlacklist> findByJwtIdAndUserId(String tokenId, String userId);
    @Modifying
    @Query("DELETE FROM JwtBlacklist j WHERE j.expiresAt < :now")
    void deleteAllExpiredSince(@Param("now") Instant now);
    boolean existsByJwtIdAndUserId(String tokenId, String userId);
}
