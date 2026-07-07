package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepo extends JpaRepository<OrderProduct,Long> {
    public List<OrderProduct> findByOrderId(Long orderId) ;
}
