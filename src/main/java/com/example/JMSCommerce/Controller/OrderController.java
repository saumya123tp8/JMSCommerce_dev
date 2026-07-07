package com.example.JMSCommerce.Controller;

import com.example.JMSCommerce.DTOs.CreateOrderRequestDTO;
import com.example.JMSCommerce.DTOs.GetOrderResponseDTO;
import com.example.JMSCommerce.Model.Order;
import com.example.JMSCommerce.Services.OrderService;
import com.example.JMSCommerce.Utility.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GetOrderResponseDTO>>> getAllOrders(){
          return ResponseEntity.ok().body(ApiResponse.success(orderService.getAllOrders(),"All Orders Fetched Successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createOrder(@RequestBody CreateOrderRequestDTO createOrderRequestDTO){
//        orderService.createOrder(createOrderRequestDTO);
        System.out.println("Inside createOrder");
        orderService.createOrder(createOrderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null,"Order Created Successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetOrderResponseDTO>> getOrderByOrderId(@PathVariable Long id){
//        return orderService.getOrderByOrderId(id);
        return ResponseEntity.ok().body(ApiResponse.success(orderService.getOrderByOrderId(id),"order data fetched successfully"));
    }


//    @GetMapping("/{id}")
//    public List<Orders> getAllOrderByUserId(@PathVariable Long id){
//        return orderService.getAllOrderByUserId(id);
//    }
}
