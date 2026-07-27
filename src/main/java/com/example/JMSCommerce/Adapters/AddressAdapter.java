package com.example.JMSCommerce.Adapters;

import com.example.JMSCommerce.DTOs.address.AddressReqDTO;
import com.example.JMSCommerce.DTOs.address.AddressResponseDTO;
import com.example.JMSCommerce.Model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressAdapter {

    public Address mapRequestToAddress(AddressReqDTO dto) {

        return Address.builder()
                .receiverName(dto.getReceiverName())
                .receiverPhone(dto.getReceiverPhone())
                .countryCode(dto.getCountryCode())
                .houseNumber(dto.getHouseNumber())
                .apartment(dto.getApartment())
                .street(dto.getStreet())
                .landmark(dto.getLandmark())
                .city(dto.getCity())
                .state(dto.getState())
                .country(dto.getCountry())
                .pincode(dto.getPincode())
                .type(dto.getType())
                .defaultAddress(dto.isDefaultAddress())
                .deliveryInstructions(dto.getDeliveryInstructions())
                .build();
    }

    public AddressResponseDTO mapAddressToResponse(Address address) {

        return AddressResponseDTO.builder()
                .id(address.getId())
                .receiverName(address.getReceiverName())
                .receiverPhone(address.getReceiverPhone())
                .countryCode(address.getCountryCode())
                .houseNumber(address.getHouseNumber())
                .apartment(address.getApartment())
                .street(address.getStreet())
                .landmark(address.getLandmark())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .pincode(address.getPincode())
                .type(address.getType())
                .defaultAddress(address.isDefaultAddress())
                .deliveryInstructions(address.getDeliveryInstructions())
                .build();
    }
}