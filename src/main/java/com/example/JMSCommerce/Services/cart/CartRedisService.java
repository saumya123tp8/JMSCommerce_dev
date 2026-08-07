package com.example.JMSCommerce.Services.cart;

import com.example.JMSCommerce.DTOs.cart.CartDTO;

public interface CartRedisService {

    CartDTO getCart(String ownerId);

    void saveCart(CartDTO cart);

    void deleteCart(String ownerId);



}
