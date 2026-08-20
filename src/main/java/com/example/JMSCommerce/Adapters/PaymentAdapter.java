package com.example.JMSCommerce.Adapters;

import com.example.JMSCommerce.DTOs.payment.PaymentVerificationResponseDTO;
import com.example.JMSCommerce.Model.Order;
import com.example.JMSCommerce.Model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentAdapter {
    public PaymentVerificationResponseDTO buildVerificationResponse(
            Order order,
            Payment payment,
            boolean success
    ) {

        return PaymentVerificationResponseDTO.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .paymentStatus(
                        payment.getStatus().name()
                )
                .orderStatus(
                        order.getStatus().name()
                )
                .success(success)
                .build();
    }
}
