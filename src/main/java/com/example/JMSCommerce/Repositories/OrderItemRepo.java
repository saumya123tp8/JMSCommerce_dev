package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem,Long> {
    public List<OrderItem> findByOrder_Id(Long orderId) ;

    void deleteByOrder_Id(Long id);

    @Override
    Optional<OrderItem> findById(Long aLong);

    Optional<OrderItem> findByIdAndOrder_Id(Long orderProductId, Long orderId);
}
