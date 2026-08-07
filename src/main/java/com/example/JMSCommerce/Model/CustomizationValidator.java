package com.example.JMSCommerce.Model;

//@Component
//public class CustomizationValidator {
//
//    public void validate(CreateCustomizationRequestDTO request) {
//
//        if (request == null || request.getGroups() == null || request.getGroups().isEmpty()) {
//            throw new BadRequestException("At least one customization group is required.");
//        }
//
//        // validate each group
//    }
//
//}

import com.example.JMSCommerce.DTOs.customization.CreateCustomizationRequestDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomizationValidator {

    // Admin side
    public void validateDefinition(CreateCustomizationRequestDTO request) {
        if (request == null || request.getGroups() == null || request.getGroups().isEmpty()) {
            throw new BadRequestException("At least one customization group is required.");
        }

        // validate each group
    }

    // Customer side (Cart)
    public void validateSelection(
            Product product,
            List<Long> optionIds
    ) {

    }
}
