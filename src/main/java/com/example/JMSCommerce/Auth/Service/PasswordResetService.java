package com.example.JMSCommerce.Auth.Service;

import com.example.JMSCommerce.Auth.Model.PasswordResetToken;
import com.example.JMSCommerce.Auth.Repositoy.PasswordResetTokenRepository;
import com.example.JMSCommerce.Auth.Repositoy.RefreshTokenRepo;
import com.example.JMSCommerce.Model.User;
import com.example.JMSCommerce.Repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class PasswordResetService {

    private final UserRepo userRepo;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private final RefreshTokenRepo refreshTokenRepo;

    @Value("${resend.base-url-email}")
    private String verificationBaseUrl;

    @Transactional
    public void forgotPassword(String email) {

        User user = userRepo.findByEmail(email)
                .orElse(null);

        /*
         * Do not reveal whether the email exists.
         */
        if (user == null) {
            return;
        }

        String token = generateToken();
        String tokenHash = hashToken(token);

        PasswordResetToken resetToken =
                tokenRepository.findByUserId(user.getId())
                        .orElseGet(PasswordResetToken::new);

        Instant now = Instant.now();

        resetToken.setUser(user);
        resetToken.setTokenHash(tokenHash);
        resetToken.setCreatedAt(now);
        resetToken.setExpiresAt(
                now.plus(30, ChronoUnit.MINUTES)
        );
        resetToken.setUsedAt(null);

        tokenRepository.save(resetToken);

        String resetUrl =
                verificationBaseUrl+"/reset-password?token="
                        + token;

        emailService.sendPasswordResetEmail(
                user.getEmail(),
                user.getName(),
                resetUrl
        );
    }

    @Transactional
    public void resetPassword(
            String token,
            String newPassword
    ) {

        if (token == null || token.isBlank()) {
            throw new RuntimeException(
                    "Reset token is missing"
            );
        }

        if (newPassword == null || newPassword.isBlank()) {
            throw new RuntimeException(
                    "New password is required"
            );
        }

        String tokenHash = hashToken(token);

        PasswordResetToken resetToken =
                tokenRepository.findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid password reset token"
                                )
                        );

        if (resetToken.getUsedAt() != null) {
            throw new RuntimeException(
                    "Password reset link has already been used"
            );
        }

        if (resetToken.getExpiresAt()
                .isBefore(Instant.now())) {

            throw new RuntimeException(
                    "Password reset link has expired"
            );
        }

        User user = resetToken.getUser();

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        resetToken.setUsedAt(Instant.now());

        tokenRepository.save(resetToken);
        userRepo.save(user);

        refreshTokenRepo.revokeAllByUserId(user.getId());
    }

    private String generateToken() {

        byte[] bytes = new byte[32];

        SecureRandom secureRandom =
                new SecureRandom();

        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private String hashToken(String token) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            token.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "SHA-256 algorithm not available",
                    e
            );
        }
    }
}