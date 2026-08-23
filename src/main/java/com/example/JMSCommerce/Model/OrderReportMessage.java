package com.example.JMSCommerce.Model;

import com.example.JMSCommerce.Utility.enums.ReportSenderType;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(
        name = "order_report_messages",
        indexes = {
                @Index(name = "idx_report_message_report", columnList = "report_id")
        }
)
public class OrderReportMessage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_id", nullable = false)
    private OrderReport report;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ReportSenderType senderType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
}