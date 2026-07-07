package com.example.JMSCommerce.Adapters;

import com.example.JMSCommerce.DTOs.GetOrderResponseDTO;
import com.example.JMSCommerce.DTOs.OrderItemResponseDTO;
import com.example.JMSCommerce.Model.OrderProduct;
import com.example.JMSCommerce.Model.Order;
import com.example.JMSCommerce.Repositories.OrderProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderAdapter {
    private final OrderProductRepo orderProductRepo;
    public List<GetOrderResponseDTO> mapToGetOrderResponseDTOList(List<Order> orders) {
        return orders.stream().map(this::mapToGetOrderResponseDTO).collect(Collectors.toList());
    }
    public GetOrderResponseDTO mapToGetOrderResponseDTO(Order order) {
       List<OrderProduct> listOrderItem = new ArrayList<>();
       listOrderItem = orderProductRepo.findByOrderId(order.getId());
        System.out.println("order_product");
//        System.out.println(listOrderItem);
        listOrderItem.forEach(System.out::println);
       List<OrderItemResponseDTO> OrderItems = mapToOrderItemResponseDTOList(listOrderItem);
        OrderItems.forEach(System.out::println);
       return GetOrderResponseDTO.builder()
               .orderStatus(String.valueOf(order.getStatus()))
               .createdAt(order.getCreatedAt())
               .updatedAt(order.getUpdatedAt())
               .deliveredAt((order.getDeliveredAt()))
               .currentSubtotal(order.getCurrentSubtotal())
               .orderItems(OrderItems)
               .build();

    }

    private List<OrderItemResponseDTO> mapToOrderItemResponseDTOList(List<OrderProduct> listOrderItem) {

        return listOrderItem.stream().map(this::mapToOrderItemResponseDTO).collect(Collectors.toList());

    }

    private OrderItemResponseDTO mapToOrderItemResponseDTO(OrderProduct orderProduct) {
        return OrderItemResponseDTO.builder()
                .productId(orderProduct.getProduct().getId())
                .quantity(orderProduct.getQuantity())
                .productImage((orderProduct.getProduct().getImage()))
                .productName(orderProduct.getProduct().getTitle())
                .productPrice(orderProduct.getProduct().getPrice())
                .subTotal((orderProduct.getProduct().getPrice()).multiply(BigDecimal.valueOf(orderProduct.getQuantity())))
                .build();
    }
}
