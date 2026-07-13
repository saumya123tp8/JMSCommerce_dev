package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
//    List<Product> findByCategory(String category);
//    Now I will search based upon category id
List<Product>findByCategory(Long category_id);

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
}
