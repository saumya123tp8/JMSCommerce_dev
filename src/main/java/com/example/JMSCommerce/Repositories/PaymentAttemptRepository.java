package com.example.JMSCommerce.Repositories;


import com.example.JMSCommerce.Model.PaymentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentAttemptRepository
        extends JpaRepository<PaymentAttempt, Long> {

    Optional<PaymentAttempt> findByRazorpayOrderId(
            String razorpayOrderId
    );

    Optional<PaymentAttempt> findByRazorpayPaymentId(
            String razorpayPaymentId
    );

    Optional<PaymentAttempt> findByIdAndPayment_Id(
            Long attemptId,
            Long paymentId
    );

    Optional<PaymentAttempt> findTopByPayment_IdOrderByCreatedAtDesc(Long id);
}
