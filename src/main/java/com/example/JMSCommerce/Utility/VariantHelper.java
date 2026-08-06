package com.example.JMSCommerce.Utility;

import com.example.JMSCommerce.DTOs.product.variants.CreateVariantAttributeRequestDTO;
import com.example.JMSCommerce.DTOs.product.variants.CreateVariantRequestDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Model.ProductVariant;
import com.example.JMSCommerce.Model.SpecificationDefinition;
import com.example.JMSCommerce.Model.VariantAttribute;
import com.example.JMSCommerce.Repositories.ProductVariantRepository;
import com.example.JMSCommerce.Repositories.SpecificationDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class VariantHelper {
    private final ProductVariantRepository variantRepository;
    private final SpecificationDefinitionRepository specificationRepository;
    public ProductVariant getVariantOrThrow(
            ProductVariantRepository repository,
            Long productId,
            Long variantId
    ) {

        return repository.findByIdAndProduct_Id(
                        variantId,
                        productId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Variant not found."
                        )
                );

    }


    public void validateUniqueSku(

            Long currentVariantId,  String sku
    ) {

        if (sku == null || sku.isBlank()) {
            return;
        }
//       ProductVariant productVariant = variantRepository.findBySku(sku).orElseThrow(
//               ()->   new BadRequestException("SKU not exists.")
//       );
        variantRepository.findBySku(sku)
                .ifPresent(existing -> {


                    if (currentVariantId == null ||
                            !existing.getId().equals(currentVariantId)) {

                        throw new BadRequestException(
                                "SKU already exists."
                        );
                    }

                });
    }

    public void validateUniqueBarcode(
            Long currentVariantId, String barcode
    ) {

        if (barcode == null || barcode.isBlank()) {
            return;
        }

        variantRepository.findByBarcode(barcode)
                .ifPresent(existing -> {

                    if (currentVariantId == null ||
                            !existing.getId().equals(currentVariantId)) {

                        throw new BadRequestException(
                                "Barcode already exists."
                        );
                    }

                });
    }

    public boolean sameAttributes(

            ProductVariant existing,
            ProductVariant incoming
    ) {

        if (existing.getAttributes().size()
                != incoming.getAttributes().size()) {
            return false;
        }

        Map<Long, String> existingMap =
                existing.getAttributes()
                        .stream()
                        .collect(Collectors.toMap(
                                attribute -> attribute
                                        .getSpecificationDefinition()
                                        .getId(),
                                VariantAttribute::getValue
                        ));

        Map<Long, String> incomingMap =
                incoming.getAttributes()
                        .stream()
                        .collect(Collectors.toMap(
                                attribute -> attribute
                                        .getSpecificationDefinition()
                                        .getId(),
                                a -> a.getValue().trim().toLowerCase()
                        ));

        return existingMap.equals(incomingMap);

    }

    public void validateDuplicateVariant(
            Long currentVariantId,
            Product product,
            ProductVariant incoming
    ) {

        boolean duplicate = product.getVariants()
                .stream()
                .filter(existing ->
                        currentVariantId == null ||
                                !existing.getId().equals(currentVariantId)
                )
                .anyMatch(existing ->
                        sameAttributes(existing, incoming)
                );

        if (duplicate) {
            throw new BadRequestException(
                    "Variant with the same attribute combination already exists."
            );
        }
    }

    public List<SpecificationDefinition> loadSpecifications(
            CreateVariantRequestDTO request
    ) {

        List<Long> ids =
                request.getAttributes()
                        .stream()
                        .map(CreateVariantAttributeRequestDTO::
                                getSpecificationDefinitionId)
                        .toList();

        List<SpecificationDefinition> definitions =
                specificationRepository.findAllById(ids);

        if (definitions.size() != ids.size()) {
            throw new ResourceNotFoundException(
                    "One or more specifications not found."
            );
        }

        return definitions;

    }

}
