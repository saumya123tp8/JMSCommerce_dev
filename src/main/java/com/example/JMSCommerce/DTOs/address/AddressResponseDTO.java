package com.example.JMSCommerce.DTOs.address;

import com.example.JMSCommerce.Utility.enums.AddressType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponseDTO {

    private Long id;

    private String receiverName;

    private String receiverPhone;

    private String countryCode;

    private String houseNumber;

    private String apartment;

    private String street;

    private String landmark;

    private String city;

    private String state;

    private String country;

    private String pincode;

    private AddressType type;

    private boolean defaultAddress;

    private String deliveryInstructions;
}