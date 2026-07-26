package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Utility.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    //    List<Product> findByCategory(String category);
    //    Now I will search based upon category id
    List<Product>findByCategory_Id(Long category_id);

    // this gives error because while selecting we have id column 2 times for id and product
    //@Query(nativeQuery = true,
    //value = "SELECT p.*, c.* FROM PRODUCT p JOIN Category c on p.category_id=c.id where p.id = :id "
    //)


        // becuase we are still querying the db to get category explicitly
    //    @Query(nativeQuery = true,
    //            value = "SELECT p.*, c.name FROM PRODUCT p JOIN Category c on p.category_id=c.id where p.id = :id "
    //    )
    @Query( "SELECT p from Product p join fetch p.category  where p.id = :id ")
     Optional<Product> findProductDetailById(Long id);

        @Modifying
        @Query("""
        UPDATE Product p
        SET p.status = :status
        WHERE p.category.id = :categoryId
    """)
    void updateStatusByCategory(
            Long categoryId,
            ProductStatus status
    );

    boolean existsByNameIgnoreCase(String trim);

    boolean existsBySkuIgnoreCase(String trim);

    boolean existsByBarcode(String trim);

    boolean existsBySlug(String s);
}
