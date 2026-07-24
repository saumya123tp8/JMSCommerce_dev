package com.example.JMSCommerce.Auth.Config;

import com.example.JMSCommerce.Auth.Security.JwtAuthenticationFilter;
import com.example.JMSCommerce.Auth.Security.OAuth2FailureHandler;
import com.example.JMSCommerce.Auth.Security.OAuth2SuccessHandler;
import com.example.JMSCommerce.Utility.ApiResponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Configuration
// if we want to add security using roles on individual method in any controller
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    //    private AuthenticationSuccessHandler successHandler;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private final OAuth2FailureHandler oAuth2FailureHandler;

//    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationSuccessHandler successHandler, OAuth2SuccessHandler oAuth2SuccessHandler, OAuth2FailureHandler oAuth2FailureHandler) {
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
////        this.successHandler=successHandler;
//        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
//        this.oAuth2FailureHandler = oAuth2FailureHandler;
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(
                        sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeHttpRequests ->

                                authorizeHttpRequests
                                        .requestMatchers("/api/v1/reviews/**").permitAll()
                                        .requestMatchers("/api/v1/auth/register").permitAll()
                                        .requestMatchers("/api/v1/auth/login").permitAll()
                                        .requestMatchers("/api/v1/auth/refresh").permitAll()
                                        .requestMatchers("/api/v1/developer/**").hasRole("DEVELOPER")
                                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                                        .anyRequest().authenticated()
                )
                .logout(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex.authenticationEntryPoint((req, res, e) -> {
                            //message send to client//when someone fail to access protected api
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json");
                            String message = "Unauthorized access to this ";
                            var objectMapper = new ObjectMapper();
                            String err = (String) req.getAttribute("error");
                            if (err == null) {

                                err = e.getMessage();
                            }
                            ApiResponse<Void> response = ApiResponse.error(err, message,req.getRequestURI());
                            objectMapper.writeValue(res.getWriter(), response);

                        }).accessDeniedHandler(
                                (req, res, e) -> {
                                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                    res.setContentType("application/json");

                                    ApiResponse<Void> response = ApiResponse.error(
                                            "Access Denied",
                                            "You don't have permission to access this resource.",
                                            req.getRequestURI()
                                    );


                                    new ObjectMapper().writeValue(res.getWriter(), response);
                                })


                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws RuntimeException {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.frontend_url}") String allowed_url
    ){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        String[] urls = allowed_url.trim().split(",");
        corsConfiguration.setAllowedOrigins(Arrays.asList(urls));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedMethods(List.of("POST","GET","PUT","DELETE","PATCH","QUERY","OPTIONS"));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }

}

