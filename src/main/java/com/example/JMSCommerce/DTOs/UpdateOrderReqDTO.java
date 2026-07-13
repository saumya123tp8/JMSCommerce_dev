package com.example.JMSCommerce.DTOs;

import com.example.JMSCommerce.Model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderReqDTO {
    private OrderStatus status;
    private List<OrderItemActionDto> orderItems;
}
