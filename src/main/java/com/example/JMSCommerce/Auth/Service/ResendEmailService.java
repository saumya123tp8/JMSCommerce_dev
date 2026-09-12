package com.example.JMSCommerce.Auth.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResendEmailService implements EmailService {

    private final RestTemplate restTemplate;

    @Value("${resend.api-key}")
    private String apiKey;

    @Value("${resend.from-email}")
    private String fromEmail;

    @Override
    public void sendVerificationEmail(
            String email,
            String name,
            String verificationUrl
    ) {

        String url = "https://api.resend.com/emails";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String html = """
                <html>
                <body>
                    <h2>Welcome to JMSCommerce, %s!</h2>

                    <p>
                        Thank you for creating an account.
                    </p>

                    <p>
                        Please verify your email address by clicking
                        the button below:
                    </p>

                    <p>
                        <a href="%s"
                           style="
                               display:inline-block;
                               padding:12px 20px;
                               background:#000;
                               color:#fff;
                               text-decoration:none;
                               border-radius:6px;
                           ">
                            Verify Email
                        </a>
                    </p>

                    <p>
                        This verification link will expire in 30 minutes.
                    </p>

                    <p>
                        If you did not create this account, you can
                        safely ignore this email.
                    </p>

                    <p>
                        Thanks,<br>
                        JMSCommerce Team
                    </p>
                </body>
                </html>
                """.formatted(name, verificationUrl);

        Map<String, Object> body = Map.of(
                "from", fromEmail,
                "to", new String[]{email},
                "subject", "Verify your JMSCommerce email",
                "html", html
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        try {

            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            url,
                            request,
                            String.class
                    );

            System.out.println("Resend response: " + response.getBody());

        } catch (Exception e) {

            System.err.println(
                    "Failed to send email through Resend: "
                            + e.getMessage()
            );

            throw e;
        }
    }


}