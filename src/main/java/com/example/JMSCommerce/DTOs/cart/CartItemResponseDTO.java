package com.example.JMSCommerce.DTOs.cart;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDTO {

    private String id;

    private Long variantId;

    private Long productId;

    private String productName;

    private String variantName;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal customizationPrice;

    private BigDecimal totalPrice;

    private List<String> selectedCustomizations;

    private String image;

    private Integer availableStock;

    private Boolean available;

}
