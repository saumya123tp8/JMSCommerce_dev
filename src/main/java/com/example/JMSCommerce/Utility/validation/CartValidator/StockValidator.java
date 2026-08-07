package com.example.JMSCommerce.Utility.validation.CartValidator;

import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Model.ProductVariant;
import org.springframework.stereotype.Component;

@Component
public class StockValidator {

    public void validate(
            ProductVariant variant,
            Integer quantity
    ) {

        if (variant.getStock() < quantity) {

            throw new BadRequestException(
                    "Insufficient stock."
            );

        }

    }

}
