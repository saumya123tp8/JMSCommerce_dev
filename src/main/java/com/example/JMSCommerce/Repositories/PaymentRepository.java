package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrder_Id(Long orderId);


}