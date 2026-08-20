package com.example.JMSCommerce.DTOs.payment;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInitiationResponseDTO {

    private Long orderId;

    private String orderNumber;

    private String razorpayOrderId;

    /**
     * Amount in paise for Razorpay.
     */
    private Long amount;

    private String currency;

    /**
     * Razorpay Key ID.
     * This is safe to expose to frontend.
     */
    private String keyId;

    private Long paymentAttemptId;
}