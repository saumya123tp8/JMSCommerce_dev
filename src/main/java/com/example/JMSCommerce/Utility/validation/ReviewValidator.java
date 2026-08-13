package com.example.JMSCommerce.Utility.validation;

import com.example.JMSCommerce.DTOs.review.CreateReviewRequestDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Model.OrderItem;
import com.example.JMSCommerce.Repositories.ReviewRepository;
import com.example.JMSCommerce.Utility.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewValidator {

    private final ReviewRepository reviewRepository;

    public void validateCreate(
            OrderItem orderItem,
            CreateReviewRequestDTO request
    ) {

        if (orderItem.getOrder().getStatus() != OrderStatus.DELIVERED) {
            throw new BadRequestException(
                    "Review can only be submitted after order delivery."
            );
        }

        if (reviewRepository.findByOrderItem_Id(
                orderItem.getId()
        ).isPresent()) {

            throw new BadRequestException(
                    "Review already exists for this purchased product."
            );
        }

    }

}