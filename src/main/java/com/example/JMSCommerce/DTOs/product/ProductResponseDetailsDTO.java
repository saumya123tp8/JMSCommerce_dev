package com.example.JMSCommerce.DTOs.product;

import com.example.JMSCommerce.Model.Category;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductResponseDetailsDTO extends ProductResponseDTO{
    private Category category;
}
