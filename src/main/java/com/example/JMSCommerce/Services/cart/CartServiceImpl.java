package com.example.JMSCommerce.Services.cart;

import com.example.JMSCommerce.DTOs.cart.*;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.CustomizationValidator;
import com.example.JMSCommerce.Model.ProductVariant;
import com.example.JMSCommerce.Utility.validation.CartValidator.QuantityValidator;
import com.example.JMSCommerce.Utility.validation.CartValidator.StockValidator;
import com.example.JMSCommerce.Utility.validation.VariantValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl
        implements CartService {

    private final CartRedisService cartRedisService;

    private final VariantValidator variantValidator;
    private final QuantityValidator quantityValidator;
    private final StockValidator stockValidator;
    private final CustomizationValidator customizationValidator;
    private final CartAssembler cartAssembler;
    @Override
    public CartResponseDTO addItem(
            String ownerId,
            AddCartItemRequestDTO request
    ) {

        quantityValidator.validate(
                request.getQuantity()
        );

        ProductVariant variant =
                variantValidator.validateAndGet(
                        request.getVariantId()
                );

        stockValidator.validate(
                variant,
                request.getQuantity()
        );

        customizationValidator.validateSelection(
                variant.getProduct(),
                request.getCustomizationOptionIds()
        );

        CartDTO cart =
                cartRedisService.getCart(ownerId);

        Optional<CartItemDTO> existing =
                findMatchingItem(
                        cart,
                        request
                );

        if (existing.isPresent()) {

            CartItemDTO item =
                    existing.get();

            int updatedQuantity =
                    item.getQuantity()
                            + request.getQuantity();

            stockValidator.validate(
                    variant,
                    updatedQuantity
            );

            item.setQuantity(updatedQuantity);

        } else {

            CartItemDTO item =
                    CartItemDTO.builder()
                            .id(
                                    UUID.randomUUID()
                                            .toString()
                            )
                            .variantId(
                                    variant.getId()
                            )
                            .quantity(
                                    request.getQuantity()
                            )
                            .customizationOptionIds(
                                    normalize(new ArrayList<>(
                                            request.getCustomizationOptionIds()
                                    ))
                            )
                            .build();

            cart.getItems().add(item);

        }

        cartRedisService.saveCart(cart);

        return cartAssembler.assemble(cart);

    }

    @Override
    @Transactional(readOnly = true)
    public CartResponseDTO getCart(
            String ownerId
    ) {

        CartDTO cart =
                cartRedisService.getCart(ownerId);

        return cartAssembler.assemble(cart);

    }

    @Override
    public CartResponseDTO updateItem(
            String ownerId,
            String cartItemId,
            UpdateCartItemRequestDTO request
    ) {

        quantityValidator.validate(
                request.getQuantity()
        );

        CartDTO cart =
                cartRedisService.getCart(ownerId);

        CartItemDTO item =
                getCartItem(
                        cart,
                        cartItemId
                );

        ProductVariant variant =
                variantValidator.validateAndGet(
                        item.getVariantId()
                );

        stockValidator.validate(
                variant,
                request.getQuantity()
        );

        customizationValidator.validateSelection(
                variant.getProduct(),
                request.getCustomizationOptionIds()
        );

        item.setQuantity(
                request.getQuantity()
        );

        item.setCustomizationOptionIds(
                normalize(
                        request.getCustomizationOptionIds()
                )
        );

        cartRedisService.saveCart(cart);

        return cartAssembler.assemble(cart);

    }

    @Override
    public CartResponseDTO removeItem(
            String ownerId,
            String cartItemId
    ) {

        CartDTO cart =
                cartRedisService.getCart(ownerId);

        cart.getItems()
                .removeIf(item ->
                        item.getId().equals(cartItemId)
                );

        cartRedisService.saveCart(cart);

        return cartAssembler.assemble(cart);

    }

    @Override
    public void clearCart(
            String ownerId
    ) {

        cartRedisService.deleteCart(
                ownerId
        );

    }

    // helper
    private Optional<CartItemDTO> findMatchingItem(
            CartDTO cart,
            AddCartItemRequestDTO request
    ) {

        List<Long> requested =
                normalize(request.getCustomizationOptionIds());

        return cart.getItems()
                .stream()
                .filter(item ->
                        item.getVariantId().equals(request.getVariantId())
                )
                .filter(item ->
                        normalize(item.getCustomizationOptionIds())
                                .equals(requested)
                )
                .findFirst();
    }

    private List<Long> normalize(
            List<Long> ids
    ) {

        return ids.stream()
                .sorted()
                .toList();

    }

    private CartItemDTO getCartItem(
            CartDTO cart,
            String cartItemId
    ) {

        return cart.getItems()
                .stream()
                .filter(item ->
                        item.getId().equals(cartItemId)
                )
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found."
                        )
                );

    }

}
