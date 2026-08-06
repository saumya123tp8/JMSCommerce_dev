package com.example.JMSCommerce.DTOs.customization;

import com.example.JMSCommerce.Utility.enums.SelectionType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomizationGroupResponseDTO {

    private Long id;

    private String name;

    private SelectionType selectionType;

    private Boolean required;

    private Integer minSelection;

    private Integer maxSelection;

    private Integer displayOrder;

    private Boolean active;

    private List<CustomizationOptionResponseDTO> options;

}
