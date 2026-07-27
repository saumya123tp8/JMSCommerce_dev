package com.example.JMSCommerce.Adapters;

import com.example.JMSCommerce.DTOs.product.BrandReqDTO;
import com.example.JMSCommerce.DTOs.product.BrandSummaryDTO;
import com.example.JMSCommerce.Model.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandAdapter {

    public Brand mapRequestDTOToBrand(BrandReqDTO request) {

        return Brand.builder()
                .name(request.getName())
                .build();
    }

    public BrandSummaryDTO mapBrandToSummaryDTO(Brand brand) {

        return BrandSummaryDTO.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }
}