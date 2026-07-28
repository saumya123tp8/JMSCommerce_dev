package com.example.JMSCommerce.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "variant_attribute")
public class VariantAttribute extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specification_definition_id", nullable = false)
    private SpecificationDefinition specificationDefinition;

    @Column(nullable = false)
    private String value;
}
