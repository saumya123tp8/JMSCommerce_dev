package com.example.JMSCommerce.Auth.DTOs;


import com.example.JMSCommerce.DTOs.UserDTO;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long expiresIn,
        String tokenTyp,
        UserDTO userDto
) {
    public static TokenResponse of(String accessToken, String refreshToken, long expiresIn, UserDTO userDto) {
        return new TokenResponse(accessToken, refreshToken, expiresIn, "Bearer", userDto);
    }
}
