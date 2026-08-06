package com.example.JMSCommerce.DTOs.product.variants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVariantAttributeRequestDTO {

    @NotNull
    private Long specificationDefinitionId;

    @NotBlank
    private String value;
}
