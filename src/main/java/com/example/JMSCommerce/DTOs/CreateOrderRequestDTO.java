package com.example.JMSCommerce.DTOs;

import com.example.JMSCommerce.Model.OrderStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequestDTO {
    private OrderStatus status;
}
