package com.example.JMSCommerce.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "order_item_customization")
public class OrderItemCustomization extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @Column(nullable = false)
    private Long customizationOptionId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal priceAdjustment;
}
