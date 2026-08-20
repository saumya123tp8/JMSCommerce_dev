package com.example.JMSCommerce.Services;


import com.example.JMSCommerce.DTOs.payment.PaymentInitiationResponseDTO;
import com.example.JMSCommerce.DTOs.payment.PaymentVerificationRequestDTO;
import com.example.JMSCommerce.DTOs.payment.PaymentVerificationResponseDTO;

public interface PaymentService {

    PaymentInitiationResponseDTO initiatePayment(
            Long orderId
    );

    PaymentVerificationResponseDTO verifyPayment(
            PaymentVerificationRequestDTO request
    );
    void handleWebhook(
            String payload,
            String signature
    );
    PaymentInitiationResponseDTO retryPayment(Long orderId);
    void cancelPaymentAttempt(Long orderId, Long attemptId);
}