package com.example.JMSCommerce.Utility.validation;

import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Utility.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class ValidateStatusTransition {

    public void validateStatus(
            OrderStatus current,
            OrderStatus next
    ) {

        if (current == next) {
            throw new BadRequestException(
                    "Order is already in " + current + " status."
            );
        }

        boolean valid = switch (current) {

            case PENDING ->
                    next == OrderStatus.CONFIRMED ||
                            next == OrderStatus.CANCELLED;

            case CONFIRMED ->
                    next == OrderStatus.PROCESSING ||
                            next == OrderStatus.CANCELLED;

            case PROCESSING ->
                    next == OrderStatus.SHIPPED;

            case SHIPPED ->
                    next == OrderStatus.DELIVERED;

            case DELIVERED, CANCELLED ->
                    false;
        };

        if (!valid) {
            throw new BadRequestException(
                    "Invalid order status transition: "
                            + current + " -> " + next
            );
        }
    }
}
