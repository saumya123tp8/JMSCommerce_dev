package com.example.JMSCommerce;

import com.example.JMSCommerce.Model.Role;
import com.example.JMSCommerce.Repositories.RoleRepository;
import com.example.JMSCommerce.Utility.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JMSCommerceApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(JMSCommerceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//we will create some default user just after the start of the applicatio
		//ADMIN
		//GUEST

		roleRepository.findByName("ROLE_"+ AppConstants.ADMIN_ROLE).ifPresentOrElse(role->{

		},()->{
			Role role = Role.builder()
					.name("ROLE_"+AppConstants.ADMIN_ROLE)
					.build();
			roleRepository.save(role);
		});

		roleRepository.findByName("ROLE_"+AppConstants.GUEST_ROLE).ifPresentOrElse(role->{

		},()->{
			Role role = Role.builder()
					.name("ROLE_"+AppConstants.GUEST_ROLE)
					.build();
			roleRepository.save(role);
		});

		roleRepository.findByName("ROLE_"+AppConstants.DEVELOPER_ROLE).ifPresentOrElse(role->{

		},()->{
			Role role = Role.builder()
					.name("ROLE_"+AppConstants.DEVELOPER_ROLE)
					.build();
			roleRepository.save(role);
		});
	}
}
