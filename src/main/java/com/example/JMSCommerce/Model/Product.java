package com.example.JMSCommerce.Model;

import com.example.JMSCommerce.Utility.enums.CurrencyType;
import com.example.JMSCommerce.Utility.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

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
    @NotBlank
    private String name;

    @NotBlank
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CurrencyType currency = CurrencyType.INR;

    @NotNull
    @PositiveOrZero
    private BigDecimal mrp;

    @NotNull
    @PositiveOrZero
    private BigDecimal sellingPrice;
    @NotBlank
    private String primaryImage;
    @NotBlank
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
    @NotNull
    private Integer availableQuantity;
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

    private String sku;
    private String barcode;

}
