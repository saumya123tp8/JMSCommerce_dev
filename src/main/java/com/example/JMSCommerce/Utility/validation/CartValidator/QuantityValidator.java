package com.example.JMSCommerce.Utility.validation.CartValidator;

import com.example.JMSCommerce.Exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class QuantityValidator {

    public void validate(
            Integer quantity
    ) {

        if (quantity == null || quantity <= 0) {
            throw new BadRequestException(
                    "Quantity must be greater than zero."
            );
        }

    }

}
