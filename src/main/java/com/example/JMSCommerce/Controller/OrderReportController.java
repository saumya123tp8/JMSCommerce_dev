package com.example.JMSCommerce.Controller;


import com.example.JMSCommerce.DTOs.reportOrder.*;
import com.example.JMSCommerce.Services.OrderReportService;
import com.example.JMSCommerce.Utility.ApiResponse;
import com.example.JMSCommerce.Utility.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OrderReportController {

    private final OrderReportService orderReportService;


    // =========================================================
    // USER APIs
    // =========================================================

    @PostMapping("/orders/{orderId}/reports")
    
    public ResponseEntity<ApiResponse<OrderReportDetailsDTO>> createReport(
            @PathVariable Long orderId,
            @Valid @RequestBody CreateOrderReportRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                orderReportService.createReport(
                                        orderId,
                                        request
                                ),
                                "Order report submitted successfully"
                        )
                );
    }


    @GetMapping("/users/me/order-reports")
    
    public ResponseEntity<ApiResponse<List<OrderReportSummaryDTO>>>
    getMyReports() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        orderReportService.getMyReports(),
                        "Order reports fetched successfully"
                )
        );
    }


    @GetMapping("/order-reports/{reportId}")
    
    public ResponseEntity<ApiResponse<OrderReportDetailsDTO>>
    getMyReport(
            @PathVariable Long reportId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        orderReportService.getMyReport(reportId),
                        "Order report fetched successfully"
                )
        );
    }


    @PostMapping("/order-reports/{reportId}/messages")
    
    public ResponseEntity<ApiResponse<OrderReportMessageResponseDTO>>
    addUserMessage(
            @PathVariable Long reportId,
            @Valid @RequestBody CreateOrderReportMessageRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                orderReportService.addUserMessage(
                                        reportId,
                                        request
                                ),
                                "Message added successfully"
                        )
                );
    }


    // =========================================================
    // ADMIN APIs
    // =========================================================

    @GetMapping("/admin/order-reports")
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<List<AdminOrderReportSummaryDTO>>>
    getAdminReports(
            @RequestParam(required = false) String status
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        orderReportService.getAdminReports(status),
                        "Order reports fetched successfully"
                )
        );
    }


    @GetMapping("/admin/order-reports/{reportId}")
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<OrderReportDetailsDTO>>
    getAdminReport(
            @PathVariable Long reportId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        orderReportService.getAdminReport(reportId),
                        "Order report fetched successfully"
                )
        );
    }


    @PatchMapping("/admin/order-reports/{reportId}/status")
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<OrderReportDetailsDTO>>
    updateStatus(
            @PathVariable Long reportId,
            @Valid @RequestBody UpdateOrderReportStatusRequestDTO request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        orderReportService.updateStatus(
                                reportId,
                                request
                        ),
                        "Report status updated successfully"
                )
        );
    }


    @PostMapping("/admin/order-reports/{reportId}/messages")
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<OrderReportMessageResponseDTO>>
    addAdminMessage(
            @PathVariable Long reportId,
            @Valid @RequestBody CreateOrderReportMessageRequestDTO request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                orderReportService.addAdminMessage(
                                        reportId,
                                        request
                                ),
                                "Response added successfully"
                        )
                );
    }


    @PostMapping("/admin/order-reports/{reportId}/resolve")
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<OrderReportDetailsDTO>>
    resolveReport(
            @PathVariable Long reportId,
            @Valid @RequestBody ResolveOrderReportRequestDTO request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        orderReportService.resolveReport(
                                reportId,
                                request
                        ),
                        "Report resolved successfully"
                )
        );
    }
}