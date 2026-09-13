package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Model.Review;
import com.example.JMSCommerce.Repositories.ProductRepo;
import com.example.JMSCommerce.Repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductRatingSyncService {

    private final ReviewRepository reviewRepository;
    private final ProductRepo productRepository;


    public void syncRating(Product product, Review review, Integer prevRating) {

        int oldRatingCount = product.getRatingCount();
        double oldRating = product.getRating();

        int newRatingCount = oldRatingCount + 1;

        double newRating =
                ((oldRating * oldRatingCount) + review.getRating()-prevRating)
                        / newRatingCount;

        product.setReviewCount(
                product.getReviewCount() + 1
        );

        product.setRatingCount(newRatingCount);
        product.setRating(newRating);

        productRepository.save(product);
    }
    public void syncDelRating(Product product, Review review) {

        int oldRatingCount = product.getRatingCount();
        double oldRating = product.getRating();

        int newRatingCount = oldRatingCount - 1;

        double newRating =0.0;
        if(newRatingCount>0) {
            newRating =
                    ((oldRating * oldRatingCount) - review.getRating())
                            / newRatingCount;
        }
        product.setReviewCount(
                product.getReviewCount() - 1
        );

        product.setRatingCount(newRatingCount);
        product.setRating(newRating);

        productRepository.save(product);
    }
}
