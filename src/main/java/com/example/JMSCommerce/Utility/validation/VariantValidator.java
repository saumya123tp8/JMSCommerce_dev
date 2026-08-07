package com.example.JMSCommerce.Utility.validation;

import com.example.JMSCommerce.DTOs.product.variants.CreateVariantAttributeRequestDTO;
import com.example.JMSCommerce.DTOs.product.variants.CreateVariantRequestDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.ProductVariant;
import com.example.JMSCommerce.Repositories.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class VariantValidator {

    private final ProductVariantRepository variantRepository;

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

    /// / cart

    public ProductVariant validateAndGet(
            Long variantId
    ) {

        ProductVariant variant = variantRepository
                .findById(variantId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Variant not found."
                        ));

        if (!Boolean.TRUE.equals(variant.getActive())) {
            throw new BadRequestException(
                    "Variant is inactive."
            );
        }

        return variant;

    }

}
