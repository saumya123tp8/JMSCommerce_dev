package com.example.JMSCommerce.Model;

import com.example.JMSCommerce.Utility.enums.CurrencyType;
import com.example.JMSCommerce.Utility.enums.InventoryType;
import com.example.JMSCommerce.Utility.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@SQLDelete(sql="UPDATE product SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at is null")
@Table(name = "product")
public class Product extends BaseEntity{

    private String name;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CurrencyType currency = CurrencyType.INR;

//    @PositiveOrZero
//    private BigDecimal mrp;
//
//
//    private BigDecimal sellingPrice;

    private String primaryImage;

    private String slug;
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String description;

//    private String category;
//    @ManyToOne
//    eager is by default
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
//    @Builder.Default
//    private Integer availableQuantity = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryType inventoryType = InventoryType.FINITE;

    @Builder.Default
    private Double rating = 0.0;
    @Builder.Default
    private Integer ratingCount = 0;
    @Builder.Default
    private Integer reviewCount = 0;
//    private int rating;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ProductStatus status = ProductStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

//    private String sku;
//    private String barcode;
@OneToMany(
        mappedBy = "product",
        cascade = CascadeType.ALL,
        orphanRemoval = true
)
@Builder.Default
private List<ProductVariant> variants = new ArrayList<>();

}
