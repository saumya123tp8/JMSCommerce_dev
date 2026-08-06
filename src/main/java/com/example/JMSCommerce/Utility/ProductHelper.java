package com.example.JMSCommerce.Utility;

import com.example.JMSCommerce.DTOs.productSpecification.ProductSpecificationValueDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.Category;
import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Model.SpecificationDefinition;
import com.example.JMSCommerce.Repositories.CategoryRepo;
import com.example.JMSCommerce.Repositories.ProductRepo;
import com.example.JMSCommerce.Repositories.SpecificationDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductHelper {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepository;
    private final SpecificationDefinitionRepository specificationDefinitionRepository;

    public Product getActiveProductOrThrow(ProductRepo productRepository, Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id : " + productId
                        )
                );
    }
    public Product getProductOrThrow(ProductRepo productRepository, Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id : " + productId
                        )
                );
    }

    public void validateSellingPrice(
            BigDecimal mrp,
            BigDecimal sellingPrice
    ) {
        if(sellingPrice == null || sellingPrice.equals(BigDecimal.ZERO)){
            sellingPrice = mrp;
        }
        if (sellingPrice.compareTo(mrp) > 0) {
            throw new BadRequestException(
                    "Selling price cannot be greater than MRP."
            );
        }
    }
    public void validateDuplicateProductName(
            String name
    ) {

        if (productRepo.existsByNameIgnoreCase(name.trim())) {
            throw new BadRequestException(
                    "Product with name '" + name + "' already exists."
            );
        }
    }

    //    public void validateSku(
//            String sku
//    ) {
//
//        if (sku != null &&
//                !sku.isBlank() &&
//                productRepo.existsBySkuIgnoreCase(sku.trim())) {
//
//            throw new BadRequestException(
//                    "SKU already exists."
//            );
//        }
//    }

//    public void validateBarcode(
//            String barcode
//    ) {
//
//        if (barcode != null &&
//                !barcode.isBlank() &&
//                productRepo.existsByBarcode(barcode.trim())) {
//
//            throw new BadRequestException(
//                    "Barcode already exists."
//            );
//        }
//    }
    public String normalizeName(String name){

        return name.trim()
                .replaceAll("\\s+"," ");

    }


    public List<SpecificationDefinition> fetchAllSpecificationsThroughParent(
            Long categoryId
    ) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id : " + categoryId
                        )
                );

        Map<Long, SpecificationDefinition> specifications = new LinkedHashMap<>();

        while (category != null) {

            List<SpecificationDefinition> currentSpecifications =
                    specificationDefinitionRepository.findByCategory(category);

            currentSpecifications.forEach(specification ->
                    specifications.putIfAbsent(
                            specification.getId(),
                            specification
                    )
            );

            category = category.getParent();
        }

        return new ArrayList<>(specifications.values());
    }

    public void validateSpecificationValue(SpecificationDefinition definition, ProductSpecificationValueDTO productSpecificationValueDTO) {

        System.out.println(" def "+definition+" DTO "+productSpecificationValueDTO);
        String value = productSpecificationValueDTO.getValue();
        switch (definition.getDataType()) {

            case TEXT:
                if (value == null || value.isBlank()) {
                    throw new BadRequestException(
                            definition.getDisplayName() + " cannot be blank."
                    );
                }
                break;

            case NUMBER:
                try {
                    Double.parseDouble(value);
                } catch (NumberFormatException ex) {
                    throw new BadRequestException(
                            definition.getDisplayName() + " must be a valid number."
                    );
                }
                break;

            case BOOLEAN:
                if (!value.equalsIgnoreCase("true")
                        && !value.equalsIgnoreCase("false")) {

                    throw new BadRequestException(
                            definition.getDisplayName() + " must be true or false."
                    );
                }
                break;

            case DATE:
                try {
                    LocalDate.parse(value);
                } catch (DateTimeParseException ex) {
                    throw new BadRequestException(
                            definition.getDisplayName()
                                    + " must be in yyyy-MM-dd format."
                    );
                }
                break;
        }

    }
}
