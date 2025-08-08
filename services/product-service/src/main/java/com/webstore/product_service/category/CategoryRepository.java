package com.webstore.product_service.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsById(Long id);
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
    Optional<Category> findBySlug(String slug);
}
