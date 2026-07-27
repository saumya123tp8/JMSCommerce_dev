package com.example.JMSCommerce.Controller;


import com.example.JMSCommerce.DTOs.CategoryResponseDTO;
import com.example.JMSCommerce.DTOs.CreateCategoryRequestDTO;
import com.example.JMSCommerce.DTOs.UpdateCategoryRequestDTO;
import com.example.JMSCommerce.DTOs.product.BrandReqDTO;
import com.example.JMSCommerce.DTOs.product.BrandSummaryDTO;
import com.example.JMSCommerce.Repositories.BrandRepo;
import com.example.JMSCommerce.Services.BrandService;
import com.example.JMSCommerce.Services.CategoryService;
import com.example.JMSCommerce.Utility.ApiResponse;
import com.example.JMSCommerce.Utility.AppConstants;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/brand")
public class BrandController {

    private final BrandRepo brandRepo;
    private final BrandService brandService;

    @GetMapping
    @PermitAll
    public ResponseEntity<ApiResponse<List<BrandSummaryDTO>>> getAllBrands(){
        List<BrandSummaryDTO> allBrands = brandService.getAllBrands();
        return ResponseEntity.ok().body(ApiResponse.success(allBrands,"List of all Brands available"));
    }

    @PostMapping
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<BrandSummaryDTO>> createBrand(@Valid @RequestBody BrandReqDTO brandReqDTO){
        BrandSummaryDTO brandResponse = brandService.createBrand(brandReqDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(brandResponse,"Brand Created Successfully"));
    }

    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<ApiResponse<BrandSummaryDTO>> getBrandById(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(brandService.getBrandByIdAsDTO(id),"Category Fetched Successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<BrandSummaryDTO>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody BrandReqDTO request) {

        BrandSummaryDTO response =
                brandService.updateBrand(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Brand updated successfully."
                )
        );
    }
//    @DeleteMapping("/{id}")
//    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
//    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id){
//        categoryService.deleteCategory(id);
//        return ResponseEntity.ok().body(ApiResponse.success(null,"Category deleted successfully"));
//    }

}
