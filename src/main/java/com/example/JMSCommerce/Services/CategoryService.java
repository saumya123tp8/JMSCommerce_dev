package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.DTOs.CreateCategoryRequestDTO;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.Category;
import com.example.JMSCommerce.Repositories.CategoryRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public List<Category> getAllCategories() {
        log.warn("getAllCategories method called");
        return categoryRepo.findAll();
    }


    public Category createCategory(CreateCategoryRequestDTO createCategoryRequestDTO) {

        Category category = Category.builder().name(createCategoryRequestDTO.getName()).build();
        categoryRepo.save(category);
        return category;
    }

    public Category getCategoryById(Long id) {
        log.info("getCategoryById method called with id {}", id);
        return categoryRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Category not found"
                )
        );
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Category not found"
                )
        );
        categoryRepo.delete(category);
        log.info("Category with id {} deleted successfully", id);
    }
}
