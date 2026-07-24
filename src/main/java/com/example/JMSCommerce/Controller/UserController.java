package com.example.JMSCommerce.Controller;


import com.example.JMSCommerce.DTOs.UserDTO;
import com.example.JMSCommerce.Model.User;
import com.example.JMSCommerce.Services.UserService;
import com.example.JMSCommerce.Utility.ApiResponse;
import com.example.JMSCommerce.Utility.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<List<UserDTO>>> findAllUsers(){
        Optional<List<UserDTO>> userList = userService.findAllUsers();
        if(userList.isEmpty()){
            return ResponseEntity.ok(ApiResponse.success(List.of(),"No user existes"));
        }
        return ResponseEntity.ok(ApiResponse.success(userList.get(),"List of users"));
    }

    @PostMapping
    @PreAuthorize(AppConstants.HAS_ADMIN_OR_DEVELOPER)
    public ResponseEntity<ApiResponse<UserDTO>> createUser(UserDTO userDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(userService.createUser(userDTO),"User Created Successfully"));
    }
}
