package com.example.JMSCommerce.DTOs;

import com.example.JMSCommerce.Utility.enums.CategoryStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponseDTO {

    private Long id;

    private String name;

    private String slug;

    private String description;

    private CategoryStatus status;

    private Long parentId;

}
