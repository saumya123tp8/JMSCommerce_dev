package com.example.JMSCommerce.Adapters;


import com.example.JMSCommerce.DTOs.GetOrderReviewResponseDTO;
import com.example.JMSCommerce.Model.OrderReview;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderReviewAdapter {

    public List<GetOrderReviewResponseDTO> mapToGetReviewResponseDtoList(List<OrderReview> reviews) {
        return reviews.stream()
                .map(this::mapToGetReviewResponseDto)
                .collect(Collectors.toList());
    }

    public GetOrderReviewResponseDTO mapToGetReviewResponseDto(OrderReview review) {
        return GetOrderReviewResponseDTO.builder()
                .id(review.getId())
                .productId(review.getProduct().getId())
                .orderId(review.getOrder().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
