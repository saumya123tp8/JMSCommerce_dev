package com.example.JMSCommerce.Exception;

public class BadCredentialsCustomException extends RuntimeException {
    public BadCredentialsCustomException(String message) {
        super(message);
    }
}
