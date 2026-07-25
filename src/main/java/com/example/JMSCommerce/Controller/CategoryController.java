package com.example.JMSCommerce.Controller;

import com.example.JMSCommerce.DTOs.CategoryResponseDTO;
import com.example.JMSCommerce.DTOs.CreateCategoryRequestDTO;
import com.example.JMSCommerce.DTOs.UpdateCategoryRequestDTO;
import com.example.JMSCommerce.Model.Category;
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
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @PermitAll
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getAllCategories(){
        List<CategoryResponseDTO> allCategories = categoryService.getAllCategories();
        return ResponseEntity.ok().body(ApiResponse.success(allCategories,"List of all Categories available"));
    }

    @PostMapping
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> createCategory(@Valid @RequestBody CreateCategoryRequestDTO createCategoryRequestDTO){
        CategoryResponseDTO categoryResponseDTO = categoryService.createCategory(createCategoryRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(categoryResponseDTO,"Category Created Successfully"));
    }

    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> getCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(categoryService.getCategoryByIdAsDTO(id),"Category Fetched Successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequestDTO request) {

        CategoryResponseDTO response =
                categoryService.updateCategory(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        response,
                        "Category updated successfully."
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
