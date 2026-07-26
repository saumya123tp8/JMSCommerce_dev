package com.example.JMSCommerce.DTOs.product;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductResponseDTO {
    private Long id;
    private String name ;
    private String description;
    private BigDecimal mrp;
    private String primaryImage;
    private Double rating;

}
