package com.example.JMSCommerce.Repositories;


import com.example.JMSCommerce.Model.OrderReport;
import com.example.JMSCommerce.Utility.enums.OrderReportStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderReportRepo
        extends JpaRepository<OrderReport, Long> {

    List<OrderReport> findByOrder_User_Id(Long userId);

    Optional<OrderReport> findByIdAndOrder_User_Id(
            Long reportId,
            Long userId
    );

    boolean existsByOrder_IdAndStatusIn(
            Long orderId,
            List<OrderReportStatus> statuses
    );

    List<OrderReport> findByStatus(OrderReportStatus status);

    @EntityGraph(attributePaths = {
            "order",
            "order.user"
    })
    List<OrderReport> findAll();
}