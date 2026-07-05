package com.example.JMSCommerce.Model;

import jakarta.persistence.*;
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


    private String title;
    private String description;
    private BigDecimal price;
    private String image;
//    private String category;
//    @ManyToOne
//    eager is by default
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private int rating;

}
