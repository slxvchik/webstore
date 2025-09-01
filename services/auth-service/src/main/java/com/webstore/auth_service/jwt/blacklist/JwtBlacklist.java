package com.webstore.auth_service.jwt.blacklist;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "jwt_blacklist", indexes = {
        @Index(name = "idx_jwt_user", columnList = "jwtId,userId"),
        @Index(name = "idx_expires_at", columnList = "expiresAt")
})
@Getter
@Setter
public class JwtBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(nullable = false, name = "jwt_id")
    private String jwtId;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private Instant expiresAt;
    @CreationTimestamp
    Instant created;
}
