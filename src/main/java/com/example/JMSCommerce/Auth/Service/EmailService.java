package com.example.JMSCommerce.Auth.Service;


public interface EmailService {

    void sendVerificationEmail(
            String email,
            String name,
            String verificationUrl
    );
}