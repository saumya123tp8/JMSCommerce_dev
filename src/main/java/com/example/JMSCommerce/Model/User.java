package com.example.JMSCommerce.Model;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import com.example.JMSCommerce.Model.BaseEntity;
import com.example.JMSCommerce.Utility.enums.Provider;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_phone", columnList = "phone")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {
//UserDetails represents the authenticated user that Spring Security uses internally.
//POST /login ->(Email, Password )-> Spring Security -> "Give me the user"
// [ Spring Security doesn't know your User class. It only understands UserDetails.]
    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

//    @Column(unique = true, length = 10)
    private String phone;

    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 90, message = "Age cannot be greater than 90")
    private Integer age;

    @Column(length = 3)
    private String countryCode;

    private String profileImage;

    @Builder.Default
    private boolean emailVerified = false;

    @Builder.Default
    private boolean phoneVerified = false;

    @Builder.Default
    private boolean enabled = true;

    @Builder.Default
    private boolean accountLocked = false;

    private Instant lastLoginAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Provider provider = Provider.LOCALE;

    /* =======================
            Relationships
       ======================= */

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY
    )
//    @Builder.Default
//    private List<Address> addresses = new ArrayList<>();


    /* =======================
        Spring Security
       ======================= */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}