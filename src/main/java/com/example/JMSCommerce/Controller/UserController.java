package com.example.JMSCommerce.Controller;


import com.example.JMSCommerce.DTOs.UserDTO;
import com.example.JMSCommerce.Model.User;
import com.example.JMSCommerce.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> findAllUsers(){
        return userService.findAllUsers();
    }

    @PostMapping
    public void createUser(UserDTO userDTO){
        userService.createUser(userDTO);
    }
}
