package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.DTOs.product.variants.CreateVariantRequestDTO;
import com.example.JMSCommerce.DTOs.product.variants.VariantResponseDTO;

import java.util.List;

public interface VariantService {
    VariantResponseDTO createVariant(
            Long productId,
            CreateVariantRequestDTO request
    );

    List<VariantResponseDTO> getVariants(
            Long productId
    );

    VariantResponseDTO getVariant(
            Long productId,
            Long variantId
    );

    VariantResponseDTO updateVariant(
            Long productId,
            Long variantId,
            CreateVariantRequestDTO request
    );

    void deleteVariant(
            Long productId,
            Long variantId
    );
}
