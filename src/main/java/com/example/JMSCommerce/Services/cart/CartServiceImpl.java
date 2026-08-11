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

import java.util.*;

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
//                variant.getProduct(),
                variant,
                request.getCustomizationOptionIds()
        );

        CartDTO cart =
                cartRedisService.getCart(ownerId);

        Optional<CartItemDTO> existing =
                findMatchingItem(

                        cart,
                        variant.getId(),
                        request.getCustomizationOptionIds(),
                        null
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



//        if(request.getCustomizationOptionIds()!=null) {
// but I have validate even empty because there can be mandatory customization
            customizationValidator.validateSelection(
//                variant.getProduct(),
                    variant,
                    request.getCustomizationOptionIds()
            );
//        }



        List<Long> updatedOptions =
                normalize(request.getCustomizationOptionIds());
        Optional<CartItemDTO> duplicate =
                findMatchingItem(

                        cart,
                        variant.getId(),
                        updatedOptions,
                        item.getId()
                );
        if (duplicate.isPresent()) {

            CartItemDTO existing =
                    duplicate.get();

            int mergedQuantity =
                    existing.getQuantity()
                            + request.getQuantity();

            stockValidator.validate(
                    variant,
                    mergedQuantity
            );

            existing.setQuantity(
                    mergedQuantity
            );

            cart.getItems().remove(item);

        }else{
            stockValidator.validate(
                    variant,
                    request.getQuantity()
            );
            item.setQuantity(
                    request.getQuantity()
            );
            item.setCustomizationOptionIds(updatedOptions);
        }
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
            Long variantId,
            List<Long> customizationOptionIds,
            String excludedItemId
    ) {

        List<Long> normalized = normalize(customizationOptionIds);

        return cart.getItems()
                .stream()
                .filter(item ->
                        excludedItemId == null
                                || !item.getId().equals(excludedItemId)
                )
                .filter(item ->
                        item.getVariantId().equals(variantId)
                )
                .filter(item ->
                        item.getCustomizationOptionIds()
                                .equals(normalized)
                )
                .findFirst();

    }

    private List<Long> normalize(
            List<Long> ids
    ) {

        if (ids == null) {
            return Collections.emptyList();
        }
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
