package com.example.JMSCommerce.DTOs.specificationDefinition;


import com.example.JMSCommerce.Utility.enums.SpecificationDataType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecificationDefinitionResponseDTO {

    private Long id;

    private String name;

    private String displayName;

    private String description;

    private SpecificationDataType dataType;

    private String unit;

    private Boolean required;

    private Boolean filterable;

    private Boolean searchable;

    private Integer displayOrder;

    private String placeholder;

    private String defaultValue;

    private Long categoryId;

    private String categoryName;
}