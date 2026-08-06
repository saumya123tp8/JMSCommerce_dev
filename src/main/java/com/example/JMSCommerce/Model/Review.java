package com.example.JMSCommerce.Model;

import com.example.JMSCommerce.Utility.enums.ReviewStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(
        name = "review",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = "order_product_id"
                )
        }
)
public class Review extends BaseEntity {

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String reviewText;

    @Builder.Default
    @Column(nullable = false)
    private Boolean verifiedPurchase = true;

    @Builder.Default
    @Column(nullable = false)
    private Integer helpfulCount = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReviewStatus status = ReviewStatus.ACTIVE;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", nullable = false)
//    private Order order;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_product_id", nullable = false)
    private OrderProduct orderProduct;
    //without order a user can not review the product


}
