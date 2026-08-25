package com.example.JMSCommerce.Adapters;

import com.example.JMSCommerce.DTOs.order.GetOrderResponseDTO;
import com.example.JMSCommerce.DTOs.order.OrderDeliveryAddressResponseDTO;
import com.example.JMSCommerce.DTOs.order.OrderItemCustomizationResponseDTO;
import com.example.JMSCommerce.DTOs.order.OrderItemResponseDTO;
import com.example.JMSCommerce.Model.*;
import com.example.JMSCommerce.Repositories.OrderItemCustomizationRepository;
import com.example.JMSCommerce.Repositories.OrderItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderAdapter {
    private final OrderItemRepo orderItemRepo;
    private final OrderItemCustomizationRepository orderItemCustomizationRepository;
    public List<GetOrderResponseDTO> mapToGetOrderResponseDTOList(List<Order> orders) {
        return orders.stream().map(this::mapToGetOrderResponseDTO).collect(Collectors.toList());
    }
    public OrderItemCustomizationResponseDTO mapToOrderItemCustomizationResponseDTO(OrderItemCustomization orderItemCustomization){
        return OrderItemCustomizationResponseDTO.builder()
                .customizationOptionId(orderItemCustomization.getCustomizationOptionId())
                .name(orderItemCustomization.getName())
                .priceAdjustment(orderItemCustomization.getPriceAdjustment())
                .build();
    }
    public List<OrderItemCustomizationResponseDTO> mapToOrderItemCustomizationResponseListDTO(List<OrderItemCustomization> orderItemCustomizationList){
       return orderItemCustomizationList.stream().map(orderItemCustomization -> mapToOrderItemCustomizationResponseDTO(orderItemCustomization)).collect(Collectors.toList());
    }
    public GetOrderResponseDTO mapToGetOrderResponseDTO(Order order) {

        List<OrderItem> orderItems =
                orderItemRepo.findByOrder_Id(order.getId());

        List<Long> orderItemIds =
                orderItems.stream()
                        .map(OrderItem::getId)
                        .toList();

        List<OrderItemCustomization> customizations =
                orderItemIds.isEmpty()
                        ? List.of()
                        : orderItemCustomizationRepository
                        .findAllByOrderItem_IdIn(orderItemIds);

        Map<Long, List<OrderItemCustomization>> customizationMap =
                customizations.stream()
                        .collect(
                                Collectors.groupingBy(
                                        customization ->
                                                customization
                                                        .getOrderItem()
                                                        .getId()
                                )
                        );

        List<OrderItemResponseDTO> orderItemResponses =
                orderItems.stream()
                        .map(orderItem ->
                                mapToOrderItemResponseDTO(
                                        orderItem,
                                        customizationMap
                                )
                        )
                        .toList();

        return GetOrderResponseDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .userName(order.getUser().getName())
                .userEmail(order.getUser().getEmail())
                .orderStatus(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .orderNumber(order.getOrderNumber())
                .deliveryAddress(
                        mapToDeliveryAddressResponseDTO(
                                order.getDeliveryAddress()
                        )
                )
                .subtotal(order.getSubtotal())
                .discount(order.getDiscount())
                .tax(order.getTax())
                .deliveryCharge(order.getDeliveryCharge())
                .grandTotal(order.getGrandTotal())
                .orderItems(orderItemResponses)
                .build();
    }


    private OrderItemResponseDTO mapToOrderItemResponseDTO(
            OrderItem orderItem,
            Map<Long, List<OrderItemCustomization>> customizationMap
    ) {

        ProductVariant variant =
                orderItem.getVariant();

        List<OrderItemCustomizationResponseDTO> customizations =
                customizationMap
                        .getOrDefault(
                                orderItem.getId(),
                                List.of()
                        )
                        .stream()
                        .map(this::mapToOrderItemCustomizationResponseDTO)
                        .toList();

        return OrderItemResponseDTO.builder()
                .orderItemId(orderItem.getId())
                .productId(
                        variant.getProduct().getId()
                )
                .variantId(
                        variant.getId()
                )
                .productName(
                        orderItem.getProductName()
                )
                .variantName(
                        orderItem.getVariantName()
                )
                .sku(
                        orderItem.getSku()
                )
                .quantity(
                        orderItem.getQuantity()
                )
                .mrp(
                        orderItem.getMrp()
                )
                .sellingPrice(
                        orderItem.getSellingPrice()
                )
                .customizationPrice(
                        orderItem.getCustomizationPrice()
                )
                .subTotal(
                        orderItem.getTotalPrice()
                )
                .productImage(
                        variant.getProduct().getPrimaryImage()
                )
                .customizations(customizations)
                .build();
    }

    private OrderDeliveryAddressResponseDTO
    mapToDeliveryAddressResponseDTO(
            OrderDeliveryAddress address
    ) {

        if (address == null) {
            return null;
        }

        return OrderDeliveryAddressResponseDTO.builder()
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
    }
}
