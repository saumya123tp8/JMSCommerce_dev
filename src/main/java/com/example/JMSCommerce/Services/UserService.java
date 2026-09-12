package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.DTOs.RoleDTO;
import com.example.JMSCommerce.DTOs.UserDTO;
import com.example.JMSCommerce.DTOs.UserProfileDTO;
import com.example.JMSCommerce.DTOs.UserProfileUpdateDTO;
import com.example.JMSCommerce.Exception.CompulsoryDataMissingException;
import com.example.JMSCommerce.Exception.DuplicateRecordException;
import com.example.JMSCommerce.Model.Role;
import com.example.JMSCommerce.Model.User;
import com.example.JMSCommerce.Repositories.RoleRepository;
import com.example.JMSCommerce.Repositories.UserRepo;
import com.example.JMSCommerce.Utility.AppConstants;
import com.example.JMSCommerce.Utility.SecurityUtils;
import com.example.JMSCommerce.Utility.enums.Provider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
//        System.out.println(userDto.getName());
//        System.out.println(userDto.getEmail());
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

//        Role role = roleRepository.findByName("ROLE_"+ AppConstants.GUEST_ROLE).orElse(null);
        Role role = roleRepository.findByName("ROLE_"+ AppConstants.GUEST_ROLE).orElse(null);
        if (user.getRoles() == null) {// because we are using builder annotation
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(role);
        User savedUser=userRepo.save(user);

        return modelMapper.map(savedUser, UserDTO.class);
    }

    public UserProfileDTO findLoginUsers() {
        String currentUserMail =
                SecurityUtils.getCurrentUserMail();
        User user = userRepo.findByEmail(currentUserMail).orElseThrow(()->new RuntimeException("Something wrong with user profile"));

        return mapToUserProfileDTO(user);

    }

    @Transactional
    public UserProfileDTO updateLoginUsers(UserProfileUpdateDTO req) {

        String currentUserMail = SecurityUtils.getCurrentUserMail();

        User user = userRepo.findByEmail(currentUserMail)
                .orElseThrow(() ->
                        new RuntimeException("Something wrong with user profile")
                );

        if (req == null) {
            throw new IllegalArgumentException("Profile update request cannot be null");
        }

        // Update only fields that were supplied
        if (req.getName() != null) {
            String name = req.getName().trim();

            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }

            user.setName(name);
        }

        if (req.getImage() != null) {
            String image = req.getImage().trim();

            if (image.isEmpty()) {
                user.setProfileImage(null);
            } else {
                user.setProfileImage(image);
            }
        }

        if (req.getPhone() != null) {
            String phone = req.getPhone().trim();

            if (phone.isEmpty()) {
                user.setPhone(null);
            } else {
                user.setPhone(phone);
                user.setPhoneVerified(false);
            }
        }

        if (req.getAge() != null) {
            user.setAge(req.getAge());
        }

        User savedUser = userRepo.save(user);

        return mapToUserProfileDTO(savedUser);
    }
    private UserProfileDTO mapToUserProfileDTO(User user) {

        return UserProfileDTO.builder()
                .email(user.getEmail())
                .age(user.getAge())
                .phone(user.getPhone())
                .image(user.getProfileImage())
                .emailVerified(user.isEmailVerified())
                .phoneVerified(user.isPhoneVerified())
                .name(user.getName())
                .provider(user.getProvider())
                .roles(
                        user.getRoles()
                                .stream()
                                .map(role ->
                                        RoleDTO.builder()
                                                .name(role.getName())
                                                .build()
                                )
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
