package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.DTOs.CreateCategoryRequestDTO;
import com.example.JMSCommerce.Model.Category;
import com.example.JMSCommerce.Repositories.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }


    public Category createCategory(CreateCategoryRequestDTO createCategoryRequestDTO) {

        Category category = Category.builder().name(createCategoryRequestDTO.getName()).build();
        categoryRepo.save(category);
        return category;
    }

    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id).orElseThrow(
                () -> new RuntimeException(
                        "Category not found"
                )
        );
    }

    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }
}
