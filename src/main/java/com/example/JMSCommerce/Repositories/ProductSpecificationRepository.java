package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.ProductSpecificationValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSpecificationRepository
        extends JpaRepository<ProductSpecificationValue, Long> {
    List<ProductSpecificationValue> findByProduct_Id(Long productId);
}
