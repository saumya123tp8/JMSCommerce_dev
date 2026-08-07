package com.example.JMSCommerce.Services.cart;

import com.example.JMSCommerce.DTOs.cart.CartDTO;
import com.example.JMSCommerce.DTOs.cart.CartResponseDTO;

public interface CartAssembler {

    CartResponseDTO assemble(
            CartDTO cart
    );

}