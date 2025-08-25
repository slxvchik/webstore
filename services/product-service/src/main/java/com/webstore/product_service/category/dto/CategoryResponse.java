package com.webstore.product_service.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private String slug;
    private String path;
    private Integer depth;
    private Long parentId;
    private Boolean active;
    private List<CategoryResponse> children;
    public CategoryResponse() {
        children = new ArrayList<>();
    }
}
