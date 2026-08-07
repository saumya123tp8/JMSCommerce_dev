package com.example.JMSCommerce.DTOs.cart;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    /**
     * Unique UUID for this cart item
     */
    private String id;

    /**
     * Variant selected
     */
    private Long variantId;

    /**
     * Quantity
     */
    private Integer quantity;

    /**
     * Selected customization options
     */
    @Builder.Default
    private List<Long> customizationOptionIds = new ArrayList<>();

}
