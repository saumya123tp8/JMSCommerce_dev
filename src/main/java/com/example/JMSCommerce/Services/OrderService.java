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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

    @Transactional
    public GetOrderResponseDTO createOrder(CreateOrderRequestDTO createOrderRequestDTO) {

        User user = userRepo.findById(createOrderRequestDTO.getUserId()).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        Order order = Order.builder()
                .status(OrderStatus.Pending)
                .deliveredAt(createOrderRequestDTO.getDeliveredAt())
                .build();
        // it will cause N+1 issue
//        List<OrderProduct> listOrderItem = createOrderRequestDTO.getProductsInOrder().stream().map(orderItemRequestDTO -> {
//            Product product = productRepo.findById(orderItemRequestDTO.getProductId()).orElseThrow(() -> new RuntimeException("Inavalid product selected"));
//            OrderProduct orderProduct = OrderProduct.builder()
//                    .quantity(orderItemRequestDTO.getQuantity())
//                    .product(product)
//                    .currentPrice(product.getPrice())
//                    .order(order)
//                    .build();
//            return orderProduct;
//        }).collect(Collectors.toList());
//        BigDecimal totalOrderPrice = listOrderItem.stream()
//                .map(op -> op.getCurrentPrice()
//                        .multiply(BigDecimal.valueOf(op.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        order.setCurrentSubtotal(totalOrderPrice);
//        orderRepo.save(order);
//        orderProductRepo.saveAll(listOrderItem);

        //solution to n+1 issue

        if(createOrderRequestDTO.getProductsInOrder()!=null){
            List<Long> listProductIds = createOrderRequestDTO.getProductsInOrder().stream().map(orderItemRequestDTO -> orderItemRequestDTO.getProductId() ).collect(Collectors.toList());
            List<Product> listProduct = productRepo.findAllById(listProductIds);
            Map<Long, Product> productMap = listProduct.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
            for(Long id : listProductIds){
                if(!productMap.containsKey(id)){

                    throw new RuntimeException("Product not found with id: " + id);
                }
            }
            List<OrderProduct> listOrderProduct = new ArrayList<>();
            BigDecimal totalOrderPrice = BigDecimal.valueOf(0);
            for(OrderItemRequestDTO itm : createOrderRequestDTO.getProductsInOrder()){
                Product product = productMap.get(itm.getProductId());
                Integer quantity = itm.getQuantity();
                totalOrderPrice = totalOrderPrice.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
                OrderProduct orderProduct = OrderProduct.builder()
                    .quantity(itm.getQuantity())
                    .product(product)
                    .currentPrice(product.getPrice())
                    .order(order)
                    .build();
                listOrderProduct.add(orderProduct);
            }
            order.setCurrentSubtotal(totalOrderPrice);
            orderRepo.save(order);
            orderProductRepo.saveAll(listOrderProduct);
        }
        System.out.println("order"+order);
        System.out.println("order_product");
       return orderAdapter.mapToGetOrderResponseDTO(order);
    }

    public GetOrderResponseDTO getOrderByOrderId(Long id) {
        Order order = orderRepo.findById(id).orElseThrow(
                () -> new RuntimeException(
                        "Order not found"
                )
        );
        System.out.println("order");
        System.out.println(order.getId());
        return orderAdapter.mapToGetOrderResponseDTO(order);
    }

//    public List<Orders> getAllOrderByUserId(Long id) {
//        userRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        return orderRepo.findByUserId(id);
//    }

}
