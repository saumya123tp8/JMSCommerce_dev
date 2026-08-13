package com.example.JMSCommerce.Model;

import com.example.JMSCommerce.Utility.enums.OrderStatus;
import com.example.JMSCommerce.Utility.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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

    private String deliveredAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
