package com.example.JMSCommerce.Auth.Security;

import io.jsonwebtoken.*;
import com.example.JMSCommerce.Repositories.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepo userRepository;
    private Logger logger=LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

       String header=request.getHeader("Authorization");
       logger.info("Authorization header:"+header);
       if(header!=null && header.startsWith("Bearer ")) {
           String token=header.substring(7);//extract token
           /// extract, validate then authentication create and then set value in security context
           try{
               if(!jwtService.isAccessToken(token)){
                   filterChain.doFilter(request, response);
                   return;
               }
               Jws<Claims> parse=jwtService.parse(token);
               Claims payload=parse.getPayload();
               String subject=payload.getSubject();
//               Long user_id = payload.getId();
               String user_email= payload.getSubject();
               userRepository.findByEmail(user_email).ifPresent(user -> {

                   //check for user enable or not

                   if (user.isEnabled()) {
                       // user mil chuka hai database se
                       List<GrantedAuthority> authorities = user.getRoles() == null ? List.of() : user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
                       UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
                       authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                       //final line : to set the authentication to security context
                       if (SecurityContextHolder.getContext().getAuthentication() == null)
                           SecurityContextHolder.getContext().setAuthentication(authentication);
                   }else{
                       try{
                           filterChain.doFilter(request, response);
                       } catch (IOException | ServletException e) {
                           throw new RuntimeException(e);
                       }
                       return ;
                   }


               });
           }catch (ExpiredJwtException e){
               request.setAttribute("error","Token Expired");
               e.printStackTrace();
           }catch (MalformedJwtException e){
               request.setAttribute("error","Token is Invalid");
               e.printStackTrace();
           }catch (JwtException e){
               request.setAttribute("error","Token is Invalid");
               e.printStackTrace();
           }catch (Exception e){
               request.setAttribute("error","Token is Invalid");
               e.printStackTrace();
           }

       }

       filterChain.doFilter(request, response);

    }


    // this will stop the filter for given request (/api/v1/auth/) eg. login and register for now
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException{
        return request.getRequestURI().startsWith("/api/v1/auth/");
    }

}
