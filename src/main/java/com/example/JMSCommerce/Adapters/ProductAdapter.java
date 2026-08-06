package com.example.JMSCommerce.Adapters;

import com.example.JMSCommerce.DTOs.product.*;
import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Model.ProductSpecificationValue;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductAdapter {
    public ProductResponseDetailsDTO mapProductToResponseDetailsDTO(Product entity){
        return ProductResponseDetailsDTO.builder()
                .name(entity.getName())
                .description(entity.getDescription())
//                .mrp(entity.getMrp())
                .primaryImage(entity.getPrimaryImage())
                .rating(entity.getRating())
                .currency(entity.getCurrency())
//                .sellingPrice(entity.getSellingPrice())
                .category(
                        CategorySummaryDTO.builder()
                                .id(entity.getCategory().getId())
                                .name(entity.getCategory().getName())
                                .description(entity.getCategory().getDescription())
                                .build()
                )
                .brand(
                        BrandSummaryDTO.builder()
                                .id(entity.getBrand().getId())
                                .name(entity.getBrand().getName())
                                .build()
                )
                .build();
    }
    public ProductResponseDTO mapProductToResponseDTO(Product entity){
        return ProductResponseDTO.builder()
                .name(entity.getName())
                .shortDescription(entity.getShortDescription())
//                .mrp(entity.getMrp())
                .primaryImage(entity.getPrimaryImage())
                .rating(entity.getRating())
                .brandName(entity.getBrand()!=null?entity.getBrand().getName():"")
                .currency(entity.getCurrency())
                .rating(entity.getRating())
//                .sellingPrice(entity.getSellingPrice())
                .status(entity.getStatus())
                .currency(entity.getCurrency())
                .inventoryType(entity.getInventoryType())
                .id(entity.getId())
                .build();
    }

    public Product mapProductCreateDTOToProduct(ProductCreateDTO productCreateDTO){
        return  Product.builder()
                .name(productCreateDTO.getName())
//                .mrp(productCreateDTO.getMrp())
                .primaryImage(productCreateDTO.getPrimaryImage())
                .description(productCreateDTO.getDescription())
                .currency(productCreateDTO.getCurrency())
                .shortDescription(productCreateDTO.getShortDescription())
                .inventoryType(productCreateDTO.getInventoryType())
//                .sellingPrice(productCreateDTO.getSellingPrice())
                .build();
    }


    public ProductSpecificationResponseDTO
    mapProductSpecificationValueToResponseDTO(
            ProductSpecificationValue value
    ) {

        return ProductSpecificationResponseDTO.builder()
                .specificationId(
                        value.getSpecificationDefinition().getId()
                )
                .name(
                        value.getSpecificationDefinition().getName()
                )
                .displayName(
                        value.getSpecificationDefinition().getDisplayName()
                )
                .unit(
                        value.getSpecificationDefinition().getUnit()
                )
                .value(
                        value.getValue()
                )
                .build();
    }
}
