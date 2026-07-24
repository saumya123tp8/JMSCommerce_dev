package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.Order;
import com.example.JMSCommerce.Utility.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

//    List<Orders> findByUserId(Long user_id);
  Optional<Order> findByIdAndUser_Id(Long orderId, Long userId);

  List<Order> findAllByUser_Id(Long userId);

  List<Order> findAllByStatus(OrderStatus status);
}
