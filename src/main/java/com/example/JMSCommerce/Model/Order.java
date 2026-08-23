package com.example.JMSCommerce.Model;

import com.example.JMSCommerce.Utility.enums.CurrencyType;
import com.example.JMSCommerce.Utility.enums.OrderStatus;
import com.example.JMSCommerce.Utility.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "orders")
public class Order extends BaseEntity{

//    private OrderStatus status;
//    @Column(name = "current_subtotal")
//    private BigDecimal currentSubtotal;
//    @Column(name = "delivered_at")
//    private String deliveredAt;
//    @ManyToOne
//    private User user;
//    //    @ManyToMany
////    @JoinTable(
////            name = "order_products",
////            joinColumns = @JoinColumn(name = "order_id"),
////            inverseJoinColumns = @JoinColumn(name = "product_id")
////    )
////    private List<Product> produts;
///
///
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @OneToOne(
            mappedBy = "order",
            fetch = FetchType.LAZY
    )
    private Payment payment;



    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false)
    private BigDecimal discount;

    @Column(nullable = false)
    private BigDecimal tax;

    @Column(nullable = false)
    private BigDecimal deliveryCharge;

    @Column(nullable = false)
    private BigDecimal grandTotal;

    @Column(nullable = false)
    @Builder.Default
    private CurrencyType currency = CurrencyType.INR;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "delivery_address_id",
            nullable = false
    )
    private OrderDeliveryAddress deliveryAddress;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @OneToMany(
            mappedBy = "order",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<OrderReport> reports = new ArrayList<>();

}
