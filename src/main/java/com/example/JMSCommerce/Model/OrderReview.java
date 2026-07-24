package com.example.JMSCommerce.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(
        name = "order_review",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"order_id", "product_id"}
                )
        }
)
public class OrderReview extends BaseEntity{

    private String comment;
    private BigDecimal rating;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    //without order a user can not review the product

}
