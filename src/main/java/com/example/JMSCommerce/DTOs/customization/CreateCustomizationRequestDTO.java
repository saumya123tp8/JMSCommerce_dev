package com.example.JMSCommerce.DTOs.customization;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomizationRequestDTO {
    @NotEmpty
    private List<CreateCustomizationGroupRequestDTO> groups;
}
