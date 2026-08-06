package com.example.JMSCommerce.Model;

import com.example.JMSCommerce.DTOs.customization.CreateCustomizationRequestDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class CustomizationValidator {

    public void validate(CreateCustomizationRequestDTO request) {

        if (request == null || request.getGroups() == null || request.getGroups().isEmpty()) {
            throw new BadRequestException("At least one customization group is required.");
        }

        // validate each group
    }

}
