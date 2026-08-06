package com.example.JMSCommerce.Model;

import com.example.JMSCommerce.Utility.enums.AdjustmentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Entity
@Table(name = "customization_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE customization_option SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class CustomizationOption extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customization_group_id", nullable = false)
    private CustomizationGroup customizationGroup;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AdjustmentType adjustmentType = AdjustmentType.FIXED;

    @PositiveOrZero
    @Column(nullable = false)
    @Builder.Default
    private BigDecimal adjustmentValue = BigDecimal.ZERO;

    @Builder.Default
    private Integer displayOrder = 0;

    @Builder.Default
    private Boolean active = true;


}
