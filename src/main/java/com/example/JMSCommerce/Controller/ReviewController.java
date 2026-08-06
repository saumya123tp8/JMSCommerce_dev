package com.example.JMSCommerce.Controller;

import com.example.JMSCommerce.DTOs.review.CreateReviewRequestDTO;
import com.example.JMSCommerce.DTOs.review.ReviewResponseDTO;
import com.example.JMSCommerce.DTOs.review.UpdateReviewRequestDTO;
import com.example.JMSCommerce.Services.ReviewService;
import com.example.JMSCommerce.Utility.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/orders/{orderId}/items/{orderProductId}/review")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> createReview(
            @PathVariable Long orderId,
            @PathVariable Long orderProductId,
            @Valid @RequestBody CreateReviewRequestDTO request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        reviewService.createReview(
                                orderId,
                                orderProductId,
                                request
                        ),
                        "Review submitted successfully."
                )
        );

    }

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<ApiResponse<List<ReviewResponseDTO>>> getReviews(
            @PathVariable Long productId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        reviewService.getReviewsByProduct(productId),
                        "Reviews fetched successfully."
                )
        );

    }

    @GetMapping("/products/{productId}/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> getReview(
            @PathVariable Long productId,
            @PathVariable Long reviewId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        reviewService.getReview(
                                productId,
                                reviewId
                        ),
                        "Review fetched successfully."
                )
        );

    }

    @PutMapping("/orders/{orderId}/items/{orderProductId}/review")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> updateReview(
            @PathVariable Long orderId,
            @PathVariable Long orderProductId,
            @Valid @RequestBody UpdateReviewRequestDTO request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        reviewService.updateReview(
                                orderId,
                                orderProductId,
                                request
                        ),
                        "Review updated successfully."
                )
        );

    }

    @DeleteMapping("/orders/{orderId}/items/{orderProductId}/review")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long orderId,
            @PathVariable Long orderProductId
    ) {

        reviewService.deleteReview(
                orderId,
                orderProductId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        null,
                        "Review deleted successfully."
                )
        );

    }

}