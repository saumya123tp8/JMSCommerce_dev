package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Model.Review;
import com.example.JMSCommerce.Repositories.ProductRepo;
import com.example.JMSCommerce.Repositories.ReviewRepository;
import com.example.JMSCommerce.Utility.enums.ReviewStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductRatingSyncService {

    private final ReviewRepository reviewRepository;
    private final ProductRepo productRepository;


    public void syncRating(Product product) {
        List<Review> reviews =
                reviewRepository.findByOrderProduct_Product_IdAndStatus(
                        product.getId(),
                        ReviewStatus.ACTIVE
                );

        product.setReviewCount(reviews.size());

        if (reviews.isEmpty()) {

            product.setRating(0.0);
            product.setRatingCount(0);

        } else {

            double average = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0);

            product.setRating(average);
            product.setRatingCount(reviews.size());

        }

        productRepository.save(product);
    }

}
