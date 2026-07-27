package com.example.JMSCommerce.Controller;


import com.example.JMSCommerce.DTOs.product.ProductCreateDTO;
import com.example.JMSCommerce.DTOs.product.ProductResponseDTO;
import com.example.JMSCommerce.DTOs.product.ProductResponseDetailsDTO;
import com.example.JMSCommerce.DTOs.product.ProductSpecificationResponseDTO;
import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Services.ProductService;
import com.example.JMSCommerce.Utility.ApiResponse;
import com.example.JMSCommerce.Utility.AppConstants;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/products")
public class ProductController {


    private final ProductService productService;

//    @PostConstruct
//    public void init() {
//        System.out.println("Controller : " + this);
//        System.out.println("Hash       : " + System.identityHashCode(this));
//        System.out.println("Service    : " + productService);
//    }

    @GetMapping
    @PermitAll
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getAllProducts(){

        return ResponseEntity.ok(ApiResponse.success(productService.getAllProducts(),"Fetched All Product"));
    }

    @PostMapping
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<ProductResponseDTO>> createProduct(@RequestBody ProductCreateDTO productCreateDTO){

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(productService.createProduct(productCreateDTO), "Product Created Successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(productService.deleteProduct(id),"Product Deleted Successfully"));
    }

    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getProductByID(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponse.success(productService.getProductByID(id),"Product with given id"));
    }

    @GetMapping("/{id}/details")
    @PermitAll
    public ResponseEntity<ApiResponse<ProductResponseDetailsDTO>> getProductDetailByID(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(productService.findProductDetailById(id), "Product detail with given Id fetched successfully"));

    }



    @GetMapping("/search")
    @PermitAll
    private ResponseEntity<ApiResponse<List<ProductResponseDetailsDTO>>> getProductByCategory(@RequestParam("categoryName") Long category_id){
        return ResponseEntity.ok(ApiResponse.success(productService.getProductByCategory(category_id), "Product Details Fetched Successfully"));
    }

    @GetMapping("/{id}/specifications")
    @PermitAll
    public ResponseEntity<ApiResponse<List<ProductSpecificationResponseDTO>>>
    getSpecifications(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        productService.getProductSpecifications(id),"List Of Specified Sepcification for the selected product"
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<ProductResponseDTO>> updateProduct(@PathVariable Long id,@RequestBody ProductCreateDTO productCreateDTO){
        return ResponseEntity.ok(ApiResponse.success(productService.updateProduct(id,productCreateDTO),"Product Updated Successfully"));
    }

}
