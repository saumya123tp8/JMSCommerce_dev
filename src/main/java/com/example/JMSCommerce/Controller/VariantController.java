package com.example.JMSCommerce.Controller;

import com.example.JMSCommerce.DTOs.product.variants.CreateVariantRequestDTO;
import com.example.JMSCommerce.DTOs.product.variants.VariantResponseDTO;
import com.example.JMSCommerce.Services.VariantService;
import com.example.JMSCommerce.Utility.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products/{productId}/variants")
@RequiredArgsConstructor
public class VariantController {

    private final VariantService variantService;

    @PostMapping
    public ResponseEntity<ApiResponse<VariantResponseDTO>> createVariant(
            @PathVariable Long productId,
            @Valid @RequestBody CreateVariantRequestDTO request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        variantService.createVariant(productId, request),
                        "Variant created successfully."
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VariantResponseDTO>>> getVariants(
            @PathVariable Long productId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        variantService.getVariants(productId),
                        "Variants fetched successfully."
                )
        );
    }

    @GetMapping("/{variantId}")
    public ResponseEntity<ApiResponse<VariantResponseDTO>> getVariant(
            @PathVariable Long productId,
            @PathVariable Long variantId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        variantService.getVariant(productId, variantId),
                        "Variant fetched successfully."
                )
        );
    }

    @PutMapping("/{variantId}")
    public ResponseEntity<ApiResponse<VariantResponseDTO>> updateVariant(
            @PathVariable Long productId,
            @PathVariable Long variantId,
            @Valid @RequestBody CreateVariantRequestDTO request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        variantService.updateVariant(
                                productId,
                                variantId,
                                request
                        ),
                        "Variant updated successfully."
                )
        );
    }

    @DeleteMapping("/{variantId}")
    public ResponseEntity<ApiResponse<Void>> deleteVariant(
            @PathVariable Long productId,
            @PathVariable Long variantId
    ) {

        variantService.deleteVariant(
                productId,
                variantId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Variant deleted successfully."
                )
        );
    }

}