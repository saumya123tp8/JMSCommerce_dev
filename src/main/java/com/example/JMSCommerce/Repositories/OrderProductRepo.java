package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.Order;
import com.example.JMSCommerce.Model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrderProductRepo extends JpaRepository<OrderProduct,Long> {
    public List<OrderProduct> findByOrder_Id(Long orderId) ;

    void deleteByOrder_Id(Long id);

    @Query("SELECT op FROM OrderProduct op JOIN FETCH op.product WHERE op.order = :order")
    Collection<OrderProduct> findByOrderWithProduct(Order order);
}
