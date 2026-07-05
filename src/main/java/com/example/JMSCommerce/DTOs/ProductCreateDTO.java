package com.example.JMSCommerce.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDTO {

    private String title;
    private String description;
    private BigDecimal price;
    private String image;
    private Long category;
    private int rating;


}
