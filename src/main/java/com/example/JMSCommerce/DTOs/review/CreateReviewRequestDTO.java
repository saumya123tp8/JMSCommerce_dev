package com.example.JMSCommerce.DTOs.review;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequestDTO {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    @Size(max = 3000)
    private String reviewText;

}
