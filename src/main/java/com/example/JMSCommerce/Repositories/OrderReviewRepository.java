package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.OrderReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderReviewRepository extends JpaRepository<OrderReview, Long> {
    List<OrderReview> findByProduct_Id(Long productId);


    List<OrderReview> findByOrder_Id(Long ordersId);
}
