package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.DTOs.ProductCreateDTO;
import com.example.JMSCommerce.DTOs.ProductResponseDTO;
import com.example.JMSCommerce.DTOs.ProductResponseDetailsDTO;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.Category;
import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Repositories.ProductRepo;
import com.example.JMSCommerce.Utility.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final CategoryService categoryService;

    //    public List<Product> getAllProducts() {
//        return productRepo.findAll();
//    }
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getAllProducts() {
        List<Product> products = productRepo.findAll();
        List<ProductResponseDTO> listProducts = products.stream().map(product -> ProductResponseDTO.builder().title(product.getTitle())
                .id(product.getId())
                .price(product.getPrice())
                .rating(product.getRating())
                .description(product.getDescription())
                .image(product.getImage())
                .build()
        ).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(listProducts, "Fetched All Product"));
    }

    public ResponseEntity<ApiResponse<Product>> createProduct(ProductCreateDTO productCreateDTO) {
        Category category = categoryService.getCategoryById(productCreateDTO.getCategory());
        Product product = Product.builder()
                .title(productCreateDTO.getTitle())
                .price(productCreateDTO.getPrice())
                .image(productCreateDTO.getImage())
                .category(category)
                .description(productCreateDTO.getDescription())
                .rating(productCreateDTO.getRating())
                .build();
        productRepo.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(product, "Product Created Successfully"));
    }

    public ResponseEntity<ApiResponse<Void>> deleteProduct(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product with id " + id + " not found"));

        productRepo.delete(product);

        return ResponseEntity.ok(
                ApiResponse.success(null, "Product deleted successfully")
        );
    }

    //    public List<Product> getProductByCategory(String category) {
//
//        return productRepo.findByCategory(category);
//    }
    public ResponseEntity<ApiResponse<List<ProductResponseDetailsDTO>>> getProductByCategory(Long category_id) {
        List<ProductResponseDetailsDTO> productListWithGivenCategory =
                productRepo.findByCategory(category_id).stream().map(
                        product -> ProductResponseDetailsDTO.builder()
                                .id(product.getId())
                                .title(product.getTitle())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .category(product.getCategory())
                                .image(product.getImage())
                                .rating(product.getRating())
                                .build()
                ).collect(Collectors.toList());
        if (productListWithGivenCategory.isEmpty()) {
            throw new ResourceNotFoundException("Product with Category" + category_id + " not found");
        }
        return ResponseEntity.ok(ApiResponse.success(productListWithGivenCategory, "Product Details Fetched Successfully"))

//        return productRepo.findByCategory(category_id);
    }

//    public Product getProductByID(Long id) {
//       return productRepo.findById(id).orElseThrow(
//               ()->new RuntimeException("Product with given Id not found")
//       );
//    }

    public ResponseEntity<ApiResponse<ProductResponseDTO>> getProductByID(Long id) {
        ProductResponseDTO response = productRepo.findById(id).map(
                        product ->
                                ProductResponseDTO.builder().title(product.getTitle())
                                        .id(product.getId())
                                        .price(product.getPrice())
                                        .rating(product.getRating())
                                        .description(product.getDescription())
                                        .image(product.getImage())
                                        .build())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Product with given Id not found")
                );
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response, "Product with given Id fetched successfully"));
    }

    public ResponseEntity<ApiResponse<ProductResponseDetailsDTO>> findProductDetailById(Long id) {
        ProductResponseDetailsDTO productResponseDetailsDTO = productRepo.findProductDetailById(id).map(
                product -> ProductResponseDetailsDTO.builder()
                        .id(product.getId())
                        .title(product.getTitle())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .category(product.getCategory())
                        .image(product.getImage())
                        .rating(product.getRating())
                        .build()
        ).orElseThrow(
                () -> new ResourceNotFoundException("Product with id " + id + " not found")
        );

        return ResponseEntity.ok(ApiResponse.success(productResponseDetailsDTO, "Product Details Fetched Successfully"))

    }
}
