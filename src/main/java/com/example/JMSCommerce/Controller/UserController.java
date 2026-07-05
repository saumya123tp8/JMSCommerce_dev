package com.example.JMSCommerce.Controller;


import com.example.JMSCommerce.DTOs.UserDTO;
import com.example.JMSCommerce.Model.User;
import com.example.JMSCommerce.Services.UserService;
import com.example.JMSCommerce.Utility.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<User>>> findAllUsers(){
        return ResponseEntity.ok(ApiResponse.success(userService.findAllUsers(),"Fetched All Users"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(UserDTO userDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(userService.createUser(userDTO),"User Created Successfully"));
    }
}
