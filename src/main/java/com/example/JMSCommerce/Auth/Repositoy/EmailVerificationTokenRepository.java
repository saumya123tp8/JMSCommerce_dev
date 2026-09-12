package com.example.JMSCommerce.Auth.Repositoy;

import com.example.JMSCommerce.Auth.Model.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository
        extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByTokenHash(String tokenHash);

    Optional<EmailVerificationToken> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
