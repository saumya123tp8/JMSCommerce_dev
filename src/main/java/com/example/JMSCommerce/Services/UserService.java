package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.DTOs.UserDTO;
import com.example.JMSCommerce.Model.User;
import com.example.JMSCommerce.Repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    public User createUser(UserDTO userDTO) {

        User user = User.builder().age(userDTO.getAge()).fname(userDTO.getFname()).lname(userDTO.getLname()).build();
        userRepo.save(user);
        return user;
    }
}
