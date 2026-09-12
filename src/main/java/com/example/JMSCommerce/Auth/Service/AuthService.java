package com.example.JMSCommerce.Auth.Service;

import com.example.JMSCommerce.Adapters.UserAdapter;
import com.example.JMSCommerce.DTOs.UserDTO;
import com.example.JMSCommerce.DTOs.UserResDTO;
import com.example.JMSCommerce.Exception.DuplicateRecordException;
import com.example.JMSCommerce.Model.User;
import com.example.JMSCommerce.Repositories.UserRepo;
import com.example.JMSCommerce.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserAdapter userAdapter;
    private final UserService userService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    public UserResDTO registerUser(UserDTO userDto) {

        // 1. Check whether email is already registered
        User existingUser =
                userRepo.findByEmail(userDto.getEmail()).orElseThrow(()->new DuplicateRecordException("Email is already taken"));
//        if (existingUser != null) {

//            // Email already verified
//            if (existingUser.isEmailVerified()) {
//                throw new RuntimeException(
//                        "Email is already registered"
//                );
//            }
//
//            // Account exists but email is not verified.
//            // Send a new verification email.
//            emailVerificationService
//                    .sendVerificationEmail(existingUser);

//            throw new RuntimeException(
//                    "Email is already registered but not verified. " +
//                            "A new verification email has been sent."
//            );
//        }

        // 2. Encode password
        userDto.setPassword(
                passwordEncoder.encode(userDto.getPassword())
        );

        // 3. Create user
        User createdUser =
                userService.createUser(userDto);

        // 4. Send verification email
        emailVerificationService
                .sendVerificationEmail(createdUser);

        return userAdapter.mapToUserResDTO(createdUser);
    }
}

