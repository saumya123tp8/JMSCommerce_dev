package com.example.JMSCommerce.Utility.validation;

import com.example.JMSCommerce.DTOs.product.variants.CreateVariantAttributeRequestDTO;
import com.example.JMSCommerce.DTOs.product.variants.CreateVariantRequestDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class VariantValidator {

    public void validate(CreateVariantRequestDTO request) {

        validatePrice(request);

        validateAttributes(request);

        validateDuplicateSpecification(request);

    }

    private void validatePrice(
            CreateVariantRequestDTO request
    ) {

        if (request.getSellingPrice()
                .compareTo(request.getMrp()) > 0) {

            throw new BadRequestException(
                    "Selling price cannot be greater than MRP."
            );

        }

    }

    private void validateAttributes(
            CreateVariantRequestDTO request
    ) {

        if (request.getAttributes() == null
                || request.getAttributes().isEmpty()) {

            throw new BadRequestException(
                    "At least one attribute is required."
            );

        }

    }

    private void validateDuplicateSpecification(
            CreateVariantRequestDTO request
    ) {

        Set<Long> specificationIds = new HashSet<>();

        for (CreateVariantAttributeRequestDTO attribute
                : request.getAttributes()) {

            if (!specificationIds.add(
                    attribute.getSpecificationDefinitionId()
            )) {

                throw new BadRequestException(
                        "Duplicate specification in variant."
                );

            }

        }

    }

}
