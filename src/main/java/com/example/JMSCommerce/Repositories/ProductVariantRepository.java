package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository
        extends JpaRepository<ProductVariant, Long> {
    boolean existsBySku(String sku);

    boolean existsByBarcode(String barcode);


    List<ProductVariant> findByProduct_Id(Long productId);

    Optional<ProductVariant> findByIdAndProduct_Id(
            Long id,
            Long productId
    );

    Optional<ProductVariant> findBySku(String sku);

    Optional<ProductVariant> findByBarcode(String barcode);
}
