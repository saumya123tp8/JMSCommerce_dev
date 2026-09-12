package com.example.JMSCommerce.Auth.Service;

import com.example.JMSCommerce.Auth.Model.EmailVerificationToken;
import com.example.JMSCommerce.Auth.Repositoy.EmailVerificationTokenRepository;
import com.example.JMSCommerce.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${resend.base-url-email}")
    private String verificationBaseUrl;

    public void sendVerificationEmail(User user) {

        String token = generateToken();

        String tokenHash = hashToken(token);

        EmailVerificationToken verificationToken =
                tokenRepository.findByUserId(user.getId())
                        .orElseGet(EmailVerificationToken::new);

        verificationToken.setUser(user);
        verificationToken.setTokenHash(tokenHash);
        verificationToken.setCreatedAt(Instant.now());
        verificationToken.setExpiresAt(
                Instant.now().plus(30, ChronoUnit.MINUTES)
        );
        verificationToken.setVerifiedAt(null);

        tokenRepository.save(verificationToken);



        String verificationUrl =
                verificationBaseUrl+"?token="
                        + token;

        emailService.sendVerificationEmail(
                user.getEmail(),
                user.getName(),
                verificationUrl
        );
    }

    // generateToken()
    private String generateToken() {

        byte[] bytes = new byte[32];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
    // hashToken()
    private String hashToken(String token) {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(token.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 algorithm not available", e
            );
        }
    }


    @Transactional
    public void verifyEmail(String token) {

        if (token == null || token.isBlank()) {
            throw new RuntimeException("Verification token is missing");
        }

        String tokenHash = hashToken(token);

        EmailVerificationToken verificationToken =
                tokenRepository.findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid verification token"
                                )
                        );

        if (verificationToken.getVerifiedAt() != null) {
            throw new RuntimeException(
                    "Email is already verified"
            );
        }

        if (verificationToken.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException(
                    "Verification token has expired"
            );
        }

        User user = verificationToken.getUser();

        user.setEmailVerified(true);

        verificationToken.setVerifiedAt(Instant.now());

        tokenRepository.save(verificationToken);
    }
}
