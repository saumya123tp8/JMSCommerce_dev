package com.example.JMSCommerce.DTOs.reportOrder;

import com.example.JMSCommerce.Utility.enums.OrderReportReason;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderReportRequestDTO {

    @NotNull(message = "Report reason is required.")
    private OrderReportReason reason;

    @Size(
            max = 2000,
            message = "Description cannot exceed 2000 characters."
    )
    private String description;
}
