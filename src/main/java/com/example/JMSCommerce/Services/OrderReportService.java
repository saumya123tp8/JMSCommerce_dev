package com.example.JMSCommerce.Services;


import com.example.JMSCommerce.DTOs.reportOrder.*;

import java.util.List;

public interface OrderReportService {

    OrderReportDetailsDTO createReport(
            Long orderId,
            CreateOrderReportRequestDTO request
    );

    List<OrderReportSummaryDTO> getMyReports();

    OrderReportDetailsDTO getMyReport(Long reportId);

    OrderReportMessageResponseDTO addUserMessage(
            Long reportId,
            CreateOrderReportMessageRequestDTO request
    );

    List<AdminOrderReportSummaryDTO> getAdminReports(
            String status
    );

    OrderReportDetailsDTO getAdminReport(Long reportId);

    OrderReportDetailsDTO updateStatus(
            Long reportId,
            UpdateOrderReportStatusRequestDTO request
    );

    OrderReportMessageResponseDTO addAdminMessage(
            Long reportId,
            CreateOrderReportMessageRequestDTO request
    );

    OrderReportDetailsDTO resolveReport(
            Long reportId,
            ResolveOrderReportRequestDTO request
    );
}