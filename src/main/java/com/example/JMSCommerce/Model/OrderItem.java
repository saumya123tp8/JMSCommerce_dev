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
@Table(name="order_item")
public class OrderItem extends BaseEntity{

//    //if we check that many order_product entries are there for one order
//    //order_product has fk of order
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", nullable = false)
//    private Order order;
//
//    //Similiarly
//    //if we check that many order_product entries can be there for one product
//    //order_product has fk of product
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;
//
//    @Column(nullable = false)
//    private Integer quantity;
//    @Column(nullable = false,name = "current_price")
//    private BigDecimal currentPrice;
//    @Column(name = "current_discount")
//    @Builder.Default
//    private BigDecimal currentDiscount = BigDecimal.ZERO;

    // now we do not have simple product , each product has variant and even the order has some customization for each product

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal mrp;

    @Column(nullable = false)
    private BigDecimal sellingPrice;

    @Column(nullable = false)
    private BigDecimal customizationPrice;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private String variantName;


    private String productName;

    private Boolean inventoryReserved;

    // also we save snapshot of customization in OrderItemCustomization
}
