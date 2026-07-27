package com.example.JMSCommerce.Controller;


import com.example.JMSCommerce.DTOs.address.AddressReqDTO;
import com.example.JMSCommerce.DTOs.address.AddressResponseDTO;
import com.example.JMSCommerce.Services.AddressService;
import com.example.JMSCommerce.Utility.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponseDTO>> create(
            @Valid @RequestBody AddressReqDTO request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        addressService.createAddress(request),
                        "Address Created successfully"
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponseDTO>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        addressService.getMyAddresses(),
                        "Fetched All Address successfully"
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponseDTO>> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        addressService.getAddressByIdResponseDTO(id),
                        "address fetched successfully"
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody AddressReqDTO request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        addressService.updateAddress(id, request),
                        "address updated successfully"
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id
    ) {

        addressService.deleteAddress(id);

        return ResponseEntity.ok(
                ApiResponse.success(null, "Address Deleted successfully")
        );
    }

    @PatchMapping("/{id}/default")
    public ResponseEntity<ApiResponse<AddressResponseDTO>> setDefault(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        addressService.setDefaultAddress(id),
                        "Address successfully set to default"
                )
        );
    }
}