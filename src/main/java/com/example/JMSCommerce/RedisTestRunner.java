package com.example.JMSCommerce;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisTestRunner
        implements CommandLineRunner {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void run(String... args) {

        redisTemplate.opsForValue()
                .set("hello", "redis");

        System.out.println(
                redisTemplate.opsForValue().get("hello")
        );

    }
}