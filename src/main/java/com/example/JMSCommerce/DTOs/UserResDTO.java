
package com.example.JMSCommerce.DTOs;

import com.example.JMSCommerce.Utility.enums.Provider;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResDTO {
    private String email;
    private String name;
    private String image;
    private String phone;
    private Integer age;
    private Provider provider;
    private Boolean emailVerified;
    private Boolean phoneVerified;
    @Builder.Default
    private Set<RoleDTO> roles=new HashSet<>();
//    public boolean getEnabled() {
//    }

}

