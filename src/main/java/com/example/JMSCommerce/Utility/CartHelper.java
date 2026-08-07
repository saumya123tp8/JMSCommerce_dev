package com.example.JMSCommerce.Utility;

import com.example.JMSCommerce.DTOs.cart.AddCartItemRequestDTO;
import com.example.JMSCommerce.DTOs.cart.CartDTO;
import com.example.JMSCommerce.DTOs.cart.CartItemDTO;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CartHelper {
    public Optional<CartItemDTO> findMatchingItem(
            CartDTO cart,
            AddCartItemRequestDTO request
    ) {

        Set<Long> requested =
                new HashSet<>(
                        request.getCustomizationOptionIds()
                );

        return cart.getItems()
                .stream()
                .filter(item ->
                        item.getVariantId().equals(
                                request.getVariantId()
                        )
                )
                .filter(item ->

                        new HashSet<>(
                                item.getCustomizationOptionIds()
                        ).equals(requested)

                )
                .findFirst();

    }
}
