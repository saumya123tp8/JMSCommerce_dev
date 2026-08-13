package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.OrderItemCustomization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemCustomizationRepository
        extends JpaRepository<OrderItemCustomization, Long> {

    List<OrderItemCustomization>
    findAllByOrderItem_IdIn(
            List<Long> orderItemIds
    );
}