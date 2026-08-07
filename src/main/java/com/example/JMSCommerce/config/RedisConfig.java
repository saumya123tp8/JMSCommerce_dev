package com.example.JMSCommerce.config;

import com.example.JMSCommerce.DTOs.cart.CartDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, CartDTO> cartRedisTemplate(
            RedisConnectionFactory connectionFactory,
            ObjectMapper objectMapper
    ) {

        RedisTemplate<String, CartDTO> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());

        template.setValueSerializer(
                new GenericJacksonJsonRedisSerializer(objectMapper)
        );

        template.setHashKeySerializer(new StringRedisSerializer());

        template.setHashValueSerializer(
                new GenericJacksonJsonRedisSerializer(objectMapper)
        );

        template.afterPropertiesSet();

        return template;
    }

}