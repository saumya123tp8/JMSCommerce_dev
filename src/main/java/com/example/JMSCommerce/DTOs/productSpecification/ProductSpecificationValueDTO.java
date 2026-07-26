package com.example.JMSCommerce.DTOs.productSpecification;

import com.example.JMSCommerce.Model.ProductSpecificationValue;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductSpecificationValueDTO {

    @NotNull
    private Long specificationId;

    @NotBlank
    private String value;
}
