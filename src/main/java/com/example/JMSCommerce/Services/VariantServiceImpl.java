package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.Adapters.VariantAdapter;
import com.example.JMSCommerce.DTOs.product.variants.CreateVariantRequestDTO;
import com.example.JMSCommerce.DTOs.product.variants.VariantResponseDTO;
import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Model.ProductVariant;
import com.example.JMSCommerce.Model.SpecificationDefinition;
import com.example.JMSCommerce.Repositories.ProductRepo;
import com.example.JMSCommerce.Repositories.ProductVariantRepository;
import com.example.JMSCommerce.Repositories.SpecificationDefinitionRepository;
import com.example.JMSCommerce.Utility.ProductHelper;
import com.example.JMSCommerce.Utility.VariantHelper;
import com.example.JMSCommerce.Utility.validation.VariantValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VariantServiceImpl
        implements VariantService {

    private final ProductRepo productRepository;
    private final ProductVariantRepository variantRepository;
    private final SpecificationDefinitionRepository specificationRepository;
    private final ProductHelper productHelper;
    private final VariantHelper variantHelper;
    private final VariantAdapter variantAdapter;
    private final VariantValidator variantValidator;
    private final ProductPriceSyncService productPriceSyncService;

    @Override
    public VariantResponseDTO createVariant(
            Long productId,
            CreateVariantRequestDTO request
    ) {

        // 1. Validate request
        variantValidator.validate(request);

        // 2. Get Product
        Product product =
                productHelper.getProductOrThrow(
                        productRepository,
                        productId
                );

        // 3. Load Specifications

        List<SpecificationDefinition> definitions =
                variantHelper.loadSpecifications(request);

        // 4. Convert DTO -> Entity

        ProductVariant variant =
                variantAdapter.toEntity(
                        request,
                        definitions
                );

//        // 5. Attach Product --- causing issue in validating sku ( because while search by sku
        // run query for insert toooooo
//
//        product.addVariant(variant);

        // 6. Business Validations

        variantHelper.validateUniqueSku(null,request.getSku());

        variantHelper.validateUniqueBarcode(null,request.getBarcode());

        variantHelper.validateDuplicateVariant(null,product, variant);

        // 5. Attach Product -- this may work perfectly

        product.addVariant(variant);

        // 8. Synchronize Product Prices

        productPriceSyncService.syncPrices(product);

        // 7. Save

//        Product saved =
        productRepository.save(product);

        // 9. Return Created Variant

//        ProductVariant created =
//                saved.getVariants()
//                        .getLast();

        return variantAdapter.toResponse(variant);
// I am thinking we should return product because we have this
    }

    @Override
    @Transactional(readOnly = true)
    public List<VariantResponseDTO> getVariants(
            Long productId
    ) {

        productHelper.getProductOrThrow(
                productRepository,
                productId
        );

        return variantRepository
                .findByProduct_Id(productId)
                .stream()
                .map(variantAdapter::toResponse)
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public VariantResponseDTO getVariant(
            Long productId,
            Long variantId
    ) {

        ProductVariant variant =
                variantHelper.getVariantOrThrow(
                        variantRepository,
                        productId,
                        variantId
                );

        return variantAdapter.toResponse(
                variant
        );

    }

    @Override
    public VariantResponseDTO updateVariant(
            Long productId,
            Long variantId,
            CreateVariantRequestDTO request
    ) {

        // 1. Validate request
        variantValidator.validate(request);

        // 2. Load Product
        Product product = productHelper.getProductOrThrow(
                productRepository,
                productId
        );

        // 3. Load Variant
        ProductVariant variant = variantHelper.getVariantOrThrow(
                variantRepository,
                productId,
                variantId
        );

        // 4. Load Specifications
        List<SpecificationDefinition> definitions =
                variantHelper.loadSpecifications(request);

        // 5. Create temporary variant
        ProductVariant updatedVariant =
                variantAdapter.toEntity(
                        request,
                        definitions
                );

        // 6. Validate uniqueness
//        validateUniqueSku(
//
//                request.getSku()
//        );
//
//        validateUniqueBarcode(
//
//                request.getBarcode()
//        );
//
//        validateDuplicateVariant(
//                product,
//
//                updatedVariant
//        );

        variantHelper.validateUniqueSku(
                variantId,
                request.getSku()
        );

        variantHelper.validateUniqueBarcode(
                variantId,
                request.getBarcode()
        );

        variantHelper.validateDuplicateVariant(
                variantId,
                product,
                updatedVariant
        );

        // 7. Update basic fields
        variant.setMrp(updatedVariant.getMrp());
        variant.setSellingPrice(updatedVariant.getSellingPrice());
        variant.setStock(updatedVariant.getStock());
        variant.setSku(updatedVariant.getSku());
        variant.setBarcode(updatedVariant.getBarcode());
        variant.setDisplayName(updatedVariant.getDisplayName());

        // 8. Replace attributes
        variant.getAttributes().clear();

        updatedVariant.getAttributes()
                .forEach(variant::addAttribute);

        // 9. Synchronize Product Price
        productPriceSyncService.syncPrices(product);

        // 10. Save
        Product savedProduct =
                productRepository.save(product);

        ProductVariant savedVariant =
                variantHelper.getVariantOrThrow(
                        variantRepository,
                        productId,
                        variantId
                );

        return variantAdapter.toResponse(savedVariant);
    }

    @Override
    public void deleteVariant(
            Long productId,
            Long variantId
    ) {

        Product product =
                productHelper.getProductOrThrow(
                        productRepository,
                        productId
                );

        ProductVariant variant =
                variantHelper.getVariantOrThrow(
                        variantRepository,
                        productId,
                        variantId
                );

        product.removeVariant(variant);

        productPriceSyncService.syncPrices(product);

        productRepository.save(product);

    }



}
