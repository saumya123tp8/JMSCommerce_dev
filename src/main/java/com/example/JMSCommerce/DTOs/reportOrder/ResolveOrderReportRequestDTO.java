package com.example.JMSCommerce.DTOs.reportOrder;

import com.example.JMSCommerce.Utility.enums.ResolutionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResolveOrderReportRequestDTO {

    @NotNull(message = "Resolution type is required.")
    private ResolutionType type;

    @NotBlank(message = "Resolution message is required.")
    @Size(
            max = 3000,
            message = "Resolution message cannot exceed 3000 characters."
    )
    private String message;
}
