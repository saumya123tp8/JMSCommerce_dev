package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.Adapters.OrderAdapter;
import com.example.JMSCommerce.DTOs.*;
import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Exception.CompulsoryDataMissingException;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.*;
import com.example.JMSCommerce.Repositories.OrderProductRepo;
import com.example.JMSCommerce.Repositories.OrderRepo;
import com.example.JMSCommerce.Repositories.ProductRepo;
import com.example.JMSCommerce.Repositories.UserRepo;
import com.example.JMSCommerce.Utility.SecurityUtils;
import com.example.JMSCommerce.Utility.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
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
                () -> new ResourceNotFoundException("User not found")
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
       return orderAdapter.mapToGetOrderResponseDTO(order);
    }

    public GetOrderResponseDTO getOrderByOrderId(Long id) {
        Order order = orderRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Order not found"
                )
        );
        return orderAdapter.mapToGetOrderResponseDTO(order);
    }

    @Transactional
    public Void deleteOrderByOrderId(Long id) {
        Order order = orderRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Order not found"
                )
        );
        orderProductRepo.deleteByOrder_Id(id);
        orderRepo.deleteById(id);
        //order of deletion is important otherwise it will give fk_constraints error
        return null;
    }




    private GetOrderResponseDTO CommonMethodToUpdateOrder(UpdateOrderReqDTO updateOrderReqDTO, Order order) {
        if(updateOrderReqDTO.getStatus()!=null){
            order.setStatus(updateOrderReqDTO.getStatus());
            orderRepo.save(order);
        }

        if(updateOrderReqDTO.getOrderItems() != null) {
            List<Long> listProductIds = updateOrderReqDTO.getOrderItems().stream().map(item -> item.getProductId()).collect(Collectors.toList());

            List<Product> products = productRepo.findAllById(listProductIds);

            Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));

            for(Long pid : listProductIds){
                if(!productMap.containsKey(pid)){
                    throw new ResourceNotFoundException("Product not found with id: " + pid);
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
                            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
                        }
                        toDelete.add(existing);
                        existingItems.remove(product.getId());
                    }
                    case INCREMENT -> {
                        if(existing == null) {
                            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
                        }
                        existing.setQuantity(existing.getQuantity() + 1);
                        toSave.add(existing);

                    }
                    case DECREMENT -> {
                        if(existing == null) {
                            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
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

    public GetOrderResponseDTO updateOrderByOrderIdCurrUser(Long id, UpdateOrderReqDTO updateOrderReqDTO) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Order order = orderRepo
                .findByIdAndUser_Id(id, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return CommonMethodToUpdateOrder(updateOrderReqDTO, order);

    }


    public GetOrderResponseDTO updateOrderByOrderId(Long id,UpdateOrderReqDTO updateOrderReqDTO) {
        Order order = orderRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("order not found"));
        return CommonMethodToUpdateOrder(updateOrderReqDTO, order);
    }

    public Void updateOrderStatus(Long id,UpdateOrderReqDTO updateOrderReqDTO) {
        Order order = orderRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("order not found"));
        if(updateOrderReqDTO.getStatus()!=null){
            order.setStatus(updateOrderReqDTO.getStatus());
            orderRepo.save(order);
        }else{
            throw new CompulsoryDataMissingException("Status is missing");
        }
        return null;
    }

    // this will check order and return only if order is related to the current authenticated user
    public GetOrderResponseDTO getOrderByOrderIdCurrUser(Long orderId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Order order = orderRepo
                .findByIdAndUser_Id(orderId, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return orderAdapter.mapToGetOrderResponseDTO(order);
    }

    public Void deleteOrderByOrderIdCurrUser(Long id) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        Order order = orderRepo
                .findByIdAndUser_Id(id, currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        orderRepo.delete(order);
        return null;
    }


    public List<GetOrderResponseDTO> getAllOrderByUserId(Long userId) {

        List<Order> orders = orderRepo.findAllByUser_Id(userId);

        return orders.stream()
                .map(orderAdapter::mapToGetOrderResponseDTO)
                .toList();

    }

    public List<GetOrderResponseDTO> getAllOrderByOrderStatus(String status) {
        OrderStatus orderStatus;

        try {
            orderStatus = OrderStatus.valueOf(status.toUpperCase().trim());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid order status : " + status);
        }

        List<Order> orders = orderRepo.findAllByStatus(orderStatus);

        return orders.stream()
                .map(orderAdapter::mapToGetOrderResponseDTO)
                .toList();
    }
}
