package com.example.JMSCommerce.DTOs.product.variants;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantAttributeResponseDTO {

    private Long specificationDefinitionId;

    private String specificationName;

    private String value;
}
