package com.example.JMSCommerce.Controller;

import com.example.JMSCommerce.DTOs.CreateCategoryRequestDTO;
import com.example.JMSCommerce.Model.Category;
import com.example.JMSCommerce.Services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @PostMapping
    public void createCategory(@RequestBody CreateCategoryRequestDTO createCategoryRequestDTO){
        categoryService.createCategory(createCategoryRequestDTO);
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
    }

}
