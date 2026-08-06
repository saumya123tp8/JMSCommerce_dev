package com.example.JMSCommerce.DTOs.product.variants;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVariantRequestDTO {

    @NotNull
    @Positive
    private BigDecimal mrp;

    @NotNull
    @Positive
    private BigDecimal sellingPrice;

    @NotNull
    @PositiveOrZero
    private Integer stock;

    @Size(max = 100)
    private String sku;

    @Size(max = 100)
    private String barcode;

    @NotEmpty
    @Valid
    private List<CreateVariantAttributeRequestDTO> attributes;
}
