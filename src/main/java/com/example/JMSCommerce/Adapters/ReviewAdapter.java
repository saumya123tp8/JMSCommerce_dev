package com.example.JMSCommerce.Adapters;

import com.example.JMSCommerce.DTOs.review.CreateReviewRequestDTO;
import com.example.JMSCommerce.DTOs.review.ReviewResponseDTO;
import com.example.JMSCommerce.Model.OrderProduct;
import com.example.JMSCommerce.Model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewAdapter {

    public Review toEntity(
            CreateReviewRequestDTO request,
            OrderProduct orderProduct
    ) {

        return Review.builder()
                .rating(request.getRating())
                .title(request.getTitle())
                .reviewText(request.getReviewText())
                .verifiedPurchase(true)
                .orderProduct(orderProduct)
                .build();

    }

    public ReviewResponseDTO toResponse(
            Review review
    ) {

        return ReviewResponseDTO.builder()
                .id(review.getId())
                .rating(review.getRating())
                .title(review.getTitle())
                .reviewText(review.getReviewText())
                .verifiedPurchase(review.getVerifiedPurchase())
                .helpfulCount(review.getHelpfulCount())
                .createdAt(review.getCreatedAt())
                .reviewerName(
                        review.getOrderProduct()
                                .getOrder()
                                .getUser()
                                .getName()
                )
                .build();

    }

}