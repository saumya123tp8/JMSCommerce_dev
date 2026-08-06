package com.example.JMSCommerce.Model;

import com.example.JMSCommerce.Utility.enums.SelectionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customization_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE customization_group SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class CustomizationGroup extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SelectionType selectionType;

    @Column(nullable = false)
    private Integer minSelection;

    @Column(nullable = false)
    private Integer maxSelection;

    @Builder.Default
    private Integer displayOrder = 0;

    @Builder.Default
    private Boolean active = true;

    @OneToMany(
            mappedBy = "customizationGroup",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<CustomizationOption> options = new ArrayList<>();

    public void addOption(CustomizationOption option) {

        if (!options.contains(option)) {
            options.add(option);
        }

        option.setCustomizationGroup(this);

    }

    public void removeOption(CustomizationOption option) {

        options.remove(option);
        option.setCustomizationGroup(null);

    }

    @Builder.Default
    @Column(nullable = false)
    private Boolean required = false;
}
