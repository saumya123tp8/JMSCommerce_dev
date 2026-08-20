package com.example.JMSCommerce.Model;

import com.example.JMSCommerce.Utility.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "payment_attempts",
        indexes = {
                @Index(
                        name = "idx_attempt_payment",
                        columnList = "payment_id"
                ),
                @Index(
                        name = "idx_attempt_razorpay_order",
                        columnList = "razorpay_order_id"
                ),
                @Index(
                        name = "idx_attempt_razorpay_payment",
                        columnList = "razorpay_payment_id"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAttempt extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "payment_id",
            nullable = false
    )
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.INITIATING;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 10)
    private String currency;

    /**
     * Razorpay Order ID
     * Example: order_xxxxxxxxx
     */
    @Column(
            name = "razorpay_order_id",
            unique = true
    )
    private String razorpayOrderId;

    /**
     * Razorpay Payment ID
     * Example: pay_xxxxxxxxx
     */
    @Column(
            name = "razorpay_payment_id",
            unique = true
    )
    private String razorpayPaymentId;

    /**
     * Signature returned by Razorpay Checkout.
     */
    @Column(
            name = "razorpay_signature",
            length = 255
    )
    private String razorpaySignature;

    private LocalDateTime initiatedAt;

    private LocalDateTime paidAt;

    private LocalDateTime failedAt;

    @Column(length = 100)
    private String failureCode;

    @Column(length = 1000)
    private String failureMessage;


}