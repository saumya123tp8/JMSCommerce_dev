package com.example.JMSCommerce.Adapters;

import com.example.JMSCommerce.DTOs.product.ProductCreateDTO;
import com.example.JMSCommerce.DTOs.product.ProductResponseDTO;
import com.example.JMSCommerce.DTOs.product.ProductResponseDetailsDTO;
import com.example.JMSCommerce.Model.Product;
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
                .mrp(entity.getMrp())
                .primaryImage(entity.getPrimaryImage())
                .rating(entity.getRating())
                .category(entity.getCategory())
                .build();
    }
    public ProductResponseDTO mapProductToResponseDTO(Product entity){
        return ProductResponseDTO.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .mrp(entity.getMrp())
                .primaryImage(entity.getPrimaryImage())
                .rating(entity.getRating())
                .build();
    }

    public Product mapProductCreateDTOToProduct(ProductCreateDTO productCreateDTO){
        return  Product.builder()
                .name(productCreateDTO.getName())
                .mrp(productCreateDTO.getMrp())
                .primaryImage(productCreateDTO.getPrimaryImage())
                .description(productCreateDTO.getDescription())
                .currency(productCreateDTO.getCurrency())
                .sellingPrice(productCreateDTO.getSellingPrice())
                .build();
    }
}
