package com.example.JMSCommerce.Adapters;

import com.example.JMSCommerce.DTOs.CategoryResponseDTO;
import com.example.JMSCommerce.Model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryAdapter {
    public CategoryResponseDTO mapToCategoryResponseDTO(Category category) {

        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .status(category.getStatus())
                .parentId(
                        category.getParent() == null
                                ? null
                                : category.getParent().getId()
                )
                .build();
    }
}
