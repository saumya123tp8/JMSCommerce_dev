package com.example.JMSCommerce.DTOs.order;

import com.example.JMSCommerce.Utility.enums.AddressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemActionDto {

    private Long productId;

    private Integer quantity;

    private AddressType.OrderItemAction action;
}