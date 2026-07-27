package com.example.JMSCommerce.DTOs.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CategorySummaryDTO {

    private Long id;

    private String name;

    private String description;

    private String slug;
}
