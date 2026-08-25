package com.example.JMSCommerce.DTOs.order;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponseDTO {

    private Long orderItemId;

    private Long productId;

    private Long variantId;

    private String productName;

    private String variantName;

    private String sku;

    private Integer quantity;

    private BigDecimal mrp;

    private BigDecimal sellingPrice;

    private BigDecimal customizationPrice;

    private BigDecimal subTotal;

    private String productImage;

    private List<OrderItemCustomizationResponseDTO> customizations;
}
