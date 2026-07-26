package com.example.JMSCommerce.Model;

import com.example.JMSCommerce.Model.BaseEntity;
import com.example.JMSCommerce.Model.Category;
import com.example.JMSCommerce.Utility.enums.SpecificationDataType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "specification_definitions")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpecificationDefinition extends BaseEntity {

    private String name;

    private String displayName;

    private String description;

    @Enumerated(EnumType.STRING)
    private SpecificationDataType dataType;

    private String unit;

    private Boolean required;

    private Boolean filterable;

    private Boolean searchable;

    private Integer displayOrder;

    private String placeholder;

    private String defaultValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}