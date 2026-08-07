package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.Adapters.OrderReviewAdapter;
import com.example.JMSCommerce.Adapters.ReviewAdapter;
import com.example.JMSCommerce.DTOs.review.CreateReviewRequestDTO;
import com.example.JMSCommerce.DTOs.review.ReviewResponseDTO;
import com.example.JMSCommerce.DTOs.review.UpdateReviewRequestDTO;
import com.example.JMSCommerce.Exception.AccessDeniedException;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.OrderProduct;
import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Model.Review;
import com.example.JMSCommerce.Model.User;
import com.example.JMSCommerce.Repositories.OrderProductRepo;
import com.example.JMSCommerce.Repositories.ProductRepo;
import com.example.JMSCommerce.Repositories.ReviewRepository;
import com.example.JMSCommerce.Repositories.UserRepo;
import com.example.JMSCommerce.Utility.ProductHelper;
import com.example.JMSCommerce.Utility.SecurityUtils;
import com.example.JMSCommerce.Utility.enums.ReviewStatus;
import com.example.JMSCommerce.Utility.validation.ReviewValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository orderReviewRepository;
    private final OrderReviewAdapter orderReviewAdapter;
    private final ReviewRepository reviewRepository;
    private final OrderProductRepo orderProductRepo;
    private final ProductRepo productRepository;

    private final ReviewAdapter reviewAdapter;
    private final ReviewValidator reviewValidator;
    private final ProductRatingSyncService ratingSyncService;

    private final ProductHelper productHelper;

    private final UserRepo userRepo;

//    public List<GetOrderReviewResponseDTO> getAllReviews() {
//        return orderReviewAdapter.mapToGetReviewResponseDtoList(orderReviewRepository.findAll());
//    }

    @Transactional(readOnly = true)
    public ReviewResponseDTO getReview(
            Long productId,
            Long reviewId
    ) {

        Review review = getReviewOrThrow(
                productId,
                reviewId
        );

        return reviewAdapter.toResponse(
                review
        );
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getReviewsByProduct(
            Long productId
    ) {

        Product product =
                productHelper.getActiveProductOrThrow(
                        productRepository,
                        productId
                );

        return reviewRepository
                .findByOrderProduct_Product_IdAndStatus(
                        product.getId(),
                        ReviewStatus.ACTIVE
                )
                .stream()
                .map(reviewAdapter::toResponse)
                .toList();
    }

//    public List<GetOrderReviewResponseDTO> getReviewsByOrderId(Long orderId) {
//        return orderReviewAdapter.mapToGetReviewResponseDtoList(orderReviewRepository.findByOrder_Id(orderId));
//    }

    public ReviewResponseDTO createReview(
            Long orderId,
            Long orderProductId,
            CreateReviewRequestDTO request
    ) {

        OrderProduct orderProduct = getOrderProduct(
                orderId,
                orderProductId
        );
        User currentUser = getCurrentUserId();
        if (!orderProduct.getOrder().getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Access Denied");
        }

        reviewValidator.validateCreate(
                orderProduct,
                request
        );

        Review review = reviewAdapter.toEntity(
                request,
                orderProduct
        );

        Review savedReview =
                reviewRepository.save(review);

        ratingSyncService.syncRating(
                orderProduct.getProduct()
        );

        return reviewAdapter.toResponse(
                savedReview
        );
    }

    public ReviewResponseDTO updateReview(
            Long orderId,
            Long orderProductId,
            UpdateReviewRequestDTO request
    ) {

        OrderProduct orderProduct = getOrderProduct(
                orderId,
                orderProductId
        );

        Review review =
                reviewRepository.findByOrderProduct_Id(
                                orderProduct.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Review not found."
                                ));

        review.setRating(request.getRating());
        review.setTitle(request.getTitle());
        review.setReviewText(
                request.getReviewText()
        );

        Review saved =
                reviewRepository.save(review);

        ratingSyncService.syncRating(
                orderProduct.getProduct()
        );

        return reviewAdapter.toResponse(saved);
    }


    public void deleteReview(
            Long orderId,
            Long orderProductId
    ) {

        OrderProduct orderProduct = getOrderProduct(
                orderId,
                orderProductId
        );

        Review review =
                reviewRepository.findByOrderProduct_Id(
                                orderProduct.getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Review not found."
                                ));

        reviewRepository.delete(review);

        ratingSyncService.syncRating(
                orderProduct.getProduct()
        );
    }

    /*------------------------------------------------*/

    private OrderProduct getOrderProduct(
            Long orderId,
            Long orderProductId
    ) {

        return orderProductRepo
                .findByIdAndOrder_Id(
                        orderProductId,
                        orderId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Purchased product not found."
                        ));

    }

    private Review getReviewOrThrow(
            Long productId,
            Long reviewId
    ) {

        return reviewRepository
                .findById(reviewId)
                .filter(review ->
                        review.getOrderProduct()
                                .getProduct()
                                .getId()
                                .equals(productId)
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Review not found."
                        ));

    }

    private User getCurrentUserId() {
        String currentUserMail = SecurityUtils.getCurrentUserMail();
        User user = userRepo.findByEmail(currentUserMail).orElseThrow(
                ()->new ResourceNotFoundException("some thing wrong  with current logged in user")
        );
        return user;
    }
}