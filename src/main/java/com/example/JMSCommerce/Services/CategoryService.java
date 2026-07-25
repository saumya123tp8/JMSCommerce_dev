package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.Adapters.CategoryAdapter;
import com.example.JMSCommerce.DTOs.CategoryResponseDTO;
import com.example.JMSCommerce.DTOs.CreateCategoryRequestDTO;
import com.example.JMSCommerce.DTOs.UpdateCategoryRequestDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.Category;
import com.example.JMSCommerce.Repositories.CategoryRepo;
import com.example.JMSCommerce.Repositories.ProductRepo;
import com.example.JMSCommerce.Utility.CategoryHelper;
import com.example.JMSCommerce.Utility.enums.CategoryStatus;
import com.example.JMSCommerce.Utility.enums.ProductStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepo categoryRepo;
    private final CategoryAdapter categoryAdapter;
    private final CategoryHelper categoryHelper;
    private final ProductRepo productRepo;
    @Value("${product.utility.maxInheritValueForCategory}")
    private int maxLeveAllowed;


    public List<CategoryResponseDTO> getAllCategories() {
        log.warn("getAllCategories method called");
        List<Category> categories = categoryRepo.findAll();
        return categories.stream().map(category -> {
           return categoryAdapter.mapToCategoryResponseDTO(category);
        }).collect(Collectors.toList());

    }


//    public Category createCategory(CreateCategoryRequestDTO createCategoryRequestDTO) {
//
//        Category category = Category.builder().name(createCategoryRequestDTO.getName()).build();
//        categoryRepo.save(category);
//        return category;
//    }

    public CategoryResponseDTO createCategory(
            CreateCategoryRequestDTO request
    ){

        String normalizedName = categoryHelper.normalizeName(request.getName());

        categoryHelper.validateDuplicateName(normalizedName);

        Category parent = categoryHelper.getParent(request.getParentId());

        Category category =  Category.builder()
                .name(normalizedName)
                .slug(categoryHelper.generateUniqueSlug(normalizedName))
                .description(request.getDescription())
                .status(CategoryStatus.ACTIVE)
                .parent(parent)
                .build();
        if(parent == null ){
            category.setLevel(1);
        }else{
            int new_level = parent.getLevel()+1;
            if(new_level > maxLeveAllowed){
                throw new BadRequestException(
                        "select different parent, max inheritance leve " + maxLeveAllowed
                );
            }
            category.setLevel(new_level);
        }
        categoryRepo.save(category);


        return categoryAdapter.mapToCategoryResponseDTO(category);

    }

    public Category getCategoryById(Long id) {
        log.info("getCategoryById method called with id {}", id);
        return categoryRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Category not found"
                )
        );
    }

    public CategoryResponseDTO getCategoryByIdAsDTO(Long id){
        return categoryAdapter.mapToCategoryResponseDTO(this.getCategoryById(id));
    }

    @Transactional
    public CategoryResponseDTO updateCategory(
            Long id,
            UpdateCategoryRequestDTO request
    ) {

        Category category = getCategoryById(id);

        String normalizedName =
                categoryHelper.normalizeName(request.getName());

        Category parent =
                categoryHelper.getParent(request.getParentId());

        categoryHelper.validateParent(category, parent);

        if (!category.getName().equalsIgnoreCase(normalizedName)) {

            categoryHelper.validateDuplicateName(normalizedName);

            category.setName(normalizedName);

            category.setSlug(
                    categoryHelper.generateUniqueSlug(normalizedName)
            );

        }

        category.setDescription(
                request.getDescription()
        );

        if (request.getStatus() != null) {

            category.setStatus(
                    request.getStatus()
            );

        }

        category.setParent(parent);

        category = categoryRepo
                .save(category);

        return categoryAdapter.mapToCategoryResponseDTO(category);

    }


//    public void deleteCategory(Long id) {
//        Category category = categoryRepo.findById(id).orElseThrow(
//                () -> new ResourceNotFoundException(
//                        "Category not found"
//                )
//        );
//        categoryRepo.delete(category);
//        log.info("Category with id {} deleted successfully", id);
//    }

    @Transactional
    public void updateStatus(Long id, CategoryStatus status) {

        Category category = getCategoryById(id);

        category.setStatus(status);

        productRepo.updateStatusByCategory(
                id,
                ProductStatus.valueOf(status.name())
        );
    }
}
