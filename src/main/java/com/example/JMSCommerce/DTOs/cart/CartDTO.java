package com.example.JMSCommerce.DTOs.cart;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    /**
     * Logged-in user or Guest
     */
    private String ownerId;

    /**
     * Cart Items
     */
    @Builder.Default
    private List<CartItemDTO> items = new ArrayList<>();

}
