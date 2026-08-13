package com.example.JMSCommerce.DTOs.order;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequestDTO {
    private Integer quantity;
    private Long productId;
}
