package com.example.JMSCommerce.Controller;


import com.example.JMSCommerce.DTOs.payment.PaymentInitiationResponseDTO;
import com.example.JMSCommerce.DTOs.payment.PaymentVerificationRequestDTO;
import com.example.JMSCommerce.DTOs.payment.PaymentVerificationResponseDTO;
import com.example.JMSCommerce.Services.PaymentService;
import com.example.JMSCommerce.Utility.ApiResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/orders/{orderId}/initiate")
    @PermitAll
    public ResponseEntity<ApiResponse<PaymentInitiationResponseDTO>>
    initiatePayment(
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        paymentService.initiatePayment(orderId),
                        "Payment initiated successfully."
                )
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<
            ApiResponse<PaymentVerificationResponseDTO>
            > verifyPayment(
            @Valid
            @RequestBody
            PaymentVerificationRequestDTO request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        paymentService.verifyPayment(request),
                        "Payment verified successfully."
                )
        );
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestHeader("X-Razorpay-Signature")
            String signature,
            @RequestBody String payload
    ) {

        paymentService.handleWebhook(
                payload,
                signature
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/orders/{orderId}/retry")
    public ResponseEntity<ApiResponse<PaymentInitiationResponseDTO>> retryPayment(
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        paymentService.retryPayment(orderId),
                        "Payment retry initiated successfully."
                )
        );
    }

    @PatchMapping("/orders/{orderId}/attempts/{attemptId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelPaymentAttempt(
            @PathVariable Long orderId,
            @PathVariable Long attemptId
    ) {

        paymentService.cancelPaymentAttempt(
                orderId,
                attemptId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Payment attempt cancelled successfully."
                )
        );
    }
}