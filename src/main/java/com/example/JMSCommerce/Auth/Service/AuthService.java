package com.example.JMSCommerce.Auth.Service;

import com.example.JMSCommerce.DTOs.UserDTO;
import com.example.JMSCommerce.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class  AuthService {
    private UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserDTO registerUser(UserDTO userDto) {
        //why we create a different method for register user
        //if we can use simply create user
        // because in future we may use password validation and many other thing
        //validate email
        //validate phone
        //verify mail
        //verify phone
        //verify password
        //encode password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        UserDTO userDto1=userService.createUser(
                userDto
        );
        return userDto1;
    }

//    public void loginUser(String email, String password) {
//
//    }

}
