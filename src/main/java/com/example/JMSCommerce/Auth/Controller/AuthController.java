package com.example.JMSCommerce.Auth.Controller;
import com.example.JMSCommerce.Auth.DTOs.LoginRequest;
import com.example.JMSCommerce.Auth.DTOs.RefreshTokenRequest;
import com.example.JMSCommerce.Auth.DTOs.TokenResponse;
import com.example.JMSCommerce.Auth.Model.RefreshToken;
import com.example.JMSCommerce.Auth.Repositoy.RefreshTokenRepo;
import com.example.JMSCommerce.Auth.Security.CookieService;
import com.example.JMSCommerce.Auth.Security.JwtService;
import com.example.JMSCommerce.Auth.Service.AuthService;
import com.example.JMSCommerce.DTOs.UserDTO;
import com.example.JMSCommerce.Exception.BadCredentialsCustomException;
import com.example.JMSCommerce.Model.User;
import com.example.JMSCommerce.Repositories.UserRepo;
import com.example.JMSCommerce.Utility.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final UserRepo userRepository;
    private AuthService authService;
    private JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private RefreshTokenRepo refreshTokenRepo;
    private final CookieService cookieService;
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> loginUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {// it will generate token
        Authentication authentication=authenticate(loginRequest);
        User user=userRepository.findByEmail(loginRequest.email()).orElseThrow(()->new BadCredentialsCustomException("user not found"));
        if(!user.isEnabled()){
            throw new DisabledException("User is disabled");
        }


        //first save refresh tokens jti in table with all information
        String jti=UUID.randomUUID().toString();
        RefreshToken refreshTokenDB=RefreshToken.builder()
                .jti(jti)
                .user(user)
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTokenSeconds()))
                .revoked(false)
                .build();
        refreshTokenRepo.save(refreshTokenDB);
        //generate token
        String accessToken = jwtService.generateAccessToken(user);
        //generate refresh token also but with saved jti into db
        String refreshToken= jwtService.generateRefreshToken(user,jti);

        //use cookie service to attach refresh token to cookie
        cookieService.attachRefreshTokenCookie(response,refreshToken,(int)jwtService.getRefreshTokenSeconds());
        cookieService.NoStoreHeader(response);

        TokenResponse tokenResponse=TokenResponse.of(accessToken,refreshToken,jwtService.getAccessTokenSeconds(),modelMapper.map(user, UserDTO.class));
        System.out.println(tokenResponse);
        return ResponseEntity.ok(ApiResponse.success(tokenResponse,"login successfully"));

    }

    private Authentication authenticate(LoginRequest loginRequest) {
        try{
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(),loginRequest.password()));
        } catch (Exception e) {
            throw new BadCredentialsCustomException("username or password is incorrect");
        }

    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> registerUser(@RequestBody UserDTO userDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(authService.registerUser(userDto), "User created succesfully"));
        } catch (Exception e) {
            throw new BadCredentialsCustomException("register data is not proper");
        }
    }

    //this end point will validate the refresh token first then return new access token and new refresh token
    //it will take req and refresh token from cookie(sent by client) and attach new tokens to response
    // we can expect refresh token in body but iy is optional(RefreshTokenRequest)
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> renewTokens(@RequestBody(required = false) RefreshTokenRequest refreshToken, HttpServletRequest req, HttpServletResponse res){
        String fetchedRefreshToken=readRefreshTokenFromReq(refreshToken,req).orElseThrow(()->new BadCredentialsCustomException("missing refresh token"));
        if(!jwtService.isRefreshToken(fetchedRefreshToken)){
            throw new BadCredentialsCustomException("Invalid refresh token type");
        }
        String jti=jwtService.getJti(fetchedRefreshToken);
        UUID userId=jwtService.getUserId(fetchedRefreshToken);
        //fetch old stored refresh token
        RefreshToken storedRefreshToken=refreshTokenRepo.findByJti(jti).orElseThrow(()->new BadCredentialsCustomException("Invalid refresh token"));
        if(storedRefreshToken.isRevoked()){
            throw new BadCredentialsCustomException("Refresh token revoked");
        }
        if(storedRefreshToken.getExpiresAt().isBefore(Instant.now())){
            throw new BadCredentialsCustomException("Refresh token expired");
        }

        if(!storedRefreshToken.getUser().getId().equals(userId)){
            throw new BadCredentialsCustomException("Refresh token does not belong to this user");
        }

        //now rotate refresh token
        storedRefreshToken.setRevoked(true);
        String newJti=UUID.randomUUID().toString();
        storedRefreshToken.setReplacedByToken(newJti);
        //now I have to save these changes in old refresh token
        refreshTokenRepo.save(storedRefreshToken);

        //now create new refresh token with new jti
        User user=storedRefreshToken.getUser();
        RefreshToken newRefreshTokenDB= RefreshToken.builder()
                .jti(newJti)
                .revoked(false)
                .user(user)
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTokenSeconds()))
                .build();

        refreshTokenRepo.save(newRefreshTokenDB);

        String newAccessToken= jwtService.generateAccessToken(user);
        String newRefreshToken= jwtService.generateRefreshToken(user, newRefreshTokenDB.getJti());

        cookieService.attachRefreshTokenCookie(res,newRefreshToken, (int) jwtService.getRefreshTokenSeconds());
        cookieService.NoStoreHeader(res);

        return ResponseEntity.ok(TokenResponse.of(newAccessToken,newRefreshToken, jwtService.getAccessTokenSeconds(), modelMapper.map(user,UserDTO.class)));

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest req, HttpServletResponse res){
        String fetchedRefreshToken = readRefreshTokenFromReq(null,req).orElseThrow(()->new BadCredentialsCustomException("Missing refresh token"));
        if(!jwtService.isRefreshToken(fetchedRefreshToken)){
            throw new BadCredentialsCustomException("Invalid Refresh Token");
        }
        String jti=jwtService.getJti(fetchedRefreshToken);
        RefreshToken fetchStoredRefreshToken=refreshTokenRepo.findByJti(jti).orElseThrow(()->new BadCredentialsCustomException("Invalid refresh Token"));
        fetchStoredRefreshToken.setRevoked(true);
        refreshTokenRepo.save(fetchStoredRefreshToken);
//        return ResponseEntity.ok();
        cookieService.clearRefreshTokenCookie(res);
        cookieService.NoStoreHeader(res);


        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Optional<String> readRefreshTokenFromReq(RefreshTokenRequest refreshToken, HttpServletRequest req) {
//        1. prefer refresh cookie from cookie
        if(req.getCookies()!=null){
            Optional<String> fromCookie = Arrays.stream(
                            req.getCookies()
                    )
                    .filter( c-> cookieService.getRefreshTokenCookieName().equals(c.getName()))
                    .map(c->c.getValue())
                    .filter(v->!v.isBlank())
                    .findFirst();

            if(fromCookie.isPresent())return fromCookie;
        }
        //it means check in body
        if(refreshToken!=null&&refreshToken.refreshToken()!=null&&!refreshToken.refreshToken().isBlank()){
            return Optional.of(refreshToken.refreshToken());
        }

        //3. custom header
        String refreshFromHeader=req.getHeader("X-Refresh-Token");
        if(refreshFromHeader!=null&&!refreshFromHeader.isBlank()){
            return Optional.of(refreshFromHeader);
        }

        //4. Authorization= Bearer <token>
        String authHeader=req.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader!=null&&authHeader.regionMatches(true,0,"Bearer",0,6)){
            String candidate=authHeader.substring(7).trim();
            if(candidate!=null&&!candidate.isEmpty()){
                try{
                    if(jwtService.isRefreshToken(candidate)){
                        return Optional.of(candidate);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return Optional.empty();
    }


}
