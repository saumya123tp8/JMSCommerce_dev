package com.example.JMSCommerce.DTOs;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderItemResponseDTO {
    private Integer quantity;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private BigDecimal subTotal;
    private String productImage;
}
