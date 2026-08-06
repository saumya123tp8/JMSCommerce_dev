package com.example.JMSCommerce.DTOs.customization;

import com.example.JMSCommerce.Utility.enums.AdjustmentType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomizationOptionResponseDTO {
    private Long id;

    private String name;

    private AdjustmentType adjustmentType;

    private BigDecimal adjustmentValue;

    private Integer displayOrder;

    private Boolean active;
}
