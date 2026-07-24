package com.example.JMSCommerce.Auth.DTOs;

public record LoginRequest(
        String email,
        String password
) {
}
