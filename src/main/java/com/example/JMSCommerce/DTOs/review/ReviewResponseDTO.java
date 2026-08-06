package com.example.JMSCommerce.DTOs.review;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {

    private Long id;

    private Integer rating;

    private String title;

    private String reviewText;

    private Boolean verifiedPurchase;

    private Integer helpfulCount;

    private String reviewerName;

    private LocalDateTime createdAt;

}
