package com.example.JMSCommerce.Auth.Security;

import com.example.JMSCommerce.Auth.Model.RefreshToken;
import com.example.JMSCommerce.Auth.Repositoy.RefreshTokenRepo;
import com.example.JMSCommerce.Model.Role;
import com.example.JMSCommerce.Model.User;
import com.example.JMSCommerce.Repositories.RoleRepository;
import com.example.JMSCommerce.Repositories.UserRepo;
import com.example.JMSCommerce.Utility.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final UserRepo userRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final RoleRepository roleRepository;

    @Value("${app.auth.frontend.success_url}")
    private String success_url;
    public String findEmailForGithub(OAuth2AuthenticationToken token_auth){

        OAuth2AuthorizedClient client =
                authorizedClientService.loadAuthorizedClient(
                        token_auth.getAuthorizedClientRegistrationId(),
                        token_auth.getName());

        if (client == null) {
            return null;
        }

        String githubAccessToken = client.getAccessToken().getTokenValue();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubAccessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response =
                restTemplate.exchange(
                        "https://api.github.com/user/emails",
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<List<Map<String, Object>>>() {}
                );

        List<Map<String, Object>> emails = response.getBody();

        if (emails == null) {
            return null;
        }

        return emails.stream()
                .filter(e -> Boolean.TRUE.equals(e.get("primary")))
                .filter(e -> Boolean.TRUE.equals(e.get("verified")))
                .map(e -> (String) e.get("email"))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("Successful authentication");
        logger.info(authentication.toString());

        //got user name
        //user mail
        // i will redirect it to frontend with a token
        OAuth2AuthenticationToken token_auth =
                (OAuth2AuthenticationToken) authentication;

        OAuth2User oAuth2User = token_auth.getPrincipal();

        String registrationId = "unknown";
        if(authentication instanceof OAuth2AuthenticationToken token){
            registrationId = token.getAuthorizedClientRegistrationId();
        }

        logger.info("registranId"+registrationId);
        logger.info("user"+oAuth2User.toString());

        User user;
        switch(registrationId){
            case "google"->{
                String googleId = oAuth2User.getAttributes().getOrDefault("sub","").toString();
                String email = oAuth2User.getAttributes().getOrDefault("email","").toString();
                String name = oAuth2User.getAttributes().getOrDefault("name","").toString();
                String pictures = oAuth2User.getAttributes().getOrDefault("picture","").toString();
                System.out.println("image"+pictures);
                User newUser = User.builder()
                        .email(email)
                        .name(name)
                        .profileImage(pictures)
                        .enabled(true)
                        .build();
                Role role = roleRepository.findByName("ROLE_"+ AppConstants.GUEST_ROLE).orElse(null);
                if (newUser.getRoles() == null) {// because we are using builder annotation
                    newUser.setRoles(new HashSet<>());
                }
                newUser.getRoles().add(role);
                user = userRepo.findByEmail(email).orElseGet(()->userRepo.save(newUser));
            }
            case "github"->{
                String githubId = oAuth2User.getAttributes().getOrDefault("id", "").toString();

                String login = oAuth2User.getAttributes().getOrDefault("login", "").toString();

                String avatarUrl = oAuth2User.getAttributes().getOrDefault("avatar_url", "").toString();

                String profileUrl = oAuth2User.getAttributes().getOrDefault("html_url", "").toString();

                String email = oAuth2User.getAttributes().get("email") != null
                        ? oAuth2User.getAttributes().get("email").toString()
                        : (
                                findEmailForGithub(token_auth)
                        );

                String name = oAuth2User.getAttributes().get("name") != null
                        ? oAuth2User.getAttributes().get("name").toString()
                        : login;
                System.out.println("image"+avatarUrl);
                System.out.println("name"+name);
                System.out.println("mail"+email);
                User newUser = User.builder()
                        .email(email)
                        .name(name)
                        .profileImage(avatarUrl)
                        .enabled(true)
                        .build();
                Role role = roleRepository.findByName("ROLE_"+ AppConstants.GUEST_ROLE).orElse(null);
                if (newUser.getRoles() == null) {// because we are using builder annotation
                    newUser.setRoles(new HashSet<>());
                }
                newUser.getRoles().add(role);
                user = userRepo.findByEmail(email).orElseGet(()->userRepo.save(newUser));
            }
            default -> {
                throw new RuntimeException("Invalid registration Id");
            }
        }



        // now we have a user saved in db
        //now I will share a refresh to to user
        // he can use this refresh token to get access token
        String jti = UUID.randomUUID().toString();
        RefreshToken refreshTokenObj = RefreshToken.builder()
                .jti(jti)
                .user(user)
                .revoked(false)
//                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTokenSeconds()))
                .build();

        refreshTokenRepo.save(refreshTokenObj);
//        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user, refreshTokenObj.getJti());
        cookieService.attachRefreshTokenCookie(response,refreshToken,(int)jwtService.getRefreshTokenSeconds());

//        response.getWriter().write("Login Successful");
        response.sendRedirect(success_url);
    }
}
