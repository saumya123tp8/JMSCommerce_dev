package com.example.JMSCommerce.DTOs.payment;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVerificationResponseDTO {

    private Long orderId;

    private String orderNumber;

    private String paymentStatus;

    private String orderStatus;

    private boolean success;
}
