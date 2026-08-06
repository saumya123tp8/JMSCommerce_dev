package com.example.JMSCommerce.DTOs.customization;

import com.example.JMSCommerce.Utility.enums.AdjustmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomizationOptionRequestDTO {

    @NotBlank
    private String name;

    @Builder.Default
    private AdjustmentType adjustmentType = AdjustmentType.FIXED;

    @PositiveOrZero
    @Builder.Default
    private BigDecimal adjustmentValue = BigDecimal.ZERO;

    @Builder.Default
    private Integer displayOrder = 0;

}
