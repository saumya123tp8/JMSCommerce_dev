package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.Adapters.BrandAdapter;
import com.example.JMSCommerce.DTOs.product.BrandReqDTO;
import com.example.JMSCommerce.DTOs.product.BrandSummaryDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.Brand;
import com.example.JMSCommerce.Repositories.BrandRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepo brandRepository;
    private final BrandAdapter brandAdapter;

    public List<BrandSummaryDTO> getAllBrands() {

        return brandRepository.findAll()
                .stream()
                .map(brandAdapter::mapBrandToSummaryDTO)
                .toList();
    }

    public BrandSummaryDTO createBrand(
            @Valid BrandReqDTO request
    ) {

        if (brandRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException(
                    "Brand with name '" + request.getName() + "' already exists."
            );
        }

        Brand brand = brandAdapter.mapRequestDTOToBrand(request);

        brand = brandRepository.save(brand);

        return brandAdapter.mapBrandToSummaryDTO(brand);
    }

    public BrandSummaryDTO getBrandByIdAsDTO(Long id) {

        Brand brand = getBrandById(id);

        return brandAdapter.mapBrandToSummaryDTO(brand);
    }

    public BrandSummaryDTO updateBrand(
            Long id,
            @Valid BrandReqDTO request
    ) {

        Brand brand = getBrandById(id);

        if (brandRepository.existsByNameIgnoreCaseAndIdNot(
                request.getName(),
                id
        )) {
            throw new BadRequestException(
                    "Brand with name '" + request.getName() + "' already exists."
            );
        }

        brand.setName(request.getName());

        brand = brandRepository.save(brand);

        return brandAdapter.mapBrandToSummaryDTO(brand);
    }

    public Brand getBrandById(Long id) {

        return brandRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Brand with id " + id + " not found."
                        ));
    }
}