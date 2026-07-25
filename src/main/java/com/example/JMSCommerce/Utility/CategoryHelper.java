package com.example.JMSCommerce.Utility;

import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Exception.DuplicateRecordException;
import com.example.JMSCommerce.Model.Category;
import com.example.JMSCommerce.Repositories.CategoryRepo;
import com.example.JMSCommerce.Utility.enums.CategoryStatus;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryHelper {
    private final CategoryRepo categoryRepository;

    public String generateUniqueSlug(String name){

        String baseSlug = SlugUtil.toSlug(name);

        String slug = baseSlug;

        int counter = 1;

        while(categoryRepository.existsBySlug(slug)){

            slug = baseSlug + "-" + counter++;

        }

        return slug;

    }

    public Category getParent(Long parentId){

        if(parentId == null){

            return null;

        }

        return categoryRepository.findById(parentId)
                .orElseThrow(
                        () -> new BadRequestException(
                                "Parent category not found."
                        )
                );

    }

    public String normalizeName(String name){

        return name.trim()
                .replaceAll("\\s+"," ");

    }

    public void validateDuplicateName(String name) {

        Category category = categoryRepository
                .findByNameIgnoreCase(name)
                .orElse(null);

        if (category == null) {
            return;
        }

        if (category.getStatus() == CategoryStatus.ACTIVE) {
            throw new DuplicateRecordException(
                    "Category already exists."
            );
        }

        throw new DuplicateRecordException(
//                category.getId(),
                "Category already exists in inactive state."
        );

    }

    private void validateDuplicateName(
            Long categoryId,
            String name
    ) {

        if (categoryRepository
                .existsByNameIgnoreCaseAndIdNot(name, categoryId)) {

            throw new DuplicateRecordException(
                    "Category already exists"
            );

        }

    }

    public void validateParent(Category category, Category parent) {


        if (parent == null) {

            return;
        }

        if (category.getId().equals(parent.getId())) {
            throw new BadRequestException(
                    "Category cannot be its own parent."
            );
        }

    }

}
