package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.DTOs.customization.CreateCustomizationRequestDTO;
import com.example.JMSCommerce.DTOs.customization.CustomizationResponseDTO;
import jakarta.validation.Valid;

public interface CustomizationService {

    CustomizationResponseDTO createCustomizations(
            Long productId,
            CreateCustomizationRequestDTO request
    );

    CustomizationResponseDTO getCustomizations(
            Long productId
    );

    CustomizationResponseDTO updateCustomizations(Long productId, @Valid CreateCustomizationRequestDTO request);
}
