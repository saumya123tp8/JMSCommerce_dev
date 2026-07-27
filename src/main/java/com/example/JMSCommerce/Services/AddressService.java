package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.Adapters.AddressAdapter;
import com.example.JMSCommerce.DTOs.address.AddressReqDTO;
import com.example.JMSCommerce.DTOs.address.AddressResponseDTO;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.Address;
import com.example.JMSCommerce.Model.User;
import com.example.JMSCommerce.Repositories.AddressRepository;
import com.example.JMSCommerce.Repositories.UserRepo;
import com.example.JMSCommerce.Utility.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
//at a time only one address can be added after successful operation user can add new one later
//and while fetch we gave only default address first and then afte click on change gith other and one button to add new

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressAdapter addressAdapter;
    private final UserRepo userRepo;

    @Transactional
    public AddressResponseDTO createAddress(
            AddressReqDTO request
    ) {

        String currentUserMail = SecurityUtils.getCurrentUserMail();
        User user = userRepo.findByEmail(currentUserMail).orElseThrow(
                ()->new ResourceNotFoundException("some thing wrong  with current logged in user")
        );
        Address address =
                addressAdapter.mapRequestToAddress(request);

        address.setUser(user);

        List<Address> existingAddresses =
                addressRepository.findByUser_Id(user.getId());

        // First address should always become default
        if (existingAddresses.isEmpty()) {
            address.setDefaultAddress(true);
        }

        // User selected this as default
        else if (address.isDefaultAddress()) {

            existingAddresses.forEach(
                    a -> a.setDefaultAddress(false)
            );
        }

        address = addressRepository.save(address);

        return addressAdapter.mapAddressToResponse(address);
    }

    @Transactional
    public AddressResponseDTO updateAddress(
            Long id,
            AddressReqDTO request
    ) {

        Address address = getAddressById(id);

        User user = getCurrentUserId();
        if (request.isDefaultAddress()) {

            addressRepository
                    .findByUser_Id(user.getId())
                    .forEach(a -> a.setDefaultAddress(false));
        }

        address.setReceiverName(request.getReceiverName());
        address.setReceiverPhone(request.getReceiverPhone());
        address.setCountryCode(request.getCountryCode());
        address.setHouseNumber(request.getHouseNumber());
        address.setApartment(request.getApartment());
        address.setStreet(request.getStreet());
        address.setLandmark(request.getLandmark());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPincode(request.getPincode());
        address.setType(request.getType());
        address.setDefaultAddress(request.isDefaultAddress());
        address.setDeliveryInstructions(
                request.getDeliveryInstructions()
        );

        return addressAdapter.mapAddressToResponse(address);
    }

    private Address getAddressById(Long id) {
        User user = getCurrentUserId();
        return addressRepository
                .findByIdAndUser_Id(
                        id,
                        user.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found."
                        ));

    }

    private User getCurrentUserId() {
        String currentUserMail = SecurityUtils.getCurrentUserMail();
        User user = userRepo.findByEmail(currentUserMail).orElseThrow(
                ()->new ResourceNotFoundException("some thing wrong  with current logged in user")
        );
        return user;
    }

    public AddressResponseDTO getAddressByIdResponseDTO(Long id) {

        User user = getCurrentUserId();
        Address address = addressRepository
                .findByIdAndUser_Id(
                        id,
//                        SecurityUtils.getCurrentUserId()
                        user.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found."
                        ));

        return addressAdapter.mapAddressToResponse(address);

    }

    public List<AddressResponseDTO> getMyAddresses() {
        User user = getCurrentUserId();
        return addressRepository
                .findByUser_IdOrderByDefaultAddressDescCreatedAtDesc(
//                        SecurityUtils.getCurrentUserId()
                        user.getId()
                )
                .stream()
                .map(addressAdapter::mapAddressToResponse)
                .toList();
    }

    @Transactional
    public void deleteAddress(Long id) {

        Address address = getAddressById(id);
        User user = getCurrentUserId();
        boolean wasDefault = address.isDefaultAddress();

        addressRepository.delete(address);

        if (wasDefault) {

            addressRepository
                    .findFirstByUser_IdOrderByCreatedAtAsc(
//                            SecurityUtils.getCurrentUserId()
                            user.getId()
                    )

                    .ifPresent(a -> a.setDefaultAddress(true));
        }
    }

    @Transactional
    public AddressResponseDTO setDefaultAddress(Long id) {
        User user = getCurrentUserId();
        Long userId = user.getId();

        Address address = getAddressById(id);

        addressRepository
                .findByUser_Id(userId)
                .forEach(a -> a.setDefaultAddress(false));

        address.setDefaultAddress(true);

        return addressAdapter.mapAddressToResponse(address);
    }

}
