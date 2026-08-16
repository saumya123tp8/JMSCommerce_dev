package com.example.JMSCommerce.Model;

import com.example.JMSCommerce.Utility.enums.AddressType;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_delivery_address")
public class OrderDeliveryAddress extends BaseEntity {

    @Column(nullable = false)
    private String receiverName;

    @Column(nullable = false)
    private String receiverPhone;

    private String countryCode;

    @Column(nullable = false)
    private String houseNumber;

    private String apartment;

    @Column(nullable = false)
    private String street;

    private String landmark;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String pincode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddressType type;

    @Column(length = 500)
    private String deliveryInstructions;
}
