package com.webstore.product_service.product;

import com.webstore.product_service.category.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false, unique = true)
    private String name;
    @Column
    private String description;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "thumbnail_image_id")
    private String thumbnailImageId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "product_gallery",
            joinColumns = @JoinColumn(name = "product_id")
    )
    private List<String> galleryImageIds = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime created;

    //aggregated data
    private Float rating;
    @Column(name = "reviews_count")
    private Integer reviewsCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
