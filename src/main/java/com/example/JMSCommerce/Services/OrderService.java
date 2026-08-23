package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.Adapters.OrderAdapter;
import com.example.JMSCommerce.DTOs.cart.CartDTO;
import com.example.JMSCommerce.DTOs.cart.CartItemDTO;
import com.example.JMSCommerce.DTOs.order.CreateOrderRequestDTO;
import com.example.JMSCommerce.DTOs.order.GetOrderResponseDTO;
import com.example.JMSCommerce.DTOs.order.PlaceOrderRequestDTO;
import com.example.JMSCommerce.DTOs.order.UpdateOrderReqDTO;
import com.example.JMSCommerce.DTOs.payment.MockPaymentRequestDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Exception.CompulsoryDataMissingException;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.*;
import com.example.JMSCommerce.Repositories.*;
import com.example.JMSCommerce.Services.cart.CartOwnerProvider;
import com.example.JMSCommerce.Services.cart.CartRedisService;
import com.example.JMSCommerce.Utility.SecurityUtils;
import com.example.JMSCommerce.Utility.enums.OrderStatus;
import com.example.JMSCommerce.Utility.enums.PaymentMethod;
import com.example.JMSCommerce.Utility.enums.PaymentStatus;
import com.example.JMSCommerce.Utility.validation.CartValidator.StockValidator;
import com.example.JMSCommerce.Utility.validation.VariantValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final InventoryService inventoryService;
    private final PaymentRepository paymentRepository;
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final OrderAdapter orderAdapter;
    private final ProductRepo productRepo;
    private final OrderItemRepo orderItemRepo;
    private final AddressRepository addressRepository;

    private final CartRedisService cartRedisService;
    private final VariantValidator variantValidator;
    private final StockValidator stockValidator;
    private final CustomizationValidator customizationValidator;

    private final CustomizationOptionRepository customizationOptionRepository;
    private final OrderItemCustomizationRepository orderItemCustomizationRepository;

    private final CartOwnerProvider cartOwnerProvider;
    // also need to give some filter eg. address , status( we have ),

    public List<GetOrderResponseDTO> getAllOrders() {
        //we should have user_id and address to group all record
        List<Order> orders = orderRepo.findAll();
        return orderAdapter.mapToGetOrderResponseDTOList(orders);
    }

    @Transactional
    public GetOrderResponseDTO createOrder(CreateOrderRequestDTO createOrderRequestDTO) {

//        User user = userRepo.findById(createOrderRequestDTO.getUserId()).orElseThrow(
//                () -> new ResourceNotFoundException("User not found")
//        );
//
//        Order order = Order.builder()
//                .status(OrderStatus.Pending)
//                .deliveredAt(createOrderRequestDTO.getDeliveredAt())
//                .build();
//        if(createOrderRequestDTO.getProductsInOrder()!=null){
//            List<Long> listProductIds = createOrderRequestDTO.getProductsInOrder().stream().map(orderItemRequestDTO -> orderItemRequestDTO.getProductId() ).collect(Collectors.toList());
//            List<Product> listProduct = productRepo.findAllById(listProductIds);
//            Map<Long, Product> productMap = listProduct.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
//            for(Long id : listProductIds){
//                if(!productMap.containsKey(id)){
//
//                    throw new ResourceNotFoundException("Product not found with id: " + id);
//                }
//            }
//            List<OrderItem> listOrderItem = new ArrayList<>();
//            BigDecimal totalOrderPrice = BigDecimal.valueOf(0);
//            for(OrderItemRequestDTO itm : createOrderRequestDTO.getProductsInOrder()){
//                Product product = productMap.get(itm.getProductId());
//                Integer quantity = itm.getQuantity();
////                totalOrderPrice = totalOrderPrice.add(product.getMrp().multiply(BigDecimal.valueOf(quantity)));
//                OrderItem orderItem = OrderItem.builder()
//                    .quantity(itm.getQuantity())
//                    .product(product)
////                    .currentPrice(product.getMrp())
//                    .order(order)
//                    .build();
//                listOrderItem.add(orderItem);
//            }
//            order.setCurrentSubtotal(totalOrderPrice);
//            orderRepo.save(order);
//            orderItemRepo.saveAll(listOrderItem);
//        }
//       return orderAdapter.mapToGetOrderResponseDTO(order);
        return null;
    }


    @Transactional
    public Void deleteOrderByOrderId(Long id) {
//        Order order = orderRepo.findById(id).orElseThrow(
//                () -> new ResourceNotFoundException(
//                        "Order not found"
//                )
//        );
//        orderItemRepo.deleteByOrder_Id(id);
//        orderRepo.deleteById(id);
//        //order of deletion is important otherwise it will give fk_constraints error
        return null;
    }


    private GetOrderResponseDTO CommonMethodToUpdateOrder(UpdateOrderReqDTO updateOrderReqDTO, Order order) {
//        if(updateOrderReqDTO.getStatus()!=null){
//            order.setStatus(updateOrderReqDTO.getStatus());
//            orderRepo.save(order);
//        }
//
//        if(updateOrderReqDTO.getOrderItems() != null) {
//            List<Long> listProductIds = updateOrderReqDTO.getOrderItems().stream().map(item -> item.getProductId()).collect(Collectors.toList());
//
//            List<Product> products = productRepo.findAllById(listProductIds);
//
//            Map<Long, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
//
//            for(Long pid : listProductIds){
//                if(!productMap.containsKey(pid)){
//                    throw new ResourceNotFoundException("Product not found with id: " + pid);
//                }
//            }
//
//            List<OrderItem> toSave = new ArrayList<>();
//            List<OrderItem> toDelete = new ArrayList<>();
//
//            Map<Long, OrderItem> existingItems = orderItemRepo.findByOrderWithProduct(order).stream().collect(Collectors.toMap(op->op.getProduct().getId(),Function.identity()));
//            for(OrderItemActionDto itemAction : updateOrderReqDTO.getOrderItems()) {
//                Product product = productMap.get(itemAction.getProductId());
//
//                OrderItem existing = existingItems.get(product.getId());
//
//                switch(itemAction.getAction()) {
//                    case ADD -> {
//                        if(existing != null) {
//                            int addQty = (itemAction.getQuantity() != null ? itemAction.getQuantity() : 1);
//                            existing.setQuantity(existing.getQuantity() + addQty);
//                            toSave.add(existing);
//                        } else {
//                            OrderItem newItem = OrderItem
//                                    .builder()
//                                    .order(order)
//                                    .product(product)
//                                    .quantity(itemAction.getQuantity() != null ? itemAction.getQuantity() : 1)
//                                    .build();
//                            existingItems.put(product.getId(), newItem);
//                            toSave.add(newItem);
//                        }
//                    }
//                    case REMOVE -> {
//                        if(existing == null) {
//                            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
//                        }
//                        toDelete.add(existing);
//                        existingItems.remove(product.getId());
//                    }
//                    case INCREMENT -> {
//                        if(existing == null) {
//                            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
//                        }
//                        existing.setQuantity(existing.getQuantity() + 1);
//                        toSave.add(existing);
//
//                    }
//                    case DECREMENT -> {
//                        if(existing == null) {
//                            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
//                        }
//                        if(existing.getQuantity() <= 1) {
//                            toDelete.add(existing);
//                            existingItems.remove(product.getId());
//                        } else {
//                            existing.setQuantity(existing.getQuantity() - 1);
//                            toSave.add(existing);
//                        }
//
//
//                    }
//                }
//
//            }
//
//            if(!toSave.isEmpty()) {
//                orderItemRepo.saveAll(toSave);
//            }
//            if(!toDelete.isEmpty()) {
//                orderItemRepo.deleteAll(toDelete);
//            }
//        }
//        return orderAdapter.mapToGetOrderResponseDTO(order);
        return null;
    }

    public GetOrderResponseDTO updateOrderByOrderIdCurrUser(Long id, UpdateOrderReqDTO updateOrderReqDTO) {
//        Long currentUserId = SecurityUtils.getCurrentUserId();

//        String currUserMail = SecurityUtils.getCurrentUserMail();
//
//        Order order = orderRepo
//                .findByIdAndUser_email(id, currUserMail)
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
//
//        return CommonMethodToUpdateOrder(updateOrderReqDTO, order);
        return null;

    }


    public GetOrderResponseDTO updateOrderByOrderId(Long id, UpdateOrderReqDTO updateOrderReqDTO) {
//        Order order = orderRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("order not found"));
//        return CommonMethodToUpdateOrder(updateOrderReqDTO, order);
        return null;
    }


    public Void deleteOrderByOrderIdCurrUser(Long id) {
//        String currentUserMail = SecurityUtils.getCurrentUserMail();
//        Order order = orderRepo
//                .findByIdAndUser_email(id, currentUserMail)
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
//
//        orderRepo.delete(order);
        return null;
    }


    @Transactional
    public Void updateOrderStatus(
            Long id,
            UpdateOrderReqDTO request
    ) {

        if (request.getStatus() == null) {
            throw new CompulsoryDataMissingException(
                    "Status is missing"
            );
        }

        Order order =
                orderRepo.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                )
                        );


        order.setStatus(
                request.getStatus()
        );

        orderRepo.save(order);

        return null;
    }

    // this will check order and return only if order is related to the current authenticated user
    public GetOrderResponseDTO getOrderByOrderIdCurrUser(Long orderId) {
        String currentUserMail = SecurityUtils.getCurrentUserMail();
        Order order = orderRepo
                .findByIdAndUser_email(orderId, currentUserMail)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

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

    @Transactional
    public GetOrderResponseDTO placeOrder(PlaceOrderRequestDTO placeOrderRequestDTO) {

        String currUserEmail =
                SecurityUtils.getCurrentUserMail();

        User user =
                userRepo.findByEmail(currUserEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Current user not found."
                                )
                        );

        Address address =
                addressRepository.findByIdAndUser_Id(
                                placeOrderRequestDTO.getAddressId(),
                                user.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Delivery address not found."
                                )
                        );

        String ownerId = cartOwnerProvider.getOwnerId();
        CartDTO cart =
                cartRedisService.getCart(ownerId);

        if (cart.getItems() == null ||
                cart.getItems().isEmpty()) {

            throw new BadRequestException(
                    "Cart is empty."
            );
        }
