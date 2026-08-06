package com.example.JMSCommerce.DTOs.product.variants;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantResponseDTO {

    private Long id;

    private String displayName;

    private BigDecimal mrp;

    private BigDecimal sellingPrice;

    private Integer stock;

    private String sku;

    private String barcode;

    private Boolean active;

    private List<VariantAttributeResponseDTO> attributes;
}
