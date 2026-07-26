package com.example.JMSCommerce.DTOs.specificationDefinition;

import com.example.JMSCommerce.Utility.enums.SpecificationDataType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSpecificationDefinitionRequestDTO {

    @NotBlank(message = "Specification name is required.")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Display name is required.")
    @Size(max = 150)
    private String displayName;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Data type is required.")
    private SpecificationDataType dataType;

    @Size(max = 30)
    private String unit;

    @NotNull
    private Boolean required;

    @NotNull
    private Boolean filterable;

    @NotNull
    private Boolean searchable;

    @NotNull
    private Integer displayOrder;

    @Size(max = 100)
    private String placeholder;

    @Size(max = 255)
    private String defaultValue;

    @NotNull(message = "Category is required.")
    private Long categoryId;
}
