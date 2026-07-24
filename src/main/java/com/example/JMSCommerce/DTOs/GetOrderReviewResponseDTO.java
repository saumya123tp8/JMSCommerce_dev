package com.example.JMSCommerce.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class GetOrderReviewResponseDTO {

        private Long id;

        private Long productId;

        private Long orderId;

        private BigDecimal rating;

        private String comment;

        private LocalDateTime createdAt;
    }
