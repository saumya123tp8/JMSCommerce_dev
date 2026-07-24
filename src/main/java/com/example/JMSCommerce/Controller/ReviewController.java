package com.example.JMSCommerce.Controller;

import com.example.JMSCommerce.DTOs.GetOrderReviewResponseDTO;
import com.example.JMSCommerce.Model.OrderReview;
import com.example.JMSCommerce.Services.OrderReviewService;
import com.example.JMSCommerce.Utility.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final OrderReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GetOrderReviewResponseDTO>>> getAllReviews() {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getAllReviews(),"Fetched all review successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderReview>> createReview(@RequestBody OrderReview review) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(reviewService.createReview(review),"review added successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.deleteReview(id),"review deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GetOrderReviewResponseDTO>> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewById(id),"Review fetched successfully"));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<GetOrderReviewResponseDTO>>> getReviewsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewsByProductId(productId),"Review fetched for a product"));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<GetOrderReviewResponseDTO>>> getReviewsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getReviewsByOrderId(orderId),"Review fetched for order"));
    }
}
