package com.example.JMSCommerce.DTOs.reportOrder;

import com.example.JMSCommerce.Utility.enums.ResolutionType;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderReportResolutionResponseDTO {

    private ResolutionType type;

    private String message;

    private Instant resolvedAt;
}