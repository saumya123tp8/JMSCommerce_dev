package com.example.JMSCommerce.Model;

import com.example.JMSCommerce.Utility.enums.ResolutionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "order_report_resolutions")
public class OrderReportResolution extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_id", nullable = false, unique = true)
    private OrderReport report;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ResolutionType type;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private Instant resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resolved_by", nullable = false)
    private User resolvedBy;
}