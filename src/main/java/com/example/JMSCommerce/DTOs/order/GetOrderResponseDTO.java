package com.example.JMSCommerce.DTOs.order;

import com.example.JMSCommerce.Utility.enums.OrderStatus;
import com.example.JMSCommerce.Utility.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetOrderResponseDTO {

    private Long id;

    private OrderStatus orderStatus;

    private PaymentStatus paymentStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String deliveredAt;

    private BigDecimal subtotal;

    private BigDecimal discount;

    private BigDecimal tax;

    private BigDecimal deliveryCharge;

    private BigDecimal grandTotal;

    private List<OrderItemResponseDTO> orderItems;
}