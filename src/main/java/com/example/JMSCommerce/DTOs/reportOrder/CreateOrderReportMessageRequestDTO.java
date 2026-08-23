package com.example.JMSCommerce.DTOs.reportOrder;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderReportMessageRequestDTO {

    @NotBlank(message = "Message is required.")
    @Size(
            max = 3000,
            message = "Message cannot exceed 3000 characters."
    )
    private String message;
}