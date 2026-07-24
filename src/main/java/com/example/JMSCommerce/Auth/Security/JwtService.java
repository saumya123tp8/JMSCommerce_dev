package com.example.JMSCommerce.Auth.Security;

import com.example.JMSCommerce.Model.Role;
import com.example.JMSCommerce.Model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@Getter
@Setter
public class JwtService {
     private final SecretKey key;
     private final long accessTokenSeconds;
     private final long refreshTokenSeconds;
     private final String issuer;

     public JwtService(@Value("${security.jwt.secret}") String secretKey,
                       @Value("${security.jwt.access-ttl-seconds}") long accessTokenSeconds,
                       @Value("${security.jwt.refresh-ttl-seconds}") long refreshTokenSeconds,
                       @Value("${security.jwt.issuer}") String issuer) {

         if(secretKey == null || secretKey.length() < 64) {
             throw new IllegalArgumentException("secretKey is invalid");
         }

         this.key=Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

         this.accessTokenSeconds = accessTokenSeconds;
         System.out.println("access token time period : "+accessTokenSeconds);
         this.refreshTokenSeconds = refreshTokenSeconds;
         this.issuer = issuer;

     }

     //generate access token
    public String generateAccessToken(User user) {
         Instant now = Instant.now();
         List<String> roles=user.getRoles()==null?List.of():user.getRoles().stream().map(Role::getName).toList();
        System.out.println("access token time period- : "+accessTokenSeconds);
         return Jwts.builder()
                 .id(UUID.randomUUID().toString())
                 .subject(user.getEmail())
                 .issuer(issuer)
                 .issuedAt(Date.from(now))
                 .expiration(new Date(now.toEpochMilli() + accessTokenSeconds*1000))
                 .claims(
                         Map.of(
                         "email",user.getEmail(),
                         "roles", roles,
                         "typ","access"
                 ))
                 .signWith(key, SignatureAlgorithm.HS512)
                 .compact();
    }


    //generate refresh token
    public String generateRefreshToken(User user, String jti) {
        Instant now = Instant.now();
        List<String> roles=user.getRoles()==null?List.of():user.getRoles().stream().map(Role::getName).toList();
        return Jwts.builder()
                .id(jti)
                .subject(user.getEmail())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(new Date(now.toEpochMilli() + refreshTokenSeconds*1000))
                .claims(
                        Map.of(
                                "typ","refresh"
                        ))
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
    }


    //Parse the token
    public Jws<Claims> parse(String token){
         // I wish exception should be handled by parent
             return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }

    public boolean isAccessToken(String token) {
         Claims c = parse(token).getPayload();
         System.out.println("Claim c :"+c.toString());
         return "access".equals(c.get("typ"));
    }
    public boolean isRefreshToken(String token) {
        try {
            Claims c = parse(token).getPayload();
            return "refresh".equals(c.get("typ"));
        }catch(ExpiredJwtException e){
            throw e;
        }catch(JwtException e){
            throw e;
        }
    }
    public UUID getUserId(String token) {
        Claims c = parse(token).getPayload();
        return UUID.fromString(c.getSubject());
    }

    public String getJti(String token) {
         return parse(token).getPayload().getId();
    }

    public List<String> getRoles(String token) {
         Claims c = parse(token).getPayload();
         return (List<String>)(c.get("roles"));
    }

    public String getEmail(String token) {
         Claims c = parse(token).getPayload();
         return (String)(c.get("email"));
    }


}
