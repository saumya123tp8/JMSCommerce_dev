package com.example.JMSCommerce.DTOs;

import com.example.JMSCommerce.Utility.enums.CategoryStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false) // Data will override getter setter RequiredArgsConstroto and equals and hashcode
//so equas ad hashcode asking that do I need to condider parent attr. to compare obj. in equals
public class UpdateCategoryRequestDTO extends CreateCategoryRequestDTO{
    private CategoryStatus status;
}
