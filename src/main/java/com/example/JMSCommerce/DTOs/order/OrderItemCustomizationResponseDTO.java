package com.example.JMSCommerce.DTOs.order;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemCustomizationResponseDTO {

    private Long customizationOptionId;

    private String name;

    private BigDecimal priceAdjustment;
}
