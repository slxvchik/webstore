package com.webstore.product_service.product;

import com.webstore.product_service.category.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name must not be blank")
    @Size(min = 3, max = 500, message = "Name must be between 3 and 500 characters")
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    @Column(nullable = false)
    @Positive
    private BigDecimal price;
    @Column(nullable = false)
    @PositiveOrZero
    private Integer quantity;

    //aggregated data
    @PositiveOrZero
    private Float rating;
    @Column(name = "reviews_count")
    @PositiveOrZero
    private Integer reviewsCount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
