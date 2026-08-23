package com.example.JMSCommerce.Services;


import com.example.JMSCommerce.Adapters.PaymentAdapter;
import com.example.JMSCommerce.DTOs.payment.PaymentInitiationResponseDTO;
import com.example.JMSCommerce.DTOs.payment.PaymentVerificationRequestDTO;
import com.example.JMSCommerce.DTOs.payment.PaymentVerificationResponseDTO;
import com.example.JMSCommerce.DTOs.payment.RazorpayOrderResponse;
import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Exception.PaymentGatewayException;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.*;
import com.example.JMSCommerce.Repositories.*;
import com.example.JMSCommerce.Services.cart.CartOwnerProvider;
import com.example.JMSCommerce.Services.cart.CartRedisService;
import com.example.JMSCommerce.Utility.SecurityUtils;
import com.example.JMSCommerce.Utility.enums.CurrencyType;
import com.example.JMSCommerce.Utility.enums.OrderStatus;
import com.example.JMSCommerce.Utility.enums.PaymentMethod;
import com.example.JMSCommerce.Utility.enums.PaymentStatus;
import com.example.JMSCommerce.config.RazorpayProperties;
import com.razorpay.RazorpayException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final InventoryService inventoryService;
    private final OrderRepo orderRepository;
    private final PaymentRepository paymentRepository;
    private final RazorpayService razorpayService;
    private final PaymentAttemptRepository paymentAttemptRepository;
    private final PaymentAdapter paymentAdapter;
    private final UserRepo userRepo;
    private final RazorpayProperties razorpayProperties;
    private final OrderItemRepo orderItemRepo;
    private final CartOwnerProvider cartOwnerProvider;
    private final CartRedisService cartRedisService;
    @PersistenceContext
    EntityManager entityManager;
    @Value("${razorpay.key-id}")
    private String razorpayKeyId;

    @Override
    @Transactional
    public PaymentInitiationResponseDTO initiatePayment(Long orderId) {

        String currentUserMail =
                SecurityUtils.getCurrentUserMail();

        Order order =
                orderRepository
                        .findByIdAndUser_email(
                                orderId,
                                currentUserMail
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                )
                        );

        // TODO: verify order belongs to current authenticated user

        if (order.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException(
                    "Payment has already been completed for this order."
            );
        }

        Payment payment = paymentRepository
                .findByOrder_Id(orderId)
                .orElseGet(() ->
                        Payment.builder()
                                .order(order)
                                .amount(order.getGrandTotal())
                                .currency(CurrencyType.INR)
                                .status(PaymentStatus.PENDING)
                                .build()
                );



        payment.setStatus(PaymentStatus.INITIATING);

        payment = paymentRepository.save(payment);
        //due to cascade has some issue so I am managing it explicitly
        PaymentAttempt attempt = PaymentAttempt.builder()
                .payment(payment)
                .amount(order.getGrandTotal())
                .currency("INR")
                .status(PaymentStatus.INITIATING)
                .initiatedAt(LocalDateTime.now())
                .build();
        payment.addAttempt(attempt);
        System.out.println("Payment managed: "
                + entityManager.contains(payment));

        System.out.println("Attempt managed: "
                + entityManager.contains(attempt));
        try {

            RazorpayOrderResponse razorpayOrder =
                    razorpayService.createOrder(
                            order.getGrandTotal(),
                            order.getCurrency().toString(),
                            order.getOrderNumber()
                    );

            attempt.setRazorpayOrderId(
                    razorpayOrder.getRazorpayOrderId()
            );

            attempt.setStatus(
                    PaymentStatus.INITIATED
            );
            System.out.println("Attempt managed after update: "
                    + entityManager.contains(attempt));
            payment.setStatus(
                    PaymentStatus.INITIATED
            );
            // Explicitly persist attempt( while saving payment can save payment_attempt due to cascade type all )
            paymentAttemptRepository.save(attempt);
//            paymentAttemptRepository.save(attempt);
            paymentRepository.save(payment);

            return PaymentInitiationResponseDTO.builder()
                    .orderId(order.getId())
                    .orderNumber(order.getOrderNumber())
                    .razorpayOrderId(
                            razorpayOrder.getRazorpayOrderId()
                    )
                    .amount(
                            razorpayOrder.getAmount()
                    )
                    .currency(
                            razorpayOrder.getCurrency()
                    )
                    .keyId(razorpayKeyId)
                    .paymentAttemptId(attempt.getId())
                    .build();

        } catch (RazorpayException e) {

            attempt.setStatus(
                    PaymentStatus.FAILED
            );

            attempt.setFailedAt(
                    LocalDateTime.now()
            );

            attempt.setFailureMessage(
                    e.getMessage()
            );

            payment.setStatus(
                    PaymentStatus.FAILED
            );

            // Explicitly persist attempt( while saving payment can save payment_attempt due to cascade type all )
            paymentAttemptRepository.save(attempt);
            paymentRepository.save(payment);
            log.error("Razorpay order creation failed", e);
            throw new PaymentGatewayException(
                    "Unable to initiate payment."
            );
        }
    }
    @Override
    @Transactional
    public PaymentVerificationResponseDTO verifyPayment(
            PaymentVerificationRequestDTO request
    ) {

        PaymentAttempt attempt =
                paymentAttemptRepository
                        .findByRazorpayOrderId(
                                request.getRazorpayOrderId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment attempt not found."
                                )
                        );

        Payment payment = attempt.getPayment();

        Order order = payment.getOrder();

        /*
         * Idempotency:
         *
         * If this payment was already successfully processed,
         * simply return the existing result.
         */
        if (attempt.getStatus() == PaymentStatus.SUCCESS) {

            return paymentAdapter.buildVerificationResponse(
                    order,
                    payment,
                    true
            );
        }

        /*
         * Make sure the payment ID belongs to this attempt.
         *
         * This also prevents somebody from mixing:
         *
         * order_A
         * payment_B
         * signature_C
         */
        if (
                attempt.getRazorpayPaymentId() != null
                        &&
                        !attempt.getRazorpayPaymentId()
                                .equals(request.getRazorpayPaymentId())
        ) {

            throw new BadRequestException(
                    "Payment ID does not match payment attempt."
            );
        }

        /*
         * Verify Razorpay signature.
         */
        boolean valid =
                razorpayService.verifyPaymentSignature(
                        request.getRazorpayOrderId(),
                        request.getRazorpayPaymentId(),
                        request.getRazorpaySignature()
                );

        if (!valid) {

            attempt.setStatus(
                    PaymentStatus.FAILED
            );

            attempt.setFailureMessage(
                    "Invalid Razorpay payment signature."
            );

            attempt.setFailedAt(
                    LocalDateTime.now()
            );

            payment.setStatus(
                    PaymentStatus.FAILED
            );

            order.setPaymentStatus(
                    PaymentStatus.FAILED
            );

            paymentAttemptRepository.save(attempt);
            paymentRepository.save(payment);
            orderRepository.save(order);

            throw new BadRequestException(
                    "Payment verification failed."
            );
        }

        /*
         * Signature is valid.
         */
        attempt.setRazorpayPaymentId(
                request.getRazorpayPaymentId()
        );

        attempt.setRazorpaySignature(
                request.getRazorpaySignature()
        );

        attempt.setStatus(
                PaymentStatus.SUCCESS
        );

        attempt.setPaidAt(
                LocalDateTime.now()
        );

        payment.setStatus(
                PaymentStatus.SUCCESS
        );

        order.setPaymentStatus(
                PaymentStatus.SUCCESS
        );

        order.setStatus(
                OrderStatus.CONFIRMED
        );

        // clear cart here after success
        // Payment succeeded → cart can now be cleared
        String ownerId = cartOwnerProvider.getOwnerId();
        cartRedisService.deleteCart(
                ownerId
        );

        paymentAttemptRepository.save(attempt);
        paymentRepository.save(payment);
        orderRepository.save(order);

        return paymentAdapter.buildVerificationResponse(
                order,
                payment,
                true
        );
    }
    @Override
    @Transactional
    public void handleWebhook(
            String payload,
            String signature
    ) {

        boolean valid =
                razorpayService.verifyWebhookSignature(
                        payload,
                        signature
                );

        if (!valid) {
            throw new BadRequestException(
                    "Invalid Razorpay webhook signature."
            );
        }

        JSONObject webhook =
                new JSONObject(payload);

        String event =
                webhook.getString("event");

        switch (event) {

            case "payment.captured" ->
                    handlePaymentCaptured(webhook);

            case "payment.failed" ->
                    handlePaymentFailed(webhook);

            default ->
                // Ignore events that our application
                // doesn't currently need.
                    log.info(
                            "Ignoring Razorpay event: {}",
                            event
                    );
        }
    }
    private void handlePaymentCaptured(
            JSONObject webhook
    ) {

        JSONObject payload =
                webhook
                        .getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");

        String razorpayPaymentId =
                payload.getString("id");

        String razorpayOrderId =
                payload.getString("order_id");

        PaymentAttempt attempt =
                paymentAttemptRepository
                        .findByRazorpayOrderId(
                                razorpayOrderId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment attempt not found."
                                )
                        );

        /*
         * Idempotency:
         *
         * Razorpay may deliver the webhook more than once.
         */
        if (attempt.getStatus() ==
                PaymentStatus.SUCCESS) {

            return;
        }

        Payment payment =
                attempt.getPayment();

        Order order =
                payment.getOrder();

        attempt.setRazorpayPaymentId(
                razorpayPaymentId
        );

        attempt.setStatus(
                PaymentStatus.SUCCESS
        );

        attempt.setPaidAt(
                LocalDateTime.now()
        );

        payment.setStatus(
                PaymentStatus.SUCCESS
        );

        order.setPaymentStatus(
                PaymentStatus.SUCCESS
        );

        order.setStatus(
                OrderStatus.CONFIRMED
        );

        paymentAttemptRepository.save(attempt);
        paymentRepository.save(payment);
        orderRepository.save(order);
    }
    private void handlePaymentFailed(
            JSONObject webhook
    ) {

        JSONObject payload =
                webhook
                        .getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");

        String razorpayPaymentId =
                payload.getString("id");

        String razorpayOrderId =
                payload.getString("order_id");

        PaymentAttempt attempt =
                paymentAttemptRepository
                        .findByRazorpayOrderId(
                                razorpayOrderId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment attempt not found."
                                )
                        );

        if (attempt.getStatus() ==
                PaymentStatus.SUCCESS) {

            return;
        }

        Payment payment =
                attempt.getPayment();

        Order order =
                payment.getOrder();

        attempt.setRazorpayPaymentId(
                razorpayPaymentId
        );

        attempt.setStatus(
                PaymentStatus.FAILED
        );

        attempt.setFailedAt(
                LocalDateTime.now()
        );

        payment.setStatus(
                PaymentStatus.FAILED
        );

        order.setPaymentStatus(
                PaymentStatus.FAILED
        );
        if (attempt.getStatus() != PaymentStatus.FAILED) {// to stock webhook multiple time release
            attempt.setStatus(PaymentStatus.FAILED);

            releaseOrderInventory(order);
        }

        paymentAttemptRepository.save(attempt);
        paymentRepository.save(payment);
        orderRepository.save(order);
    }

    private void releaseOrderInventory(Order order) {

        List<OrderItem> listOrderItem = orderItemRepo.findByOrder_Id(order.getId());
        for (OrderItem item : listOrderItem) {

            if (Boolean.TRUE.equals(
                    item.getInventoryReserved()
            )) {

                inventoryService.release(
                        item.getVariant().getId(),
                        item.getQuantity()
                );

                item.setInventoryReserved(false);
            }
        }
    }
    @Override
    @Transactional
    public PaymentInitiationResponseDTO retryPayment(
            Long orderId
    ) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found."
                                )
                        );

        /*
         * TODO:
         * Verify that this order belongs to the
         * currently authenticated user.
         */

        if (order.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException(
                    "Payment is already completed for this order."
            );
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException(
                    "Cancelled order cannot be paid."
            );
        }

        Payment payment =
                paymentRepository.findByOrder_Id(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found for this order."
                                )
                        );



        if (payment.getMethod() != PaymentMethod.RAZORPAY) {
            throw new BadRequestException(
                    "Retry payment is only available for Razorpay orders."
            );
        }

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException(
                    "Payment is already successful."
            );
        }

        PaymentAttempt latestAttempt =
                paymentAttemptRepository
                        .findTopByPayment_IdOrderByCreatedAtDesc(
                                payment.getId()
                        )
                        .orElse(null);
        if (latestAttempt != null &&
                latestAttempt.getStatus() == PaymentStatus.INITIATED) {

            latestAttempt.setStatus(
                    PaymentStatus.CANCELLED
            );
            paymentAttemptRepository.save(latestAttempt);
        }
        /*
         * Create a completely new attempt.
         */
        PaymentAttempt attempt =
                PaymentAttempt.builder()
                        .payment(payment)
                        .amount(payment.getAmount())
                        .currency(payment.getCurrency().name())
                        .status(PaymentStatus.INITIATING)
                        .initiatedAt(LocalDateTime.now())
                        .build();

        payment.addAttempt(attempt);

        payment.setStatus(
                PaymentStatus.INITIATING
        );

        paymentAttemptRepository.save(attempt);

        try {

            RazorpayOrderResponse razorpayOrder =
                    razorpayService.createOrder(
                            payment.getAmount(),
                            payment.getCurrency().name(),
                            order.getOrderNumber()
                                    + "-R"
                                    + attempt.getId()
                    );

            attempt.setRazorpayOrderId(
                    razorpayOrder.getRazorpayOrderId()
            );

            attempt.setStatus(
                    PaymentStatus.INITIATED
            );

            payment.setStatus(
                    PaymentStatus.INITIATED
            );

            paymentAttemptRepository.save(attempt);
            paymentRepository.save(payment);

            return PaymentInitiationResponseDTO.builder()
                    .orderId(order.getId())
                    .orderNumber(order.getOrderNumber())
                    .paymentAttemptId(attempt.getId())
                    .razorpayOrderId(
                            razorpayOrder.getRazorpayOrderId()
                    )
                    .amount(
                            razorpayOrder.getAmount()
                    )
                    .currency(
                            razorpayOrder.getCurrency()
                    )
                    .keyId(
                            razorpayProperties.getKeyId()
                    )
                    .build();

        } catch (RazorpayException e) {

            attempt.setStatus(
                    PaymentStatus.FAILED
            );

            attempt.setFailedAt(
                    LocalDateTime.now()
            );

            attempt.setFailureMessage(
                    e.getMessage()
            );

            payment.setStatus(
                    PaymentStatus.FAILED
            );

            paymentAttemptRepository.save(attempt);
            paymentRepository.save(payment);

            throw new PaymentGatewayException(
                    "Unable to retry payment."
            );
        }
    }
    private Order getUserOrder(Long orderId) {

        String email =
                SecurityUtils.getCurrentUserMail();

        User user =
                userRepo.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Current user not found."
                                )
                        );

        return orderRepository
                .findByIdAndUser_Id(
                        orderId,
                        user.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found."
                        )
                );
    }
    @Override
    @Transactional
    public void cancelPaymentAttempt(
            Long orderId,
            Long attemptId
    ) {

        Order order = getUserOrder(orderId);

        Payment payment =
                paymentRepository.findByOrder_Id(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found."
                                )
                        );

        PaymentAttempt attempt =
                paymentAttemptRepository
                        .findByIdAndPayment_Id(
                                attemptId,
                                payment.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment attempt not found."
                                )
                        );

        /*
         * Never cancel an already completed payment.
         */
        if (attempt.getStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException(
                    "Successful payment cannot be cancelled."
            );
        }

        /*
         * Cancellation is meaningful only for an
         * active attempt.
         */
        if (attempt.getStatus() != PaymentStatus.INITIATING &&
                attempt.getStatus() != PaymentStatus.INITIATED) {

            return;
        }

        attempt.setStatus(
                PaymentStatus.CANCELLED
        );

        payment.setStatus(
                PaymentStatus.CANCELLED
        );

        paymentAttemptRepository.save(attempt);
        paymentRepository.save(payment);
    }
}
