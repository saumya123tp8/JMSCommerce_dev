package com.example.JMSCommerce.Controller;

import com.example.JMSCommerce.DTOs.CreateOrderRequestDTO;
import com.example.JMSCommerce.DTOs.GetOrderResponseDTO;
import com.example.JMSCommerce.DTOs.UpdateOrderReqDTO;
import com.example.JMSCommerce.Model.Order;
import com.example.JMSCommerce.Services.OrderService;
import com.example.JMSCommerce.Utility.ApiResponse;
import com.example.JMSCommerce.Utility.AppConstants;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<List<GetOrderResponseDTO>>> getAllOrders(){
          return ResponseEntity.ok().body(ApiResponse.success(orderService.getAllOrders(),"All Orders Fetched Successfully"));
    }

    @PostMapping
    @PermitAll
    public ResponseEntity<ApiResponse<GetOrderResponseDTO>> createOrder(@RequestBody CreateOrderRequestDTO createOrderRequestDTO){
//        orderService.createOrder(createOrderRequestDTO);
        System.out.println("Inside createOrder");
        GetOrderResponseDTO getOrderResponseDTO= orderService.createOrder(createOrderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(getOrderResponseDTO,"Order Created Successfully"));
    }

    @GetMapping("by-order/{id}")
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<GetOrderResponseDTO>> getOrderByOrderId(@PathVariable Long id){
//        return orderService.getOrderByOrderId(id);
        return ResponseEntity.ok().body(ApiResponse.success(orderService.getOrderByOrderId(id),"order data fetched successfully"));
    }

    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<ApiResponse<GetOrderResponseDTO>> getOrderByOrderIdCurrUser(@PathVariable Long id){
//        return orderService.getOrderByOrderId(id);
        return ResponseEntity.ok().body(ApiResponse.success(orderService.getOrderByOrderIdCurrUser(id),"order data fetched successfully"));
    }

    @DeleteMapping("by_order/{id}")
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<Void>> deleteOrderByOrderId(@PathVariable Long id){
        return ResponseEntity.ok().body(ApiResponse.success(orderService.deleteOrderByOrderId(id),"order deleted successfully"));
    }

    @DeleteMapping("/{id}")
    @PermitAll
    public ResponseEntity<ApiResponse<Void>> deleteOrderByOrderIdCurrUser(@PathVariable Long id){
        return ResponseEntity.ok().body(ApiResponse.success(orderService.deleteOrderByOrderIdCurrUser(id),"order deleted successfully"));
    }

    @PutMapping("by-order/{id}")
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<GetOrderResponseDTO>> updateOrderByOrderId(@PathVariable Long id, @RequestBody UpdateOrderReqDTO updateOrderReqDTO){
        return ResponseEntity.ok().body(ApiResponse.success(orderService.updateOrderByOrderId(id,updateOrderReqDTO),"order deleted successfully"));
    }

    @PutMapping("/{id}")
    @PermitAll
    public ResponseEntity<ApiResponse<GetOrderResponseDTO>> updateOrderByOrderIdCurrUser(@PathVariable Long id, @RequestBody UpdateOrderReqDTO updateOrderReqDTO){
        return ResponseEntity.ok().body(ApiResponse.success(orderService.updateOrderByOrderIdCurrUser(id,updateOrderReqDTO),"order deleted successfully"));
    }

    // TODO //
    @GetMapping("/by-user/{id}")
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<List<GetOrderResponseDTO>>> getAllOrderByUserId(@PathVariable Long user_id){
        return ResponseEntity.ok().body(ApiResponse.success(orderService.getAllOrderByUserId(user_id),"order deleted successfully"));
    }

    // TODO //
    @GetMapping("/by-status/{id}")
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<List<GetOrderResponseDTO>>> getAllOrderByOrderStatus(@PathVariable String status){
        return ResponseEntity.ok().body(ApiResponse.success(orderService.getAllOrderByOrderStatus(status),"order deleted successfully"));
    }

}
