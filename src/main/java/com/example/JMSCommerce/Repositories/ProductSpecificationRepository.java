package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.ProductSpecificationValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSpecificationRepository
        extends JpaRepository<ProductSpecificationValue, Long> {
}
