package com.example.JMSCommerce.DTOs.reportOrder;

import com.example.JMSCommerce.Utility.enums.OrderReportReason;
import com.example.JMSCommerce.Utility.enums.OrderReportStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderReportSummaryDTO {

    private Long id;

    private Long orderId;

    private String orderNumber;

    private OrderReportReason reason;

    private OrderReportStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}