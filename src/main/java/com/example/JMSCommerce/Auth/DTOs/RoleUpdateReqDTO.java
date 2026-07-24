package com.example.JMSCommerce.Auth.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleUpdateReqDTO {

    private String userEmail;
    private List<String> roles;

}
