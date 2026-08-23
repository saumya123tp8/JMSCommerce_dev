package com.example.JMSCommerce.DTOs.reportOrder;

import com.example.JMSCommerce.Utility.enums.ReportSenderType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderReportMessageResponseDTO {

    private Long id;

    private ReportSenderType senderType;

    private String message;

    private LocalDateTime createdAt;
}