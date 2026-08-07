package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.Review;
import com.example.JMSCommerce.Utility.enums.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    Optional<Review> findByOrderProduct_Id(
            Long orderProductId
    );



    List<Review> findByOrderProduct_Product_IdAndStatus(
            Long productId,
            ReviewStatus status
    );

}