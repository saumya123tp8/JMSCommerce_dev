package com.example.JMSCommerce.Model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="product_specification",uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {
                        "product_id",
                        "specification_definition_id"
                }
        )
})
public class ProductSpecificationValue extends BaseEntity{

    //if we check that many product_specification entries are there for one product
    //product_specification has fk of product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    //Similiarly
    //if we check that many product_specification entries can be there for one sepcification
    //product_specification has fk of specificationDefinition
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specification_definition_id", nullable = false)
    private SpecificationDefinition specificationDefinition;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String value;
}
