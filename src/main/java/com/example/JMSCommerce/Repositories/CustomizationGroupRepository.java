package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.CustomizationGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomizationGroupRepository extends JpaRepository<CustomizationGroup, Long> {

    List<CustomizationGroup> findByProduct_IdAndMinSelectionNot(
            Long productId,
            Integer minSelection
    );

    List<CustomizationGroup> findByProduct_IdAndRequired(Long id, Boolean i);


}
