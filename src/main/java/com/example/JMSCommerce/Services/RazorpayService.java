package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.DTOs.payment.RazorpayOrderResponse;
import com.razorpay.RazorpayException;

import java.math.BigDecimal;

public interface RazorpayService {

    RazorpayOrderResponse createOrder(
            BigDecimal amount,
            String currency,
            String receipt
    ) throws RazorpayException;
    boolean verifyPaymentSignature(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature
    );
    boolean verifyWebhookSignature(
            String payload,
            String signature
    );
}
