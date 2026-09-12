package com.example.JMSCommerce.Model;

//@Component
//public class CustomizationValidator {
//
//    public void validate(CreateCustomizationRequestDTO request) {
//
//        if (request == null || request.getGroups() == null || request.getGroups().isEmpty()) {
//            throw new BadRequestException("At least one customization group is required.");
//        }
//
//        // validate each group
//    }
//
//}

import com.example.JMSCommerce.DTOs.customization.CreateCustomizationRequestDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Repositories.CustomizationGroupRepository;
import com.example.JMSCommerce.Repositories.CustomizationOptionRepository;
import com.example.JMSCommerce.Utility.enums.SelectionType;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CustomizationValidator {

    private final CustomizationGroupRepository customizationGroupRepository;
    private final CustomizationOptionRepository customizationOptionRepository;

    public CustomizationValidator(CustomizationGroupRepository customizationGroupRepository, CustomizationOptionRepository customizationOptionRepository) {

        this.customizationGroupRepository = customizationGroupRepository;
        this.customizationOptionRepository = customizationOptionRepository;
    }

    // Admin side
    public void validateDefinition(CreateCustomizationRequestDTO request) {
        if (request == null || request.getGroups() == null || request.getGroups().isEmpty()) {
            throw new BadRequestException("At least one customization group is required.");
        }

        // validate each group
    }

    // Customer side (Cart)
    public void validateSelection(
            ProductVariant variant,
            List<Long> optionIds
    ) {
        validateDuplicates(optionIds);
        validateMandotoryCustomizationPresent(variant.getProduct(),optionIds);
        if(optionIds!=null) {
            List<CustomizationOption> selectedOptions = customizationOptionRepository.findAllById(optionIds);
            validateExistence(optionIds);
            validateOwnership(
                    variant.getProduct(),
                    selectedOptions
            );
            validateGroupRules(
                    variant.getProduct(),
                    selectedOptions
            );
        }

    }

    private void validateMandotoryCustomizationPresent(Product product, List<Long> optionIds) {
        List<CustomizationGroup> mandatoryCustomizations = customizationGroupRepository.findByProduct_IdAndRequired(product.getId(),true);
        List<Long> listAvailableCustomization = new ArrayList<>();
        if(optionIds!=null){
            listAvailableCustomization = customizationOptionRepository.findDistinctGroupIdsByOptionIds(optionIds);
        }
        for (CustomizationGroup group : mandatoryCustomizations) {
            if (!listAvailableCustomization.contains(group.getId())) {
                throw new BadRequestException(
                        "Customization group '" + group.getName() + "' is required."
                );
            }
        }
    }

    private void validateGroupRules(Product product, List<CustomizationOption> selectedOptions) {

         Map<CustomizationGroup, List<CustomizationOption>> selectedByGroup =
                selectedOptions.stream()
                        .collect(
                                Collectors.groupingBy(
                                        CustomizationOption::getCustomizationGroup
                                )
                        );

        for (CustomizationGroup group : product.getCustomizationGroups()) {
            List<CustomizationOption> selected =
                    selectedByGroup.getOrDefault(
                            group,
                            Collections.emptyList()
                    );

            int count = selected.size();
            if (group.getRequired() && count == 0) {

                throw new BadRequestException(
                        group.getName() + " is required."
                );

            }
            if (count < group.getMinSelection()) {

                throw new BadRequestException(
                        "Minimum " +
                                group.getMinSelection() +
                                " option(s) required for " +
                                group.getName()
                );

            }
            if (count > group.getMaxSelection()) {

                throw new BadRequestException(
                        "Maximum " +
                                group.getMaxSelection() +
                                " option(s) allowed for " +
                                group.getName()
                );

            }
            if (group.getSelectionType() == SelectionType.SINGLE
                    && count > 1) {

                throw new BadRequestException(
                        group.getName() +
                                " allows only one selection."
                );

            }

        }

    }

    private List<CustomizationOption> validateExistence(
            List<Long> optionIds
    ) {

        List<CustomizationOption> options =
                customizationOptionRepository.findAllById(optionIds);

        if (options.size() != optionIds.size()) {

            throw new ResourceNotFoundException(
                    "One or more customization options do not exist."
            );

        }

        return options;

    }
    private void validateDuplicates(
            List<Long> optionIds
    ) {
        if(optionIds==null)return ; // but need to check if there is mandatory customization
                                    // then without that we can not add this evn at time of creation
        if (new HashSet<>(optionIds).size() != optionIds.size()) {

            throw new BadRequestException(
                    "Duplicate customization options selected."
            );

        }

    }
    private void validateOwnership(
            Product product,
            List<CustomizationOption> options
    ) {

        boolean invalid =
                options.stream()
                        .anyMatch(option ->

                                !option
                                        .getCustomizationGroup()
                                        .getProduct()
                                        .getId()
                                        .equals(product.getId())

                        );

        if (invalid) {

            throw new BadRequestException(
                    "Customization does not belong to this product."
            );

        }

    }


}
