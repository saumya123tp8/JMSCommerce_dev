package com.example.JMSCommerce.Model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Brand extends BaseEntity{
    private String brandName;
}
