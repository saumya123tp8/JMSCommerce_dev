package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Orders, Long> {

}
