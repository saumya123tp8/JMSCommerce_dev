package com.example.JMSCommerce.DTOs;

import com.example.JMSCommerce.Utility.enums.Provider;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
        private String email;
        private String name;
        private String password;
        private String image;
        private String phone;
        private Integer age;
        private Provider provider;
        @Builder.Default
        private Set<RoleDTO> roles=new HashSet<>();
//    public boolean getEnabled() {
//    }

}
