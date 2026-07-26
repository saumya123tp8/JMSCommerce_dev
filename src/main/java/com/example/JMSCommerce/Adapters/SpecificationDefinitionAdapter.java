package com.example.JMSCommerce.Adapters;

import com.example.JMSCommerce.DTOs.specificationDefinition.CreateSpecificationDefinitionRequestDTO;
import com.example.JMSCommerce.DTOs.specificationDefinition.SpecificationDefinitionResponseDTO;
import com.example.JMSCommerce.DTOs.specificationDefinition.UpdateSpecificationDefinitionRequestDTO;
import com.example.JMSCommerce.Model.SpecificationDefinition;
import org.springframework.stereotype.Component;

@Component
public class SpecificationDefinitionAdapter {

    public SpecificationDefinition toEntity(
            CreateSpecificationDefinitionRequestDTO dto
    ) {
          return SpecificationDefinition.builder()
                  .description(dto.getDescription())
                  .name(dto.getName())
                  .dataType(dto.getDataType())
                  .defaultValue(dto.getDefaultValue())
                  .displayName(dto.getDisplayName())
                  .unit(dto.getUnit())
                  .displayOrder(dto.getDisplayOrder())
                  .filterable(dto.getFilterable())
                  .placeholder(dto.getPlaceholder())
                  .required(dto.getRequired())
                  .searchable(dto.getSearchable())
                  .build();
    }

    public SpecificationDefinitionResponseDTO toDTO(
            SpecificationDefinition entity
    ) {
        return SpecificationDefinitionResponseDTO.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .name(entity.getName())
                .dataType(entity.getDataType())
                .defaultValue(entity.getDefaultValue())
                .displayName(entity.getDisplayName())
                .unit(entity.getUnit())
                .displayOrder(entity.getDisplayOrder())
                .filterable(entity.getFilterable())
                .placeholder(entity.getPlaceholder())
                .required(entity.getRequired())
                .searchable(entity.getSearchable())
                .categoryName(entity.getCategory().getName())
                .categoryId(entity.getCategory().getId())
                .build();
    }

    public void updateEntity(
            SpecificationDefinition entity,
            UpdateSpecificationDefinitionRequestDTO dto
    ) {

    }

}
