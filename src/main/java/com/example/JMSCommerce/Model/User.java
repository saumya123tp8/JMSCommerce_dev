package com.example.JMSCommerce.Model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name = "users")
public class User extends BaseEntity{

    private String fname;
    private String lname;
    private int age;

}
