package com.webstore.auth_service.user.deleted;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "deleted_users")
@Getter
@Setter
public class DeletedUser {
    @Id
    private String userId;

    @Column(nullable = false)
    private Instant deletedAt;

    @Column(nullable = false)
    private Instant expiresAt;

    @CreationTimestamp
    private Instant created;
}