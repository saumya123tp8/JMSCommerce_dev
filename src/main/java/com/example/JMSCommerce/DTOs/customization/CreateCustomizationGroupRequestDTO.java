package com.example.JMSCommerce.DTOs.customization;

import com.example.JMSCommerce.Utility.enums.SelectionType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomizationGroupRequestDTO {

    @NotBlank
    private String name;

    @NotNull
    private SelectionType selectionType;

    @Builder.Default
    private Boolean required = false;

    @PositiveOrZero
    private Integer minSelection;

    @Positive
    private Integer maxSelection;

    @Builder.Default
    private Integer displayOrder = 0;

    @NotEmpty
    private List<CreateCustomizationOptionRequestDTO> options;

}
