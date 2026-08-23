package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.DTOs.reportOrder.*;
import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.*;
import com.example.JMSCommerce.Repositories.*;
import com.example.JMSCommerce.Utility.enums.OrderReportStatus;
import com.example.JMSCommerce.Utility.enums.ReportSenderType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderReportServiceImpl implements OrderReportService {

    private final OrderReportRepo orderReportRepo;
    private final OrderReportMessageRepo messageRepo;
    private final OrderReportResolutionRepo resolutionRepo;
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;


    // =========================================================
    // USER
    // =========================================================

    @Override
    public OrderReportDetailsDTO createReport(
            Long orderId,
            CreateOrderReportRequestDTO request
    ) {

        User user = getAuthenticatedUser();

        Order order = orderRepo
                .findByIdAndUser_Id(orderId, user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found."
                        )
                );

        List<OrderReportStatus> activeStatuses = List.of(
                OrderReportStatus.OPEN,
                OrderReportStatus.IN_REVIEW
        );

        if (orderReportRepo.existsByOrder_IdAndStatusIn(
                orderId,
                activeStatuses
        )) {
            throw new BadRequestException(
                    "An active report already exists for this order."
            );
        }

        OrderReport report = OrderReport.builder()
                .order(order)
                .reason(request.getReason())
                .description(request.getDescription())
                .status(OrderReportStatus.OPEN)
                .build();

        OrderReport savedReport = orderReportRepo.save(report);

        return mapToDetailsDTO(savedReport);
    }


    @Override
    @Transactional(readOnly = true)
    public List<OrderReportSummaryDTO> getMyReports() {

        User user = getAuthenticatedUser();

        return orderReportRepo
                .findByOrder_User_Id(user.getId())
                .stream()
                .map(this::mapToSummaryDTO)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public OrderReportDetailsDTO getMyReport(Long reportId) {

        User user = getAuthenticatedUser();

        OrderReport report = orderReportRepo
                .findByIdAndOrder_User_Id(
                        reportId,
                        user.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order report not found."
                        )
                );

        return mapToDetailsDTO(report);
    }


    @Override
    public OrderReportMessageResponseDTO addUserMessage(
            Long reportId,
            CreateOrderReportMessageRequestDTO request
    ) {

        User user = getAuthenticatedUser();

        OrderReport report = orderReportRepo
                .findByIdAndOrder_User_Id(
                        reportId,
                        user.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order report not found."
                        )
                );

        validateMessageAllowed(report);

        OrderReportMessage message = OrderReportMessage.builder()
                .report(report)
                .sender(user)
                .senderType(ReportSenderType.USER)
                .message(request.getMessage())
                .build();

        return mapToMessageDTO(
                messageRepo.save(message)
        );
    }


    // =========================================================
    // ADMIN
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<AdminOrderReportSummaryDTO> getAdminReports(
            String status
    ) {

        List<OrderReport> reports;

        if (status == null || status.isBlank()) {

            reports = orderReportRepo.findAll();

        } else {

            OrderReportStatus reportStatus;

            try {
                reportStatus =
                        OrderReportStatus.valueOf(
                                status.toUpperCase()
                        );

            } catch (IllegalArgumentException e) {

                throw new BadRequestException(
                        "Invalid report status: " + status
                );
            }

            reports =
                    orderReportRepo.findByStatus(reportStatus);
        }

        return reports.stream()
                .map(this::mapToAdminSummaryDTO)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public OrderReportDetailsDTO getAdminReport(Long reportId) {

        OrderReport report = findReport(reportId);

        return mapToDetailsDTO(report);
    }


    @Override
    public OrderReportDetailsDTO updateStatus(
            Long reportId,
            UpdateOrderReportStatusRequestDTO request
    ) {

        OrderReport report = findReport(reportId);

        OrderReportStatus current = report.getStatus();
        OrderReportStatus next = request.getStatus();

        if (current == next) {
            throw new BadRequestException(
                    "Report is already in " + current + " status."
            );
        }

        validateStatusTransition(current, next);

        report.setStatus(next);

        return mapToDetailsDTO(
                orderReportRepo.save(report)
        );
    }


    @Override
    public OrderReportMessageResponseDTO addAdminMessage(
            Long reportId,
            CreateOrderReportMessageRequestDTO request
    ) {

        OrderReport report = findReport(reportId);

        validateMessageAllowed(report);

        User admin = getAuthenticatedUser();

        OrderReportMessage message = OrderReportMessage.builder()
                .report(report)
                .sender(admin)
                .senderType(ReportSenderType.ADMIN)
                .message(request.getMessage())
                .build();

        return mapToMessageDTO(
                messageRepo.save(message)
        );
    }


    @Override
    public OrderReportDetailsDTO resolveReport(
            Long reportId,
            ResolveOrderReportRequestDTO request
    ) {

        OrderReport report = findReport(reportId);

        if (
                report.getStatus() == OrderReportStatus.RESOLVED ||
                        report.getStatus() == OrderReportStatus.CLOSED
        ) {
            throw new BadRequestException(
                    "Report is already closed."
            );
        }

        User admin = getAuthenticatedUser();

        OrderReportResolution resolution =
                OrderReportResolution.builder()
                        .report(report)
                        .type(request.getType())
                        .message(request.getMessage())
                        .resolvedAt(Instant.now())
                        .resolvedBy(admin)
                        .build();

        report.setResolution(resolution);
        report.setStatus(OrderReportStatus.RESOLVED);

        orderReportRepo.save(report);

        return mapToDetailsDTO(report);
    }


    // =========================================================
    // HELPERS
    // =========================================================

    private User getAuthenticatedUser() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepo
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Authenticated user not found."
                        )
                );
    }


    private OrderReport findReport(Long reportId) {

        return orderReportRepo
                .findById(reportId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order report not found."
                        )
                );
    }


    private void validateMessageAllowed(OrderReport report) {

        if (
                report.getStatus() == OrderReportStatus.RESOLVED ||
                        report.getStatus() == OrderReportStatus.REJECTED ||
                        report.getStatus() == OrderReportStatus.CLOSED
        ) {

            throw new BadRequestException(
                    "Messages cannot be added to a closed report."
            );
        }
    }


    private void validateStatusTransition(
            OrderReportStatus current,
            OrderReportStatus next
    ) {

        boolean valid = switch (current) {

            case OPEN ->
                    next == OrderReportStatus.IN_REVIEW ||
                            next == OrderReportStatus.REJECTED;

            case IN_REVIEW ->
                    next == OrderReportStatus.RESOLVED ||
                            next == OrderReportStatus.REJECTED;

            case RESOLVED ->
                    next == OrderReportStatus.CLOSED;

            case REJECTED ->
                    next == OrderReportStatus.CLOSED;

            case CLOSED ->
                    false;
        };

        if (!valid) {
            throw new BadRequestException(
                    "Invalid report status transition: "
                            + current + " -> " + next
            );
        }
    }


    // =========================================================
    // MAPPERS
    // =========================================================

    private OrderReportSummaryDTO mapToSummaryDTO(
            OrderReport report
    ) {

        return OrderReportSummaryDTO.builder()
                .id(report.getId())
                .orderId(report.getOrder().getId())
                .orderNumber(report.getOrder().getOrderNumber())
                .reason(report.getReason())
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .build();
    }


    private AdminOrderReportSummaryDTO mapToAdminSummaryDTO(
            OrderReport report
    ) {

        User user = report.getOrder().getUser();

        return AdminOrderReportSummaryDTO.builder()
                .id(report.getId())
                .orderId(report.getOrder().getId())
                .orderNumber(report.getOrder().getOrderNumber())
                .userId(user.getId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .reason(report.getReason())
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .build();
    }


    private OrderReportDetailsDTO mapToDetailsDTO(
            OrderReport report
    ) {

        List<OrderReportMessageResponseDTO> messages =
                messageRepo
                        .findByReport_IdOrderByCreatedAtAsc(
                                report.getId()
                        )
                        .stream()
                        .map(this::mapToMessageDTO)
                        .toList();

        OrderReportResolutionResponseDTO resolution = null;

        if (report.getResolution() != null) {

            resolution =
                    OrderReportResolutionResponseDTO.builder()
                            .type(report.getResolution().getType())
                            .message(report.getResolution().getMessage())
                            .resolvedAt(
                                    report.getResolution().getResolvedAt()
                            )
                            .build();
        }

        return OrderReportDetailsDTO.builder()
                .id(report.getId())
                .orderId(report.getOrder().getId())
                .orderNumber(report.getOrder().getOrderNumber())
                .reason(report.getReason())
                .description(report.getDescription())
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .messages(messages)
                .resolution(resolution)
                .build();
    }


    private OrderReportMessageResponseDTO mapToMessageDTO(
            OrderReportMessage message
    ) {

        return OrderReportMessageResponseDTO.builder()
                .id(message.getId())
                .senderType(message.getSenderType())
                .message(message.getMessage())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
