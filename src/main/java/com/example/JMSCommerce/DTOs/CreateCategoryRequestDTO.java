package com.example.JMSCommerce.DTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCategoryRequestDTO {
    private String name;
}
