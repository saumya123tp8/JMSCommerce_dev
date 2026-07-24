package com.example.JMSCommerce.Model;

import com.example.JMSCommerce.Utility.enums.OrderStatus;
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

    private OrderStatus status;
    @Column(name = "current_subtotal")
    private BigDecimal currentSubtotal;
    @Column(name = "delivered_at")
    private String deliveredAt;
//    @ManyToMany
//    @JoinTable(
//            name = "order_products",
//            joinColumns = @JoinColumn(name = "order_id"),
//            inverseJoinColumns = @JoinColumn(name = "product_id")
//    )
//    private List<Product> produts;

}
