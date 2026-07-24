package com.example.JMSCommerce.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.JMSCommerce.Adapters.OrderReviewAdapter;
import com.example.JMSCommerce.DTOs.GetOrderReviewResponseDTO;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Repositories.OrderReviewRepository;
import com.example.JMSCommerce.Model.OrderReview;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderReviewService {

    private final OrderReviewRepository orderReviewRepository;
    private final OrderReviewAdapter orderReviewAdapter;

    public List<GetOrderReviewResponseDTO> getAllReviews() {
        return orderReviewAdapter.mapToGetReviewResponseDtoList(orderReviewRepository.findAll());
    }

    public GetOrderReviewResponseDTO getReviewById(Long id) {
        return orderReviewRepository.findById(id)
                .map(orderReviewAdapter::mapToGetReviewResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Review with id " + id + " not found"));
    }

    public List<GetOrderReviewResponseDTO> getReviewsByProductId(Long productId) {
        return orderReviewAdapter.mapToGetReviewResponseDtoList(orderReviewRepository.findByProduct_Id(productId));
    }

    public List<GetOrderReviewResponseDTO> getReviewsByOrderId(Long orderId) {
        return orderReviewAdapter.mapToGetReviewResponseDtoList(orderReviewRepository.findByOrder_Id(orderId));
    }

    public OrderReview createReview(OrderReview review) {
        return orderReviewRepository.save(review);
    }

    public Void deleteReview(Long id) {
        OrderReview review = orderReviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review with id " + id + " not found"));
        orderReviewRepository.delete(review);
        return null;
    }
}