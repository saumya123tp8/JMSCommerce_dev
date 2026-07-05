package com.example.JMSCommerce.Controller;


import com.example.JMSCommerce.DTOs.ProductCreateDTO;
import com.example.JMSCommerce.DTOs.ProductResponseDTO;
import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productService;

//    @GetMapping
//    private List<Product> getAllProducts(){
//        return productService.getAllProducts();
//    }
@GetMapping
private List<ProductResponseDTO> getAllProducts(){
    return productService.getAllProducts();
}

    @PostMapping
    private Product createProduct(@RequestBody ProductCreateDTO productCreateDTO){
        return productService.createProduct(productCreateDTO);
    }


    @DeleteMapping("/{id}")
    private void deleteProduct(@PathVariable Long id){
         productService.deleteProduct(id);
    }

    @GetMapping("/{id}")
    private ProductResponseDTO getProductByID(@PathVariable Long id){
        return productService.getProductByID(id);
    }

    @GetMapping("/{id}/details")
    private Product getProductDetailByID(@PathVariable Long id){
        return productService.findProductDetailById(id);
    }




//    @GetMapping("/search")
//    private List<Product> getProductByCategory(@RequestParam("categoryName") String category){
//        return productService.getProductByCategory(category);
//    }

    @GetMapping("/search")
    private List<Product> getProductByCategory(@RequestParam("categoryName") Long category_id){
        return productService.getProductByCategory(category_id);
    }

}
