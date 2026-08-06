package com.example.JMSCommerce.Adapters;

import com.example.JMSCommerce.DTOs.product.variants.CreateVariantAttributeRequestDTO;
import com.example.JMSCommerce.DTOs.product.variants.CreateVariantRequestDTO;
import com.example.JMSCommerce.DTOs.product.variants.VariantAttributeResponseDTO;
import com.example.JMSCommerce.DTOs.product.variants.VariantResponseDTO;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.ProductVariant;
import com.example.JMSCommerce.Model.SpecificationDefinition;
import com.example.JMSCommerce.Model.VariantAttribute;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class VariantAdapter {

    /* ===========================
            Request -> Entity
       =========================== */

    public ProductVariant toEntity(
            CreateVariantRequestDTO request,
            List<SpecificationDefinition> definitions
    ) {

        ProductVariant variant = ProductVariant.builder()
                .mrp(request.getMrp())
                .sellingPrice(request.getSellingPrice())
                .stock(request.getStock())
                .sku(request.getSku())
                .barcode(request.getBarcode())
                .active(true)
                .build();

        for (CreateVariantAttributeRequestDTO dto : request.getAttributes()) {

//            SpecificationDefinition definition =
//                    definitions.stream()
//                            .filter(d ->
//                                    d.getId().equals(
//                                            dto.getSpecificationDefinitionId()
//                                    )
//                            )
//                            .findFirst()
//                            .orElseThrow();
            Map<Long, SpecificationDefinition> definitionMap =
                    definitions.stream()
                            .collect(Collectors.toMap(
                                    SpecificationDefinition::getId,
                                    Function.identity()
                            ));
            SpecificationDefinition definition =
                    definitionMap.get(dto.getSpecificationDefinitionId());

            if (definition == null) {
                throw new ResourceNotFoundException(
                        "Specification not found with id: " +
                                dto.getSpecificationDefinitionId()
                );
            }
            VariantAttribute attribute =
                    VariantAttribute.builder()
                            .specificationDefinition(definition)
                            .value(dto.getValue())
                            .build();

            variant.addAttribute(attribute);
        }

        variant.setDisplayName(
                generateDisplayName(variant)
        );

        return variant;
    }

    /* ===========================
            Entity -> Response
       =========================== */

    public VariantResponseDTO toResponse(
            ProductVariant variant
    ) {

        return VariantResponseDTO.builder()
                .id(variant.getId())
                .displayName(variant.getDisplayName())
                .mrp(variant.getMrp())
                .sellingPrice(variant.getSellingPrice())
                .stock(variant.getStock())
                .sku(variant.getSku())
                .barcode(variant.getBarcode())
                .active(variant.getActive())
                .attributes(
//                        variant.getAttributes().stream().map(c)
                        variant.getAttributes()
                                .stream()
                                .map(this::toAttributeResponse)
                                .toList()
                )
                .build();
    }

    /* ===========================
             Helpers
       =========================== */

    private VariantAttributeResponseDTO toAttributeResponse(
            VariantAttribute attribute
    ) {

        return VariantAttributeResponseDTO.builder()
                .specificationDefinitionId(
                        attribute.getSpecificationDefinition().getId()
                )
                .specificationName(
                        attribute.getSpecificationDefinition().getDisplayName()
                )
                .value(attribute.getValue())
                .build();
    }

    private String generateDisplayName(
            ProductVariant variant
    ) {

        return variant.getAttributes()
                .stream()
                .map(VariantAttribute::getValue)
                .collect(Collectors.joining(" / "));
    }

}