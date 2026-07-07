package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

//    List<Orders> findByUserId(Long user_id);

}
