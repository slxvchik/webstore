package com.webstore.product_service.product;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    boolean existsByName(String name);
    Optional<Product> findByName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    List<Product> findAllByIdInWithLock(@Param("ids") List<Long> id);

    @Modifying
    @Query("UPDATE Product p SET p.quantity = p.quantity - :quantity WHERE p.id = :id")
    void decreaseStock(@Param("id") Long productId, @Param("quantity") Integer quantity);
}

