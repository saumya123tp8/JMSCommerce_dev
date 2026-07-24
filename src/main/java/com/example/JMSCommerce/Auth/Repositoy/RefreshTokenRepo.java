package com.example.JMSCommerce.Auth.Repositoy;

import com.example.JMSCommerce.Auth.Model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByJti(String jti);
}
