package com.example.JMSCommerce.Services.cart;

import com.example.JMSCommerce.DTOs.cart.AddCartItemRequestDTO;
import com.example.JMSCommerce.DTOs.cart.CartResponseDTO;
import com.example.JMSCommerce.DTOs.cart.UpdateCartItemRequestDTO;

public interface CartService {

    CartResponseDTO addItem(
            String ownerId,
            AddCartItemRequestDTO request
    );

    CartResponseDTO getCart(
            String ownerId
    );

    CartResponseDTO updateItem(
            String ownerId,
            String cartItemId,
            UpdateCartItemRequestDTO request
    );

    CartResponseDTO removeItem(
            String ownerId,
            String cartItemId
    );

    void clearCart(
            String ownerId
    );

}
