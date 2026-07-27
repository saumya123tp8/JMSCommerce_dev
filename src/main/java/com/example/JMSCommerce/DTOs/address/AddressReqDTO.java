package com.example.JMSCommerce.DTOs.address;

import com.example.JMSCommerce.Utility.enums.AddressType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressReqDTO {

    @NotBlank
    private String receiverName;

    @NotBlank
    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String receiverPhone;

    private String countryCode;

    @NotBlank
    private String houseNumber;

    private String apartment;

    @NotBlank
    private String street;

    private String landmark;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @Builder.Default
    private String country = "India";

    @NotBlank
    @Pattern(regexp = "^\\d{6}$")
    private String pincode;

    @Builder.Default
    private AddressType type = AddressType.HOME;

    @Builder.Default
    private boolean defaultAddress = false;

    private String deliveryInstructions;
}