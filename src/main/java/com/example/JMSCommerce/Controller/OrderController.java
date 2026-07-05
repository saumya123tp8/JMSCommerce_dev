package com.example.JMSCommerce.Controller;

import com.example.JMSCommerce.DTOs.CreateOrderRequestDTO;
import com.example.JMSCommerce.Model.Orders;
import com.example.JMSCommerce.Services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<Orders> getAllOrders(){
        return orderService.getAllOrders();
    }

    @PostMapping
    public void createCategory(@RequestBody CreateOrderRequestDTO createOrderRequestDTO){
        orderService.createOrder(createOrderRequestDTO);
    }

    @GetMapping("/{id}")
    public Orders getOrderById(@PathVariable Long id){
        return orderService.getOrderById(id);
    }
}
