package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.CustomizationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomizationOptionRepository extends JpaRepository<CustomizationOption,Long> {

    @Query("""
    SELECT DISTINCT co.customizationGroup.id
    FROM CustomizationOption co
    WHERE co.id IN :optionIds
""")
    List<Long> findDistinctGroupIdsByOptionIds(
            @Param("optionIds") List<Long> optionIds
    );

}
