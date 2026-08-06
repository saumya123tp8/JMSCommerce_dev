package com.example.JMSCommerce.Controller;

import com.example.JMSCommerce.DTOs.customization.CreateCustomizationRequestDTO;
import com.example.JMSCommerce.DTOs.customization.CustomizationResponseDTO;
import com.example.JMSCommerce.Services.CustomizationService;
import com.example.JMSCommerce.Utility.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products/{productId}/customizations")
@RequiredArgsConstructor
public class CustomizationController {

    private final CustomizationService customizationService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomizationResponseDTO>> create(
            @PathVariable Long productId,
            @Valid @RequestBody CreateCustomizationRequestDTO request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        customizationService.createCustomizations(productId, request), "Customizaation added"
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CustomizationResponseDTO>> get(
            @PathVariable Long productId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        customizationService.getCustomizations(productId),"customization you have"
                )
        );
    }

    @PutMapping
    public ResponseEntity<ApiResponse<CustomizationResponseDTO>> update(
            @PathVariable Long productId,
            @Valid @RequestBody CreateCustomizationRequestDTO request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        customizationService.updateCustomizations(productId, request),
                        "customization updated"
                )
        );
    }
}