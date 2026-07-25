package com.example.JMSCommerce.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCategoryRequestDTO {
    @NotBlank(message = "Category name is required.")
    @Size(max = 30, message = "Category name cannot exceed 30 characters.")
    @Pattern(
            regexp = "^[A-Za-z0-9&()\\-,' ]+$",
            message = "Category name contains invalid characters."
    )
    private String name;

    @Size(max = 300, message = "Category name cannot exceed 300 characters.")
    @Pattern(
            regexp = "^[A-Za-z0-9&()\\-,' ]+$",
            message = "Category name contains invalid characters."
    )
    private String description;

    private Long parentId;
}
