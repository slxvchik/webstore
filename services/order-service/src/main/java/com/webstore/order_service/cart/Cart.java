package com.webstore.order_service.cart;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "carts", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_id", "user_id"})
})
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, name = "product_id")
    private Long productId;
    @Column(nullable = false, name = "user_id")
    private Long userId;
    @Column(nullable = false)
    private Integer quantity;
    @CreationTimestamp
    @Column(name = "created", updatable = false, nullable = false)
    private LocalDateTime created;
}
