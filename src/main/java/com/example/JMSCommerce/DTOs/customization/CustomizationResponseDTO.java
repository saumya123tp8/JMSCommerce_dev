package com.example.JMSCommerce.DTOs.customization;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomizationResponseDTO {

    private Long productId;

    private List<CustomizationGroupResponseDTO> groups;
}
