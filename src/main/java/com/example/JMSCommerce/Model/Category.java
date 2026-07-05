package com.example.JMSCommerce.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@SQLDelete(sql="UPDATE category SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Table(name = "category")
public class Category extends BaseEntity{

    @Column(nullable = false)
    private String name;

}
