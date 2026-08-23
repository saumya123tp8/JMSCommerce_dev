package com.example.JMSCommerce.Model;

import com.example.JMSCommerce.Utility.enums.OrderReportReason;
import com.example.JMSCommerce.Utility.enums.OrderReportStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(
        name = "order_reports",
        indexes = {
                @Index(name = "idx_order_report_order", columnList = "order_id"),
                @Index(name = "idx_order_report_status", columnList = "status")
        }
)
public class OrderReport extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private OrderReportReason reason;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private OrderReportStatus status = OrderReportStatus.OPEN;

    @OneToMany(
            mappedBy = "report",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<OrderReportMessage> messages = new ArrayList<>();

    @OneToOne(
            mappedBy = "report",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private OrderReportResolution resolution;


    public void addMessage(OrderReportMessage message) {

        if (!messages.contains(message)) {
            messages.add(message);
        }

        message.setReport(this);
    }

    public void removeMessage(OrderReportMessage message) {

        messages.remove(message);
        message.setReport(null);
    }

    public void setResolution(OrderReportResolution resolution) {

        this.resolution = resolution;

        if (resolution != null) {
            resolution.setReport(this);
        }
    }
}