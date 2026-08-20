package com.example.JMSCommerce.Utility.validation.CartValidator;

import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Model.ProductVariant;
import com.example.JMSCommerce.Services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockValidator {

    private final InventoryService inventoryService;
    public void validate(
            ProductVariant variant,
            Integer quantity
    ) {

        if (variant.getStock() < quantity) {

            throw new BadRequestException(
                    "Insufficient stock."
            );

        }
//        inventoryService.reserve(
//                variant.getId(),
//                quantity
//        );

    }

}
