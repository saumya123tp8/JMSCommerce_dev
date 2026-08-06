package com.example.JMSCommerce.Adapters;

import com.example.JMSCommerce.DTOs.customization.*;
import com.example.JMSCommerce.Model.CustomizationGroup;
import com.example.JMSCommerce.Model.CustomizationOption;
import com.example.JMSCommerce.Model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomizationAdapter {

    public List<CustomizationGroup> toEntities(
            CreateCustomizationRequestDTO request
    ) {
        return request.getGroups()
                .stream()
                .map(this::toCustomizationGroup)
                .toList();
    }

    public CustomizationResponseDTO toCustomizationResponse(
            Product product
    ) {
        return CustomizationResponseDTO.builder()
                .productId(product.getId())
                .groups(
                        product.getCustomizationGroups()
                                .stream()
                                .map(this::toCustomizationGroupResponse)
                                .toList()
                )
                .build();
    }

    /* ------------------------ Request -> Entity ------------------------ */

    private CustomizationGroup toCustomizationGroup(
            CreateCustomizationGroupRequestDTO dto
    ) {

        CustomizationGroup group = CustomizationGroup.builder()
                .name(dto.getName())
                .selectionType(dto.getSelectionType())
                .required(dto.getRequired())
                .minSelection(dto.getMinSelection())
                .maxSelection(dto.getMaxSelection())
                .displayOrder(dto.getDisplayOrder())
                .build();

        dto.getOptions()
                .stream()
                .map(this::toCustomizationOption)
                .forEach(group::addOption);

        return group;
    }

    private CustomizationOption toCustomizationOption(
            CreateCustomizationOptionRequestDTO dto
    ) {

        return CustomizationOption.builder()
                .name(dto.getName())
                .adjustmentType(dto.getAdjustmentType())
                .adjustmentValue(dto.getAdjustmentValue())
                .displayOrder(dto.getDisplayOrder())
                .build();
    }

    /* ------------------------ Entity -> Response ------------------------ */

    private CustomizationGroupResponseDTO toCustomizationGroupResponse(
            CustomizationGroup group
    ) {

        return CustomizationGroupResponseDTO.builder()
                .id(group.getId())
                .name(group.getName())
                .selectionType(group.getSelectionType())
                .required(group.getRequired())
                .minSelection(group.getMinSelection())
                .maxSelection(group.getMaxSelection())
                .displayOrder(group.getDisplayOrder())
                .active(group.getActive())
                .options(
                        group.getOptions()
                                .stream()
                                .map(this::toCustomizationOptionResponse)
                                .toList()
                )
                .build();
    }

    private CustomizationOptionResponseDTO toCustomizationOptionResponse(
            CustomizationOption option
    ) {

        return CustomizationOptionResponseDTO.builder()
                .id(option.getId())
                .name(option.getName())
                .adjustmentType(option.getAdjustmentType())
                .adjustmentValue(option.getAdjustmentValue())
                .displayOrder(option.getDisplayOrder())
                .active(option.getActive())
                .build();
    }

}