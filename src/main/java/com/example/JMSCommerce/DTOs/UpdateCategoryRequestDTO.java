package com.example.JMSCommerce.DTOs;

import com.example.JMSCommerce.Utility.enums.CategoryStatus;
import lombok.Data;

@Data
public class UpdateCategoryRequestDTO extends CreateCategoryRequestDTO{
    private CategoryStatus status;
}
