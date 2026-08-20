package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.DTOs.payment.RazorpayOrderResponse;
import com.example.JMSCommerce.config.RazorpayProperties;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RazorpayServiceImpl implements RazorpayService {

    private final RazorpayClient razorpayClient;
    private final RazorpayProperties razorpayProperties;

    @Override
    public RazorpayOrderResponse createOrder(
            BigDecimal amount,
            String currency,
            String receipt
    ) throws RazorpayException {

        long amountInPaise =
                amount
                        .multiply(BigDecimal.valueOf(100))
                        .longValueExact();

        JSONObject options = new JSONObject();

        options.put("amount", amountInPaise);
        options.put("currency", currency);
        options.put("receipt", receipt);

        System.out.println("Razorpay options: " + options);

        Order razorpayOrder =
                razorpayClient.orders.create(options);

        return RazorpayOrderResponse.builder()
                .razorpayOrderId(
                        razorpayOrder.get("id")
                )
                .amount(
                        ((Number) razorpayOrder.get("amount")).longValue()
                )
                .currency(
                        razorpayOrder.get("currency")
                )
                .status(
                        razorpayOrder.get("status")
                )
                .build();
    }
    @Override
    public boolean verifyPaymentSignature(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature
    ) {

        try {

            JSONObject attributes = new JSONObject();

            attributes.put(
                    "razorpay_order_id",
                    razorpayOrderId
            );

            attributes.put(
                    "razorpay_payment_id",
                    razorpayPaymentId
            );

            attributes.put(
                    "razorpay_signature",
                    razorpaySignature
            );

            Utils.verifyPaymentSignature(
                    attributes,
                    razorpayProperties.getKeySecret()
            );

            return true;

        } catch (RazorpayException e) {

            return false;
        }
    }
    @Override
    public boolean verifyWebhookSignature(
            String payload,
            String signature
    ) {

        try {

            Utils.verifyWebhookSignature(
                    payload,
                    signature,
                    razorpayProperties.getWebhookSecret()
            );

            return true;

        } catch (RazorpayException e) {

            return false;
        }
    }
}