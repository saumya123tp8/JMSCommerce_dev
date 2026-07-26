package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.Category;
import com.example.JMSCommerce.Model.SpecificationDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecificationDefinitionRepository
        extends JpaRepository<SpecificationDefinition, Long> {

    List<SpecificationDefinition> findByCategory(Category category);
    boolean existsByCategoryAndNameIgnoreCase(Category category, String name);
    boolean existsByCategoryAndNameIgnoreCaseAndIdNot(Category category, String name, Long specificationId);

}
