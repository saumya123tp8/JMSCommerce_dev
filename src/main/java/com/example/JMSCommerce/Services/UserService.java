package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.DTOs.UserDTO;
import com.example.JMSCommerce.Exception.CompulsoryDataMissingException;
import com.example.JMSCommerce.Exception.DuplicateRecordException;
import com.example.JMSCommerce.Model.Role;
import com.example.JMSCommerce.Model.User;
import com.example.JMSCommerce.Repositories.RoleRepository;
import com.example.JMSCommerce.Repositories.UserRepo;
import com.example.JMSCommerce.Utility.AppConstants;
import com.example.JMSCommerce.Utility.enums.Provider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
//    private final UserRepo userRepo;
    private final RoleRepository roleRepository;
    public Optional<List<UserDTO>> findAllUsers() {
        List<UserDTO> users = userRepo.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();

        return users.isEmpty()
                ? Optional.empty()
                : Optional.of(users);
    }

    public UserDTO createUser(UserDTO userDto) {

//        User user = User.builder()
//                .email(userDTO.getEmail())
//                .age(userDTO.getAge())
//                .name(userDTO.getName())
//                .password(userDTO.getPassword())
//                .build();
//        userRepo.save(user);
//        return modelMapper.map(user,UserDTO.class);
        System.out.println(userDto.getName());
        if(userDto.getEmail()==null||userDto.getEmail().isBlank()){
            throw new CompulsoryDataMissingException("Email is required");
        }
        if(userRepo.existsByEmail(userDto.getEmail())){
            throw new DuplicateRecordException("Email is already taken");
        }

        User user=modelMapper.map(userDto, User.class);
        user.setProvider(userDto.getProvider()!=null?userDto.getProvider(): Provider.LOCALE);
        user.setEnabled(true);//initialy user will be enabled
        //role assign to new user for authorization
        //TODO

        Role role = roleRepository.findByName("ROLE_"+ AppConstants.GUEST_ROLE).orElse(null);
        if (user.getRoles() == null) {// because we are using builder annotation
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(role);
        User savedUser=userRepo.save(user);

        return modelMapper.map(savedUser, UserDTO.class);
    }
}
