package com.example.JMSCommerce.DTOs.product;

import com.example.JMSCommerce.Model.Brand;
import com.example.JMSCommerce.Model.Category;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductResponseDetailsDTO extends ProductResponseDTO{
    private CategorySummaryDTO category;
    private BrandSummaryDTO brand;
    private String description;
//    private Integer availableQuantity;
    private Integer ratingCount ;
    private Integer reviewCount ;
}
