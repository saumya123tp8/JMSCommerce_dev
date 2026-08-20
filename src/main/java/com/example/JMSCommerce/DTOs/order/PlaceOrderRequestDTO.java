package com.example.JMSCommerce.DTOs.order;

import com.example.JMSCommerce.Utility.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceOrderRequestDTO {

    @NotNull(message = "Delivery address is required.")
    private Long addressId;

    @NotNull(message = "Payment method is required.")
    private PaymentMethod paymentMethod;
}
