package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.DTOs.CreateOrderRequestDTO;
import com.example.JMSCommerce.Model.Orders;
import com.example.JMSCommerce.Repositories.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;

    public List<Orders> getAllOrders() {
        return orderRepo.findAll();
    }


    public void createOrder(CreateOrderRequestDTO createOrderRequestDTO) {

        Orders order = Orders.builder().status(createOrderRequestDTO.getStatus()).build();
        orderRepo.save(order);
    }

    public Orders getOrderById(Long id) {
        return orderRepo.findById(id).orElseThrow(
                () -> new RuntimeException(
                        "Order not found"
                )
        );
    }
}
