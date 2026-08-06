package com.example.JMSCommerce.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_variant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE product_variant SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class ProductVariant extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(
            mappedBy = "variant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<VariantAttribute> attributes = new ArrayList<>();

    public void addAttribute(VariantAttribute attribute) {

        if (!attributes.contains(attribute)) {
            attributes.add(attribute);
        }

        attribute.setVariant(this);

    }

    public void removeAttribute(VariantAttribute attribute) {

        attributes.remove(attribute);
        attribute.setVariant(null);

    }
    /**
     * Examples:
     * Default
     * Black / M
     * 256 GB
     * Large
     */
    @Column(nullable = false)
    private String displayName;

    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal mrp;

    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal sellingPrice;

    @PositiveOrZero
    @Builder.Default
    private Integer stock = 0;

    @Column(unique = true)
    private String sku;

    @Column(unique = true)
    private String barcode;

    @Builder.Default
    private Boolean active = true;

}