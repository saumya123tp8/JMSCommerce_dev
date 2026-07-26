package com.example.JMSCommerce.Controller;

import com.example.JMSCommerce.DTOs.specificationDefinition.CreateSpecificationDefinitionRequestDTO;
import com.example.JMSCommerce.DTOs.specificationDefinition.SpecificationDefinitionResponseDTO;
import com.example.JMSCommerce.DTOs.specificationDefinition.UpdateSpecificationDefinitionRequestDTO;
import com.example.JMSCommerce.Services.SpecificationDefinitionService;
import com.example.JMSCommerce.Utility.ApiResponse;
import com.example.JMSCommerce.Utility.AppConstants;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/specifications")
public class SpecificationDefinitionController {

    private final SpecificationDefinitionService specificationDefinitionService;

    @PostMapping
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<SpecificationDefinitionResponseDTO>> createSpecification(
            @Valid @RequestBody CreateSpecificationDefinitionRequestDTO request
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(specificationDefinitionService.createSpecification(request),"specification created for this category"));
    }

    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<ApiResponse<SpecificationDefinitionResponseDTO>> getSpecificationById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok().body(ApiResponse.success(specificationDefinitionService.getSpecificationById(id),"get specification for this category"));
    }

    @GetMapping
    @PermitAll
    public ResponseEntity<ApiResponse<List<SpecificationDefinitionResponseDTO>>>
    getAllSpecifications(){
        return ResponseEntity.ok().body(ApiResponse.success(specificationDefinitionService.getAllSpecifications(),"get all specification for this category"));

    }

    @PutMapping("/{id}")
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<SpecificationDefinitionResponseDTO>>
    updateSpecification(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSpecificationDefinitionRequestDTO request
    ){
        return ResponseEntity.ok().body(ApiResponse.success(specificationDefinitionService.updateSpecification(id,request),"specification updated for this category"));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<Void>>
    deleteSpecification(
            @PathVariable Long id
    ){
        specificationDefinitionService.getSpecificationById(id);
        return ResponseEntity.ok().body(ApiResponse.success(null,"specification deleted for this category"));
    }

}
