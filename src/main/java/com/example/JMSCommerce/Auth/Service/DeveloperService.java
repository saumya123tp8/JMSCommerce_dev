package com.example.JMSCommerce.Auth.Service;

import com.example.JMSCommerce.Auth.DTOs.RoleUpdateReqDTO;
import com.example.JMSCommerce.DTOs.UserDTO;
import com.example.JMSCommerce.Exception.ResourceNotFoundException;
import com.example.JMSCommerce.Model.Role;
import com.example.JMSCommerce.Model.User;
import com.example.JMSCommerce.Repositories.RoleRepository;
import com.example.JMSCommerce.Repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeveloperService {
    private final UserRepo userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    public UserDTO updateUserRole(RoleUpdateReqDTO roleUpdateReqDTO) {

        User user = userRepository.findByEmail(roleUpdateReqDTO.getUserEmail()).orElseThrow(
                ()-> new ResourceNotFoundException("User is not valid to update its role")
        );

        List<Role> roles = roleRepository.findByNameIn(roleUpdateReqDTO.getRoles());
        if (roles.size() != roleUpdateReqDTO.getRoles().size()) {
            throw new ResourceNotFoundException("One or more roles are invalid");
        }
        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
       return modelMapper.map(user, UserDTO.class);
    }
}
