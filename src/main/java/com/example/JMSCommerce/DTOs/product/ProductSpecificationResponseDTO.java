package com.example.JMSCommerce.DTOs.product;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecificationResponseDTO {

    private Long specificationId;

    private String name;

    private String displayName;

    private String value;

    private String unit;
}