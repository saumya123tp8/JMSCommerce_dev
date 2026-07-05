package com.example.JMSCommerce.DTOs;

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
    private String title;
    private String description;
    private BigDecimal price;
    private String image;
    private int rating;

}
