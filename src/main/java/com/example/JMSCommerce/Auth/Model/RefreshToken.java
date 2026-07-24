package com.example.JMSCommerce.Auth.Model;

import com.example.JMSCommerce.Model.BaseEntity;
import com.example.JMSCommerce.Model.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Primary;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_token", indexes = {
        @Index(name = "idx_resfresh_token_jti",columnList = "jti", unique = true),
        @Index(name = "idx_resfresh_token_user",columnList = "user_id"),
        @Index(name = "idx_refresh_token_expires", columnList = "expired_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken extends BaseEntity {

    @Column(name="jti", nullable = false, unique = true, updatable = false)
    private String jti;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(nullable = false)
    @Builder.Default
    private boolean revoked=false;

    @Column(name = "expired_at",nullable = false)
    private Instant expiresAt;

    private String replacedByToken;

}
