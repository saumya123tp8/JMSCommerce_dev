package com.example.JMSCommerce.DTOs.product;


import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandReqDTO {
    private String name;
    private Date stablishDate;
    private String description;
    private String logo;
}