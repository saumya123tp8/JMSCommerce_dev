package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.Adapters.SpecificationDefinitionAdapter;
import com.example.JMSCommerce.DTOs.specificationDefinition.CreateSpecificationDefinitionRequestDTO;
import com.example.JMSCommerce.DTOs.specificationDefinition.SpecificationDefinitionResponseDTO;
import com.example.JMSCommerce.DTOs.specificationDefinition.UpdateSpecificationDefinitionRequestDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.Category;
import com.example.JMSCommerce.Model.SpecificationDefinition;
import com.example.JMSCommerce.Repositories.CategoryRepo;
import com.example.JMSCommerce.Repositories.SpecificationDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SpecificationDefinitionService {

    private final SpecificationDefinitionRepository specificationRepository;
    private final CategoryRepo categoryRepository;
    private final SpecificationDefinitionAdapter specificationAdapter;

    public SpecificationDefinitionResponseDTO createSpecification(
            CreateSpecificationDefinitionRequestDTO request
    ) {

        Category category = getCategory(request.getCategoryId());

        String normalizedName = normalizeName(request.getName());

        validateDuplicateSpecification(normalizedName, category);

        request.setName(normalizedName);

        SpecificationDefinition specification =
                specificationAdapter.toEntity(request);

        specification.setCategory(category);

        specification = specificationRepository.save(specification);

        return specificationAdapter.toDTO(specification);
    }


    public SpecificationDefinitionResponseDTO updateSpecification(
            Long specificationId,
            UpdateSpecificationDefinitionRequestDTO request
    ) {

        SpecificationDefinition specification =
                getSpecification(specificationId);

        Category category = getCategory(request.getCategoryId());

        String normalizedName = normalizeName(request.getName());

        validateDuplicateSpecification(
                specificationId,
                normalizedName,
                category
        );

        request.setName(normalizedName);

        specificationAdapter.updateEntity(
                specification,
                request
        );

        specification.setCategory(category);

        specification = specificationRepository.save(specification);

        return specificationAdapter.toDTO(specification);
    }


    @Transactional(readOnly = true)
    public SpecificationDefinitionResponseDTO getSpecificationById(
            Long specificationId
    ) {

        return specificationAdapter.toDTO(
                getSpecification(specificationId)
        );
    }


    @Transactional(readOnly = true)
    public List<SpecificationDefinitionResponseDTO> getAllSpecifications() {

        return specificationRepository.findAll()
                .stream()
                .map(specificationAdapter::toDTO)
                .toList();
    }


    public void deleteSpecification(
            Long specificationId
    ) {

        SpecificationDefinition specification =
                getSpecification(specificationId);

        specificationRepository.delete(specification);
    }


    // ---------- Helper Methods ----------

    private SpecificationDefinition getSpecification(Long id) {

        return specificationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Specification not found with id : " + id
                        )
                );
    }

    private Category getCategory(Long categoryId) {

        return categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id : " + categoryId
                        )
                );
    }

    private void validateDuplicateSpecification(
            String name,
            Category category
    ) {

        if (specificationRepository.existsByCategoryAndNameIgnoreCase(
                category,
                name
        )) {

            throw new BadRequestException(
                    "Specification '" + name +
                            "' already exists for category '" +
                            category.getName() + "'."
            );
        }
    }

    private void validateDuplicateSpecification(
            Long specificationId,
            String name,
            Category category
    ) {

        if (specificationRepository.existsByCategoryAndNameIgnoreCaseAndIdNot(
                category,
                name,
                specificationId
        )) {

            throw new BadRequestException(
                    "Specification '" + name +
                            "' already exists for category '" +
                            category.getName() + "'."
            );
        }
    }

    private String normalizeName(String name) {

        return name == null
                ? null
                : name.trim().replaceAll("\\s+", " ");
    }

}
