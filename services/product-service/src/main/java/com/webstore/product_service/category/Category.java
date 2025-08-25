package com.webstore.product_service.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webstore.product_service.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "categories", indexes = @Index(columnList = "path"))
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    @Column(unique = true, nullable = false)
    private String slug;
    @Column(unique = true, nullable = false)
    private String path;
    private Integer depth;
    @Column(columnDefinition = "boolean default true")
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;


    @JsonIgnore
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Product> products = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Category> subCategories = new HashSet<>();
}
