package com.webstore.auth_service.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsById(String id);

    @Query("select u from User u left join fetch u.roles where u.id = :userId")
    Optional<User> findByIdWithAuthorities(String userId);
    Optional<User> findByEmail(String email);
}
