package com.example.JMSCommerce.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RazorpayClientConfig {

    private final RazorpayProperties properties;

    @Bean
    public RazorpayClient razorpayClient()
            throws RazorpayException {

        System.out.println(
                "Razorpay Key ID = " + properties.getKeyId()
        );

        System.out.println(
                "Razorpay Secret present = " +
                        (properties.getKeySecret() != null &&
                                !properties.getKeySecret().isBlank())
        );


        return new RazorpayClient(
                properties.getKeyId(),
                properties.getKeySecret()
        );
    }
}
