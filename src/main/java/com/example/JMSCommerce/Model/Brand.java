package com.example.JMSCommerce.Model;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Brand extends BaseEntity{
    private String name;
    private Date stablishDate;
    private String description;
    private String logo;
}
