package com.example.JMSCommerce.DTOs.reportOrder;


import com.example.JMSCommerce.Utility.enums.OrderReportStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderReportStatusRequestDTO {

    @NotNull(message = "Report status is required.")
    private OrderReportStatus status;
}