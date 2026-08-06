package com.example.JMSCommerce;

import com.example.JMSCommerce.Model.Role;
import com.example.JMSCommerce.Repositories.RoleRepository;
import com.example.JMSCommerce.Utility.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        createRole(AppConstants.ADMIN_ROLE);
        createRole(AppConstants.GUEST_ROLE);
        createRole(AppConstants.DEVELOPER_ROLE);
    }

    private void createRole(String roleName) {

        roleRepository.findByName("ROLE_" + roleName)
                .orElseGet(() ->
                        roleRepository.save(
                                Role.builder()
                                        .name("ROLE_" + roleName)
                                        .build()
                        )
                );
    }
}
