package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.Adapters.CustomizationAdapter;
import com.example.JMSCommerce.DTOs.customization.CreateCustomizationRequestDTO;
import com.example.JMSCommerce.DTOs.customization.CustomizationResponseDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Model.CustomizationGroup;
import com.example.JMSCommerce.Model.CustomizationValidator;
import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Repositories.ProductRepo;
import com.example.JMSCommerce.Utility.ProductHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CustomizationServiceImpl
        implements CustomizationService {

    private final CustomizationValidator customizationValidator;
    private final ProductRepo productRepository;
    private final ProductHelper productHelper;

    private final CustomizationAdapter customizationAdapter;

    @Override
    public CustomizationResponseDTO createCustomizations(
            Long productId,
            CreateCustomizationRequestDTO request
    ) {

        Product product = productHelper.getActiveProductOrThrow(
                productRepository,
                productId
        );

        if (!product.getCustomizationGroups().isEmpty()) {
            throw new BadRequestException(
                    "Customizations already exist for this product."
            );
        }

        customizationValidator.validateDefinition(request);

        List<CustomizationGroup> groups =
                customizationAdapter.toEntities(request);

        groups.forEach(product::addCustomizationGroup);

        Product savedProduct = productRepository.save(product);

        return customizationAdapter.toCustomizationResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomizationResponseDTO getCustomizations(
            Long productId
    ) {

        Product product = productHelper.getActiveProductOrThrow(
                productRepository,
                productId
        );

        return customizationAdapter.toCustomizationResponse(product);
    }

    @Override
    public CustomizationResponseDTO updateCustomizations(
            Long productId,
            CreateCustomizationRequestDTO request
    ) {

        // 1. Validate request
        customizationValidator.validateDefinition(request);

        // 2. Load Product
        Product product = productHelper.getProductOrThrow(
                productRepository,
                productId
        );

        // 3. Remove existing customization groups
        product.getCustomizationGroups().clear();

        // 4. Convert request to entities
        List<CustomizationGroup> groups =
                customizationAdapter.toEntities(request);

        // 5. Attach new groups
        groups.forEach(product::addCustomizationGroup);

        // 6. Save
        Product savedProduct =
                productRepository.save(product);

        // 7. Return response
        return customizationAdapter.toCustomizationResponse(
                savedProduct
        );
    }
}
