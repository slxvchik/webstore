package com.webstore.order_service.orderline;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webstore.order_service.order.Order;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @Column(nullable = false, name = "price_per_item")
    private BigDecimal pricePerItem;
    @Column(nullable = false)
    private Integer quantity;
}
