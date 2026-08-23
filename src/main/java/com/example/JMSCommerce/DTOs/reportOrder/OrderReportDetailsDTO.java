package com.example.JMSCommerce.DTOs.reportOrder;


import com.example.JMSCommerce.Utility.enums.OrderReportReason;
import com.example.JMSCommerce.Utility.enums.OrderReportStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderReportDetailsDTO {

    private Long id;

    private Long orderId;

    private String orderNumber;

    private OrderReportReason reason;

    private String description;

    private OrderReportStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<OrderReportMessageResponseDTO> messages;

    private OrderReportResolutionResponseDTO resolution;
}
