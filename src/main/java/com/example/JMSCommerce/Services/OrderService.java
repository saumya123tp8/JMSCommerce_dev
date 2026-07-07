package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.Adapters.OrderAdapter;
import com.example.JMSCommerce.DTOs.CreateOrderRequestDTO;
import com.example.JMSCommerce.DTOs.GetOrderResponseDTO;
import com.example.JMSCommerce.DTOs.OrderItemRequestDTO;
import com.example.JMSCommerce.Model.*;
import com.example.JMSCommerce.Repositories.OrderProductRepo;
import com.example.JMSCommerce.Repositories.OrderRepo;
import com.example.JMSCommerce.Repositories.ProductRepo;
import com.example.JMSCommerce.Repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final OrderAdapter orderAdapter;
    private final ProductRepo productRepo;
    private final OrderProductRepo orderProductRepo;
    public List<GetOrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepo.findAll();
        return orderAdapter.mapToGetOrderResponseDTOList(orders);
    }

    public void createOrder(CreateOrderRequestDTO createOrderRequestDTO) {

     User user = userRepo.findById(createOrderRequestDTO.getUserId()).orElseThrow(
            ()->new RuntimeException("User not found")
      );


      List<OrderProduct> listOrderItem = createOrderRequestDTO.getProductsInOrder().stream().map(orderItemRequestDTO->{
          Product product = productRepo.findById(orderItemRequestDTO.getProductId()).orElseThrow(()->new RuntimeException("Inavalid product selected"));
          OrderProduct orderProduct = OrderProduct.builder()
                  .quantity(orderItemRequestDTO.getQuantity())
                  .product(product)
                  .currentPrice(product.getPrice())
//                  .currentDiscount(product.getCurrentDiscount())
                  .build();
         return orderProduct;
      }).collect(Collectors.toList());
        BigDecimal totalOrderPrice=listOrderItem.stream()
                .map(op -> op.getCurrentPrice()
                        .multiply(BigDecimal.valueOf(op.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Order order = Order.builder()
                .status(OrderStatus.Pending)
                .deliveredAt(createOrderRequestDTO.getDeliveredAt())
                .currentSubtotal(totalOrderPrice)
                .build();
        orderRepo.save(order);
        orderProductRepo.saveAll(listOrderItem);

    }

    public Order getOrderByOrderId(Long id) {
        return orderRepo.findById(id).orElseThrow(
                () -> new RuntimeException(
                        "Order not found"
                )
        );
    }

//    public List<Orders> getAllOrderByUserId(Long id) {
//        userRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        return orderRepo.findByUserId(id);
//    }

}
