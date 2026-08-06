package com.example.JMSCommerce.Utility.validation;

import com.example.JMSCommerce.DTOs.review.CreateReviewRequestDTO;
import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Model.OrderProduct;
import com.example.JMSCommerce.Repositories.ReviewRepository;
import com.example.JMSCommerce.Utility.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewValidator {

    private final ReviewRepository reviewRepository;

    public void validateCreate(
            OrderProduct orderProduct,
            CreateReviewRequestDTO request
    ) {

        if (orderProduct.getOrder().getStatus() != OrderStatus.Delivered) {
            throw new BadRequestException(
                    "Review can only be submitted after order delivery."
            );
        }

        if (reviewRepository.findByOrderProductId(
                orderProduct.getId()
        ).isPresent()) {

            throw new BadRequestException(
                    "Review already exists for this purchased product."
            );
        }

    }

}