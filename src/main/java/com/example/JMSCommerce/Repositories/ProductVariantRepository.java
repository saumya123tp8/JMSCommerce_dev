package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Modifying
    @Query("""
    UPDATE ProductVariant v
    SET v.stock = v.stock - :quantity
    WHERE v.id = :variantId
      AND v.stock >= :quantity
""")
    int reserveStock(
            @Param("variantId") Long variantId,
            @Param("quantity") Integer quantity
    );

    @Modifying
    @Query("""
    UPDATE ProductVariant v
    SET v.stock = v.stock + :quantity
    WHERE v.id = :variantId
""")
    int releaseStock(
            @Param("variantId") Long variantId,
            @Param("quantity") Integer quantity
    );
}
