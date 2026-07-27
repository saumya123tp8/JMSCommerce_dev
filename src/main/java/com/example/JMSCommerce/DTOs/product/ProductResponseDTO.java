package com.example.JMSCommerce.DTOs.product;

import com.example.JMSCommerce.Utility.enums.CurrencyType;
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
    private BigDecimal mrp;
    private String primaryImage;
    private Double rating;
    private String brandName;
    private CurrencyType currency;
    private BigDecimal sellingPrice;
    private String shortDescription;
}
