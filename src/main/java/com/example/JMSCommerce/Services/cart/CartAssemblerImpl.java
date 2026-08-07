package com.example.JMSCommerce.Services.cart;

import com.example.JMSCommerce.DTOs.cart.CartDTO;
import com.example.JMSCommerce.DTOs.cart.CartItemDTO;
import com.example.JMSCommerce.DTOs.cart.CartItemResponseDTO;
import com.example.JMSCommerce.DTOs.cart.CartResponseDTO;
import com.example.JMSCommerce.Model.CustomizationOption;
import com.example.JMSCommerce.Model.ProductVariant;
import com.example.JMSCommerce.Repositories.CustomizationOptionRepository;
import com.example.JMSCommerce.Repositories.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartAssemblerImpl
        implements CartAssembler {

    private final ProductVariantRepository variantRepository;

    private final CustomizationOptionRepository optionRepository;

    @Override
    public CartResponseDTO assemble(
            CartDTO cart
    ) {

        Map<Long, ProductVariant> variantMap =
                loadVariants(cart);

        Map<Long, CustomizationOption> optionMap =
                loadCustomizationOptions(cart);

        List<CartItemResponseDTO> items =
                cart.getItems()
                        .stream()
                        .map(item ->
                                assembleItem(
                                        item,
                                        variantMap,
                                        optionMap
                                )
                        )
                        .toList();

        BigDecimal subtotal = items.stream()
                .map(CartItemResponseDTO::getTotalPrice)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        return CartResponseDTO.builder()
                .items(items)
                .subtotal(subtotal)
                .discount(BigDecimal.ZERO)
                .tax(BigDecimal.ZERO)
                .deliveryCharge(BigDecimal.ZERO)
                .grandTotal(subtotal)
                .totalQuantity(
                        items.stream()
                                .mapToInt(
                                        CartItemResponseDTO::getQuantity
                                )
                                .sum()
                )
                .build();

    }

    private CartItemResponseDTO assembleItem(
            CartItemDTO item,
            Map<Long, ProductVariant> variantMap,
            Map<Long, CustomizationOption> optionMap
    ) {

        ProductVariant variant =
                variantMap.get(item.getVariantId());

        List<CustomizationOption> options =
                item.getCustomizationOptionIds()
                        .stream()
                        .map(optionMap::get)
                        .filter(Objects::nonNull)
                        .toList();

        BigDecimal customizationPrice =
                options.stream()
                        .map(CustomizationOption::getAdjustmentValue)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal unitPrice =
                variant.getSellingPrice();

        BigDecimal totalPrice =
                unitPrice
                        .add(customizationPrice)
                        .multiply(
                                BigDecimal.valueOf(
                                        item.getQuantity()
                                )
                        );

        return CartItemResponseDTO.builder()
                .id(item.getId())
                .variantId(variant.getId())
                .productId(variant.getProduct().getId())
                .productName(variant.getProduct().getName())
                .variantName(variant.getDisplayName())
                .image(variant.getProduct().getPrimaryImage())
                .quantity(item.getQuantity())
                .unitPrice(unitPrice)
                .customizationPrice(customizationPrice)
                .totalPrice(totalPrice)
                .availableStock(variant.getStock())
                .available(variant.getActive())
                .selectedCustomizations(
                        options.stream()
                                .map(CustomizationOption::getName)
                                .toList()
                )
                .build();

    }

    private Map<Long, ProductVariant> loadVariants(
            CartDTO cart
    ) {

        Set<Long> ids =
                cart.getItems()
                        .stream()
                        .map(CartItemDTO::getVariantId)
                        .collect(Collectors.toSet());

        return variantRepository
                .findAllById(ids)
                .stream()
                .collect(Collectors.toMap(
                        ProductVariant::getId,
                        Function.identity()
                ));

    }

    private Map<Long, CustomizationOption> loadCustomizationOptions(
            CartDTO cart
    ) {

        Set<Long> ids =
                cart.getItems()
                        .stream()
                        .flatMap(item ->
                                item.getCustomizationOptionIds()
                                        .stream()
                        )
                        .collect(Collectors.toSet());

        return optionRepository
                .findAllById(ids)
                .stream()
                .collect(Collectors.toMap(
                        CustomizationOption::getId,
                        Function.identity()
                ));

    }

}
