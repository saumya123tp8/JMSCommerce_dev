package com.example.JMSCommerce.Auth.Service;


public interface EmailService {

    void sendVerificationEmail(
            String email,
            String name,
            String verificationUrl
    );

    void sendPasswordResetEmail(
            String email,
            String name,
            String resetUrl
    );
}