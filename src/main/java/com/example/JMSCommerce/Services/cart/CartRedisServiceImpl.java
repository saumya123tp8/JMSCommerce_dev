package com.example.JMSCommerce.Services.cart;

import com.example.JMSCommerce.DTOs.cart.CartDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CartRedisServiceImpl
        implements CartRedisService {

    private static final String PREFIX = "cart:";

//    private static final Duration TTL =
//            Duration.ofDays(30);
      @Value("${spring.data.redis.cart.ttl-days}")
      private Long ttlDays;

    private final RedisTemplate<String, CartDTO> cartRedisTemplate;

    @Override
    public CartDTO getCart(
            String ownerId
    ) {

        CartDTO cart = cartRedisTemplate
                .opsForValue()
                .get(buildKey(ownerId));

        if (cart == null) {

            return CartDTO.builder()
                    .ownerId(ownerId)
                    .build();

        }

        return cart;

    }

    @Override
    public void saveCart(
            CartDTO cart
    ) {

        cartRedisTemplate
                .opsForValue()
                .set(
                        buildKey(cart.getOwnerId()),
                        cart,
                        Duration.ofDays(ttlDays)
                );

    }

    @Override
    public void deleteCart(
            String ownerId
    ) {

        cartRedisTemplate.delete(
                buildKey(ownerId)
        );

    }

    private String buildKey(
            String ownerId
    ) {
        return PREFIX + ownerId;
    }

}