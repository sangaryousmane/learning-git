package com.example.springreact;

import com.example.springreact.ems.entities.Role;
import com.example.springreact.ems.entities.User;
import com.example.springreact.ems.utils.user_role_repo.RoleRepository;
import com.example.springreact.ems.utils.user_role_repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class SpringReactApplication {

	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringReactApplication.class, args);
	}

	@Bean
	public void addUsersAndRoles(){
		Role roleAdmin=new Role("ADMIN");
		Role roleUser=new Role("USER");
		Role roleManager=new Role("MANAGER");
		roleRepository.saveAll(List.of(roleAdmin, roleUser, roleManager));


		User user=new User("Ousmane", "ousmane@yahoo.com",
				passwordEncoder.encode("sangary"),true);
		User user1=new User("Peter", "peter@gmail.com",
				passwordEncoder.encode("sangary"),true);
		User user2=new User("Raul", "raul@gmail.com",
				passwordEncoder.encode("sangary"),true);
		user.getRoles().add(roleAdmin);
		user1.getRoles().add(roleUser);
		user2.getRoles().add(roleManager);
		userRepository.saveAll(List.of(user, user1, user2));

	}

}
