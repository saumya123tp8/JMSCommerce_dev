package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.Adapters.OrderAdapter;
import com.example.JMSCommerce.DTOs.*;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
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
        if(createOrderRequestDTO.getProductsInOrder()!=null){
            List<Long> listProductIds = createOrderRequestDTO.getProductsInOrder().stream().map(orderItemRequestDTO -> orderItemRequestDTO.getProductId() ).collect(Collectors.toList());
            List<Product> listProduct = productRepo.findAllById(listProductIds);
            Map<Long, Product> productMap = listProduct.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
            for(Long id : listProductIds){
                if(!productMap.containsKey(id)){

                    throw new ResourceNotFoundException("Product not found with id: " + id);
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

    @Transactional
    public Void deleteOrderByOrderId(Long id) {
        orderProductRepo.deleteByOrderId(id);
        orderRepo.deleteById(id);
        //order of deletion is important otherwise it will give fk_constraints error
        return null;
    }

    public GetOrderResponseDTO updateOrderByOrderId(Long id,UpdateOrderReqDTO updateOrderReqDTO) {
        Order order = orderRepo.findById(id).orElseThrow(()->new RuntimeException("order not found"));
        if(updateOrderReqDTO.getStatus()!=null){
            order.setStatus(updateOrderReqDTO.getStatus());
            orderRepo.save(order);
        }

        if(updateOrderReqDTO.getOrderItems() != null) {
            List<Long> listProductIds = updateOrderReqDTO.getOrderItems().stream().map(item -> item.getProductId()).collect(Collectors.toList());

            List<Product> products = productRepo.findAllById(listProductIds);

            Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));

            for(Long pid : listProductIds){
                if(!productMap.containsKey(id)){
                    throw new RuntimeException("Product not found with id: " + id);
                }
            }

            List<OrderProduct> toSave = new ArrayList<>();
            List<OrderProduct> toDelete = new ArrayList<>();

            Map<Long, OrderProduct> existingItems = orderProductRepo.findByOrderWithProduct(order).stream().collect(Collectors.toMap(op->op.getProduct().getId(),Function.identity()));
            for(OrderItemActionDto itemAction : updateOrderReqDTO.getOrderItems()) {
                Product product = productMap.get(itemAction.getProductId());

                OrderProduct existing = existingItems.get(product.getId());

                switch(itemAction.getAction()) {
                    case ADD -> {
                        if(existing != null) {
                            int addQty = (itemAction.getQuantity() != null ? itemAction.getQuantity() : 1);
                            existing.setQuantity(existing.getQuantity() + addQty);
                            toSave.add(existing);
                        } else {
                            OrderProduct newItem = OrderProduct
                                    .builder()
                                    .order(order)
                                    .product(product)
                                    .quantity(itemAction.getQuantity() != null ? itemAction.getQuantity() : 1)
                                    .build();
                            existingItems.put(product.getId(), newItem);
                            toSave.add(newItem);
                        }
                    }
                    case REMOVE -> {
                        if(existing == null) {
                            throw new RuntimeException("Product not found with id: " + product.getId());
                        }
                        toDelete.add(existing);
                        existingItems.remove(product.getId());
                    }
                    case INCREMENT -> {
                        if(existing == null) {
                            throw new RuntimeException("Product not found with id: " + product.getId());
                        }
                        existing.setQuantity(existing.getQuantity() + 1);
                        toSave.add(existing);

                    }
                    case DECREMENT -> {
                        if(existing == null) {
                            throw new RuntimeException("Product not found with id: " + product.getId());
                        }
                        if(existing.getQuantity() <= 1) {
                            toDelete.add(existing);
                            existingItems.remove(product.getId());
                        } else {
                            existing.setQuantity(existing.getQuantity() - 1);
                            toSave.add(existing);
                        }


                    }
                }

            }

            if(!toSave.isEmpty()) {
                orderProductRepo.saveAll(toSave);
            }
            if(!toDelete.isEmpty()) {
                orderProductRepo.deleteAll(toDelete);
            }
        }
            return orderAdapter.mapToGetOrderResponseDTO(order);
    }

//    public List<Orders> getAllOrderByUserId(Long id) {
//        userRepo.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        return orderRepo.findByUserId(id);
//    }



}
