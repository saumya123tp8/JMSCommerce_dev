package com.example.JMSCommerce.Repositories;


import com.example.JMSCommerce.Model.OrderReportResolution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderReportResolutionRepo
        extends JpaRepository<OrderReportResolution, Long> {

    Optional<OrderReportResolution> findByReport_Id(Long reportId);
}