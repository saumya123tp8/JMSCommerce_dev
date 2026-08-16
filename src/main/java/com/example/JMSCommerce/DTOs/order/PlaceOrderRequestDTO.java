package com.example.JMSCommerce.DTOs.order;

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
}
