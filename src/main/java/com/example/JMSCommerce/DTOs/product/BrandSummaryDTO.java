package com.example.JMSCommerce.DTOs.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class BrandSummaryDTO {
    private Long id;
    private String name;
    private Date stablishDate;
}
