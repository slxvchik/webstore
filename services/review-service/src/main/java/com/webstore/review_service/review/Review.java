package com.webstore.review_service.review;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(
    name = "reviews",
    uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "user_id"})
)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @Column(nullable = false, name = "product_id")
    private String productId;
    @Column(nullable = false, name = "user_id")
    private String userId;
    @Column(nullable = false)
    private String text;
    @Column(nullable = false)
    private Integer rating;

    @ElementCollection
    @CollectionTable(
            name = "review_images",
            joinColumns = @JoinColumn(name = "review_id")
    )
    private List<String> attachedImages;
}
