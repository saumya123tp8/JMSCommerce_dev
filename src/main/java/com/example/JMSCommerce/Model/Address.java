package com.example.JMSCommerce.Model;
import com.example.JMSCommerce.Utility.enums.AddressType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "addresses",
        indexes = {
                @Index(name = "idx_address_user", columnList = "user_id"),
                @Index(name = "idx_address_pincode", columnList = "pincode")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String receiverName;

    @Column(nullable = false, length = 15)
    private String receiverPhone;

    @Column(length = 10)
    private String countryCode;

    @Column(nullable = false, length = 100)
    private String houseNumber;

    @Column(length = 100)
    private String apartment;

    @Column(nullable = false, length = 255)
    private String street;

    @Column(length = 255)
    private String landmark;

    @Column(nullable = false, length = 80)
    private String city;

    @Column(nullable = false, length = 80)
    private String state;

    @Column(nullable = false, length = 80)
    @Builder.Default
    private String country = "India";

    @Column(nullable = false, length = 10)
    private String pincode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AddressType type = AddressType.HOME;

    @Builder.Default
    private boolean defaultAddress = false;

    @Column(length = 255)
    private String deliveryInstructions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}