//        cascade = CascadeType.ALL
//        saving the order will also save the OrderDeliveryAddress.

        OrderDeliveryAddress deliveryAddress =
                OrderDeliveryAddress.builder()
                        .receiverName(address.getReceiverName())
                        .receiverPhone(address.getReceiverPhone())
                        .countryCode(address.getCountryCode())
                        .houseNumber(address.getHouseNumber())
                        .apartment(address.getApartment())
                        .street(address.getStreet())
                        .landmark(address.getLandmark())
                        .city(address.getCity())
                        .state(address.getState())
                        .country(address.getCountry())
                        .pincode(address.getPincode())
                        .type(address.getType())
                        .deliveryInstructions(
                                address.getDeliveryInstructions()
                        )
                        .build();

        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .user(user)
                .deliveryAddress(deliveryAddress)
                .status(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .subtotal(BigDecimal.ZERO)
                .discount(BigDecimal.ZERO)
                .tax(BigDecimal.ZERO)
                .deliveryCharge(BigDecimal.ZERO)
                .grandTotal(BigDecimal.ZERO)
                .build();

        Order savedOrder =
                orderRepo.save(order);

        BigDecimal subtotal =
                BigDecimal.ZERO;

        for (CartItemDTO cartItem : cart.getItems()) {

            ProductVariant variant =
                    variantValidator.validateAndGet(
                            cartItem.getVariantId()
                    );

            stockValidator.validate(
                    variant,
                    cartItem.getQuantity()
            );
            inventoryService.reserve(
                    variant.getId(),
                    cartItem.getQuantity()
            );

            customizationValidator.validateSelection(
                    variant,
                    cartItem.getCustomizationOptionIds()
            );

            List<CustomizationOption> options =
                    customizationOptionRepository.findAllById(
                            cartItem.getCustomizationOptionIds()
                    );

            BigDecimal customizationPrice =
                    options.stream()
                            .map(CustomizationOption::getAdjustmentValue)
                            .reduce(
                                    BigDecimal.ZERO,
                                    BigDecimal::add
                            );

            BigDecimal unitPrice =
                    variant.getSellingPrice()
                            .add(customizationPrice);

            BigDecimal lineTotal =
                    unitPrice.multiply(
                            BigDecimal.valueOf(
                                    cartItem.getQuantity()
                            )
                    );

            OrderItem orderItem =
                    OrderItem.builder()
                            .order(savedOrder)
                            .variant(variant)
                            .productName(
                                    variant.getProduct().getName()
                            )
                            .variantName(
                                    variant.getDisplayName()
                            )
                            .sku(
                                    variant.getSku()
                            )
                            .quantity(
                                    cartItem.getQuantity()
                            )
                            .mrp(
                                    variant.getMrp()
                            )
                            .sellingPrice(
                                    variant.getSellingPrice()
                            )
                            .customizationPrice(
                                    customizationPrice
                            )
                            .totalPrice(
                                    lineTotal
                            )
                            .build();

            OrderItem savedOrderItem =
                    orderItemRepo.save(orderItem);

            for (CustomizationOption option : options) {

                OrderItemCustomization snapshot =
                        OrderItemCustomization.builder()
                                .orderItem(savedOrderItem)
                                .customizationOptionId(
                                        option.getId()
                                )
                                .name(
                                        option.getName()
                                )
                                .priceAdjustment(
                                        option.getAdjustmentValue()
                                )
                                .build();

                orderItemCustomizationRepository
                        .save(snapshot);
            }

            subtotal =
                    subtotal.add(lineTotal);
        }

        savedOrder.setSubtotal(subtotal);

        savedOrder.setGrandTotal(
                subtotal
                        .subtract(savedOrder.getDiscount())
                        .add(savedOrder.getTax())
                        .add(savedOrder.getDeliveryCharge())
        );

        Payment payment = Payment.builder()
                .order(savedOrder)
                .method(placeOrderRequestDTO.getPaymentMethod())
                .amount(savedOrder.getGrandTotal())
                .currency(savedOrder.getCurrency())
                .status(PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);

        if (placeOrderRequestDTO.getPaymentMethod() == PaymentMethod.COD) {

            savedOrder.setStatus(OrderStatus.CONFIRMED);
            savedOrder.setPaymentStatus(PaymentStatus.PENDING);

            // Payment Cash → cart can now be cleared
            cartRedisService.deleteCart(
                    ownerId
            );

        } else {

            savedOrder.setStatus(OrderStatus.PENDING);
            savedOrder.setPaymentStatus(PaymentStatus.PENDING);
        }

        Order finalOrder = orderRepo.save(savedOrder);

        return orderAdapter.mapToGetOrderResponseDTO(finalOrder);

    }

    private BigDecimal calculateCustomizationPrice(List<Long> customizationOptionIds) {
        if(customizationOptionIds == null || customizationOptionIds.isEmpty())return BigDecimal.ZERO;
        List<CustomizationOption> listOptions = customizationOptionRepository.findAllById(customizationOptionIds);
        if (listOptions.size() != customizationOptionIds.size()) {

            throw new ResourceNotFoundException(
                    "One or more customization options not found."
            );
        }
        return listOptions.stream().map(CustomizationOption::getAdjustmentValue).reduce(BigDecimal.ZERO,BigDecimal::add);
    }


    @Transactional
    public GetOrderResponseDTO cancelOrder(Long orderId) {

        String currentUserMail =
                SecurityUtils.getCurrentUserMail();

        Order order =
                orderRepo
                        .findByIdAndUser_email(
                                orderId,
                                currentUserMail
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                )
                        );

        OrderStatus currentStatus =
                order.getStatus();

        if (currentStatus == OrderStatus.CANCELLED) {

            throw new BadRequestException(
                    "Order is already cancelled."
            );
        }

        if (currentStatus == OrderStatus.SHIPPED ||
                currentStatus == OrderStatus.DELIVERED) {

            throw new BadRequestException(
                    "Order cannot be cancelled after it has been shipped."
            );
        }

        order.setStatus(
                OrderStatus.CANCELLED
        );

        Order savedOrder =
                orderRepo.save(order);

        return orderAdapter
                .mapToGetOrderResponseDTO(savedOrder);
    }


    // tesing : mock
    @Transactional
    public GetOrderResponseDTO payOrder(
            Long orderId,
            MockPaymentRequestDTO request
    ) {

        String currentUserMail =
                SecurityUtils.getCurrentUserMail();

        Order order =
                orderRepo.findByIdAndUser_email(
                                orderId,
                                currentUserMail
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                )
                        );

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException(
                    "Cancelled order cannot be paid."
            );
        }

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new BadRequestException(
                    "Order is already confirmed."
            );
        }

        if (order.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException(
                    "Payment has already been completed."
            );
        }

        if (order.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new BadRequestException(
                    "Payment cannot be processed for this order."
            );
        }

        if (request.isSuccess()) {

            order.setPaymentStatus(
                    PaymentStatus.SUCCESS
            );

            order.setStatus(
                    OrderStatus.CONFIRMED
            );

            Order savedOrder =
                    orderRepo.save(order);

            // Payment succeeded → cart can now be cleared
            String ownerId = cartOwnerProvider.getOwnerId();
            cartRedisService.deleteCart(
                    ownerId
            );

            return orderAdapter
                    .mapToGetOrderResponseDTO(savedOrder);
        }

        order.setPaymentStatus(
                PaymentStatus.FAILED
        );

        Order savedOrder =
                orderRepo.save(order);

        return orderAdapter
                .mapToGetOrderResponseDTO(savedOrder);
    }


    // testing : mock
    @Transactional
    public GetOrderResponseDTO retryPayment(
            Long orderId,
            MockPaymentRequestDTO request
    ) {

        String currentUserMail =
                SecurityUtils.getCurrentUserMail();

        Order order =
                orderRepo.findByIdAndUser_email(
                                orderId,
                                currentUserMail
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                )
                        );

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException(
                    "Cancelled order cannot be paid."
            );
        }

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new BadRequestException(
                    "Order is already confirmed."
            );
        }

        if (order.getPaymentStatus() != PaymentStatus.FAILED) {
            throw new BadRequestException(
                    "Payment retry is allowed only for failed payments."
            );
        }

        // Start another payment attempt
        order.setPaymentStatus(
                PaymentStatus.PENDING
        );

        if (request.isSuccess()) {

            order.setPaymentStatus(
                    PaymentStatus.SUCCESS
            );

            order.setStatus(
                    OrderStatus.CONFIRMED
            );

            Order savedOrder =
                    orderRepo.save(order);

            String ownerId = cartOwnerProvider.getOwnerId();
            cartRedisService.deleteCart(
                    ownerId
            );

            return orderAdapter
                    .mapToGetOrderResponseDTO(savedOrder);
        }

        order.setPaymentStatus(
                PaymentStatus.FAILED
        );

        Order savedOrder =
                orderRepo.save(order);

        return orderAdapter
                .mapToGetOrderResponseDTO(savedOrder);
    }

    public List<GetOrderResponseDTO> getAllOrderByCurrUser() {
        String currentUserMail = SecurityUtils.getCurrentUserMail();
        List<Order> orderList = orderRepo
                .findByUser_email(currentUserMail);

        return orderList.stream().map(order->orderAdapter.mapToGetOrderResponseDTO(order)).collect(Collectors.toList());
    }

    private String generateOrderNumber() {

        return "JMS-" +
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "yyyyMMddHHmmssSSS"
                                )
                        ) +
                "-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 6)
                        .toUpperCase();
    }

    public Void updateOrderStatusAdmin(Long id,UpdateOrderReqDTO request) {
        Order order = orderRepo.findById(id).orElseThrow(()->{
           throw new ResourceNotFoundException("No such order") ;
        });
        // check valid state transition
        OrderStatus finalOrderStatus;
        OrderStatus currOrderStatus = order.getStatus();
        finalOrderStatus = request.getStatus();
        if(!currOrderStatus.canTransitionTo(finalOrderStatus)){
            throw new IllegalStateException(
                    String.format("Cannot transition order status from %s to %s", currOrderStatus, finalOrderStatus)
            );
        }
        order.setStatus(finalOrderStatus);
        orderRepo.save(order);
        return null;
    }

    // OrderService.java
    public GetOrderResponseDTO decideRefund(Long orderId, boolean approve, String reason) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.CANCELLED) {
            throw new IllegalStateException("Only cancelled orders can be reviewed for refund.");
        }
        if (order.getPaymentStatus() != PaymentStatus.SUCCESS) {
            throw new IllegalStateException("Only successful payments can be refunded.");
        }

        if (approve) {
            // TODO: real payment gateway refund call goes here before
            // committing this status change.
            order.setPaymentStatus(PaymentStatus.REFUNDED);
        }
        // TODO: persist `reason` to an audit/refund-log table once one
        // exists — currently not stored anywhere on the Order entity.

        order.setUpdatedAt(LocalDateTime.now());
        orderRepo.save(order);

        return orderAdapter.mapToGetOrderResponseDTO(order);
    }
}
