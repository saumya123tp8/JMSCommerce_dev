package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.DTOs.ProductCreateDTO;
import com.example.JMSCommerce.DTOs.ProductResponseDTO;
import com.example.JMSCommerce.Model.Category;
import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Repositories.ProductRepo;
import lombok.RequiredArgsConstructor;
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
public List<ProductResponseDTO> getAllProducts() {
    List<Product> products = productRepo.findAll();
//    List<ProductResponseDTO> productRes = new ArrayList<>();
//    for(Product product : products){
//        ProductResponseDTO productResponseDTO = ProductResponseDTO.builder().title(product.getTitle())
//                .id(product.getId())
//                .price(product.getPrice())
//                .rating(product.getRating())
//                .description(product.getDescription())
//                .image(product.getImage())
//                .build();
//        productRes.add(productResponseDTO);
//
//    }
//
//    return productRes;


//    using stream
    return products.stream().map(product -> ProductResponseDTO.builder().title(product.getTitle())
            .id(product.getId())
            .price(product.getPrice())
            .rating(product.getRating())
            .description(product.getDescription())
            .image(product.getImage())
            .build()
    ).collect(Collectors.toList());
}

    public Product createProduct(ProductCreateDTO productCreateDTO) {
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
        return product;
    }

    public void deleteProduct(Long id) {

       productRepo.deleteById(id);


    }

//    public List<Product> getProductByCategory(String category) {
//
//        return productRepo.findByCategory(category);
//    }
public List<Product> getProductByCategory(Long category_id) {

    return productRepo.findByCategory(category_id);
}

//    public Product getProductByID(Long id) {
//       return productRepo.findById(id).orElseThrow(
//               ()->new RuntimeException("Product with given Id not found")
//       );
//    }

    public ProductResponseDTO getProductByID(Long id) {
        Product product = productRepo.findById(id).orElseThrow(
                ()->new RuntimeException("Product with given Id not found")
        );
        return ProductResponseDTO.builder().title(product.getTitle())
                .id(product.getId())
                .price(product.getPrice())
                .rating(product.getRating())
                .description(product.getDescription())
                .image(product.getImage())
                .build();
    }

    public Product findProductDetailById(Long id){
    return productRepo.findProductDetailById(id);
    }
}
