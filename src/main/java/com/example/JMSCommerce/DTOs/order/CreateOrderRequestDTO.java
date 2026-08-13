package com.example.JMSCommerce.DTOs.order;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequestDTO {
    private Long userId;
    private String deliveredAt;
    private List<OrderItemRequestDTO> productsInOrder;
}
