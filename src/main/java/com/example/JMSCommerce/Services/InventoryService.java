package com.example.JMSCommerce.Services;

public interface InventoryService {

    void reserve(
            Long variantId,
            Integer quantity
    );

    void release(
            Long variantId,
            Integer quantity
    );
}
