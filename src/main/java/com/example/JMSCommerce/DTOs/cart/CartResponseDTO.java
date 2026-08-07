package com.example.JMSCommerce.DTOs.cart;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {

    private List<CartItemResponseDTO> items;

    private BigDecimal subtotal;

    private Integer totalQuantity;

    private BigDecimal discount;

    private BigDecimal tax;

    private BigDecimal deliveryCharge;
    private BigDecimal grandTotal;

}
