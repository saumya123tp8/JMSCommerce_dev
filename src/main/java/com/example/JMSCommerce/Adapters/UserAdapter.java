package com.example.JMSCommerce.Adapters;

import com.example.JMSCommerce.DTOs.RoleDTO;
import com.example.JMSCommerce.DTOs.UserDTO;
import com.example.JMSCommerce.DTOs.UserResDTO;
import com.example.JMSCommerce.Model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserAdapter {
    public UserResDTO mapToUserResDTO(User user) {

        return UserResDTO.builder()
                .email(user.getEmail())
                .age(user.getAge())
                .phone(user.getPhone())
                .image(user.getProfileImage())
                .emailVerified(user.isEmailVerified())
                .phoneVerified(user.isPhoneVerified())
                .name(user.getName())
                .provider(user.getProvider())
                .roles(
                        user.getRoles()
                                .stream()
                                .map(role ->
                                        RoleDTO.builder()
                                                .name(role.getName())
                                                .build()
                                )
                                .collect(Collectors.toSet())
                )
                .build();
    }

    public UserDTO mapToUserDTO(User user) {

        return UserDTO.builder()
                .email(user.getEmail())
                .age(user.getAge())
                .phone(user.getPhone())
                .image(user.getProfileImage())
                .name(user.getName())
                .provider(user.getProvider())
                .roles(
                        user.getRoles()
                                .stream()
                                .map(role ->
                                        RoleDTO.builder()
                                                .name(role.getName())
                                                .build()
                                )
                                .collect(Collectors.toSet())
                )
                .build();
    }

}
