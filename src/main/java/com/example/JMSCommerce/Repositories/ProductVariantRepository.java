package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository
        extends JpaRepository<ProductVariant, Long> {
}
