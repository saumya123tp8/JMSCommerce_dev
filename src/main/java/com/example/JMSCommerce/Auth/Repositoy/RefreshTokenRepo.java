package com.example.JMSCommerce.Auth.Repositoy;

import com.example.JMSCommerce.Auth.Model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByJti(String jti);

    @Modifying
    @Query("""
    UPDATE RefreshToken r
    SET r.revoked = true
    WHERE r.user.id = :userId
      AND r.revoked = false
""")
    void revokeAllByUserId(@Param("userId") Long userId);
}
