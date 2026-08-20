package com.example.JMSCommerce.DTOs.payment;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RazorpayOrderResponse {

    private String razorpayOrderId;

    private Long amount;

    private String currency;

    private String status;
}