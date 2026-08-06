package com.example.JMSCommerce.DTOs.product;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandSummaryDTO {
    private Long id;
    private String name;
    private Date stablishDate;
    private String description;
    private String logo;
}
