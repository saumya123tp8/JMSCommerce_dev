package com.example.JMSCommerce.Model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "orders")
public class Orders extends BaseEntity{

    private OrderStatus status;

//    @ManyToMany
//    @JoinTable(
//            name = "order_products",
//            joinColumns = @JoinColumn(name = "order_id"),
//            inverseJoinColumns = @JoinColumn(name = "product_id")
//    )
//    private List<Product> produts;

}
