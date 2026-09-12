
package com.example.JMSCommerce.DTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDTO {

    @Size(min = 2, max = 60, message = "Name must be between 2 and 60 characters")
    private String name;

    @Size(max = 500, message = "Image URL/path cannot exceed 500 characters")
    private String image;

    @Pattern(
            regexp = "^[0-9+()\\-\\s]{7,20}$",
            message = "Invalid phone number"
    )
    private String phone;

    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 90, message = "Age cannot be greater than 90")
    private Integer age;

}

