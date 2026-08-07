package com.example.JMSCommerce.Controller;

import com.example.JMSCommerce.DTOs.cart.AddCartItemRequestDTO;
import com.example.JMSCommerce.DTOs.cart.CartResponseDTO;
import com.example.JMSCommerce.DTOs.cart.UpdateCartItemRequestDTO;
import com.example.JMSCommerce.Services.cart.CartOwnerProvider;
import com.example.JMSCommerce.Services.cart.CartService;
import com.example.JMSCommerce.Utility.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartOwnerProvider cartOwnerProvider;

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponseDTO>> addItem(
            @Valid
            @RequestBody
            AddCartItemRequestDTO request
    ) {

        String ownerId = cartOwnerProvider.getOwnerId();

        return ResponseEntity.ok(
                ApiResponse.success(
                        cartService.addItem(
                                ownerId,
                                request
                        ),
                        "Item added to cart successfully."
                )
        );

    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDTO>> getCart() {

        String ownerId = cartOwnerProvider.getOwnerId();

        return ResponseEntity.ok(
                ApiResponse.success(
                        cartService.getCart(ownerId),
                        "Cart fetched successfully."
                )
        );

    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> updateItem(
            @PathVariable
            String cartItemId,

            @Valid
            @RequestBody
            UpdateCartItemRequestDTO request
    ) {

        String ownerId = cartOwnerProvider.getOwnerId();

        return ResponseEntity.ok(
                ApiResponse.success(
                        cartService.updateItem(
                                ownerId,
                                cartItemId,
                                request
                        ),
                        "Cart updated successfully."
                )
        );

    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> removeItem(
            @PathVariable
            String cartItemId
    ) {

        String ownerId = cartOwnerProvider.getOwnerId();

        return ResponseEntity.ok(
                ApiResponse.success(
                        cartService.removeItem(
                                ownerId,
                                cartItemId
                        ),
                        "Item removed successfully."
                )
        );

    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart() {

        String ownerId = cartOwnerProvider.getOwnerId();

        cartService.clearCart(ownerId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Cart cleared successfully."
                )
        );

    }



}