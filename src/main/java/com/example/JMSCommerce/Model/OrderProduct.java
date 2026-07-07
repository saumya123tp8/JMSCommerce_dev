package com.example.JMSCommerce.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="order_product")
public class OrderProduct extends BaseEntity{

    //if we check that many order_product entries are there for one order
    //order_product has fk of order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    //Similiarly
    //if we check that many order_product entries can be there for one product
    //order_product has fk of product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false,name = "current_price")
    private BigDecimal currentPrice;
    @Column(name = "current_discount")
    private BigDecimal currentDiscount = BigDecimal.ZERO;

}
