package com.example.JMSCommerce.Auth.Controller;

import com.example.JMSCommerce.Auth.DTOs.RoleUpdateReqDTO;
import com.example.JMSCommerce.Auth.Service.DeveloperService;
import com.example.JMSCommerce.DTOs.UserDTO;
import com.example.JMSCommerce.Utility.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/developer")
@RequiredArgsConstructor
public class DeveloperController {
    private final DeveloperService developerService;

    @PostMapping("/update_role")
    public ResponseEntity<ApiResponse<UserDTO>> updateUserRole(@RequestBody RoleUpdateReqDTO roleUpdateReqDTO){
        return ResponseEntity.ok(ApiResponse.success(developerService.updateUserRole(roleUpdateReqDTO),"users role updated successfully"));
    }

}
