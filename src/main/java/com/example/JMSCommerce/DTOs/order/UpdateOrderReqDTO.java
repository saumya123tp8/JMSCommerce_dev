package com.example.JMSCommerce.DTOs.order;

import com.example.JMSCommerce.Utility.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderReqDTO {
    private OrderStatus status;
//    private List<OrderItemActionDto> orderItems;
}
