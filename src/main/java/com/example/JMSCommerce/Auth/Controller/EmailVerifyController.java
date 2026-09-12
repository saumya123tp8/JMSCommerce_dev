package com.example.JMSCommerce.Auth.Controller;

import com.example.JMSCommerce.Auth.Service.EmailVerificationService;
import com.example.JMSCommerce.Model.User;
import com.example.JMSCommerce.Repositories.UserRepo;
import com.example.JMSCommerce.Utility.ApiResponse;
import com.example.JMSCommerce.Utility.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class EmailVerifyController {

    private final EmailVerificationService emailVerificationService;
    private final UserRepo userRepo;

    @PostMapping("/reverify-email")
    public ResponseEntity<ApiResponse<Void>> sendVerificationCode() {//user can login with unverified mail and later can verify it


        User user = userRepo.findByEmail(SecurityUtils.getCurrentUserMail()).orElseThrow(()->new BadCredentialsException("something wrong with logined user"));
        emailVerificationService.sendVerificationEmail(user);

        return ResponseEntity.ok(
                ApiResponse.success(null,"Email sent")
        );
    }
}
