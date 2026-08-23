package com.example.JMSCommerce.Repositories;

import com.example.JMSCommerce.Model.OrderReportMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderReportMessageRepo
        extends JpaRepository<OrderReportMessage, Long> {

    List<OrderReportMessage> findByReport_IdOrderByCreatedAtAsc(
            Long reportId
    );
}