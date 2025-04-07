package com.pfe.backendpfe;

import com.pfe.backendpfe.auth.AuthenticationService;
import com.pfe.backendpfe.auth.RegistrationRequest;
import com.pfe.backendpfe.role.RoleName;
import com.pfe.backendpfe.security.JwtService;
import com.pfe.backendpfe.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import static com.pfe.backendpfe.role.RoleName.ROLE_ADMIN;
import static com.pfe.backendpfe.role.RoleName.ROLE_RH;

@EnableJpaAuditing
@EnableAsync
@EnableJpaRepositories(basePackages = "com.pfe.backendpfe")

@SpringBootApplication(scanBasePackages = "com.pfe.backendpfe")

public class BackendPfeApplication {
@Autowired
	private  JwtService jwtService;


	public static void main(String[] args)
	{
		SpringApplication.run(BackendPfeApplication.class, args);
	}
	/*@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(Role.builder().name("USER").build());
			}
		};
	}*/
	/*@Bean
	public CommandLineRunner commandLineRunner(AuthenticationService service, UserRepository userRepository) {
		return args -> {
			registerUserIfNotExists(service, userRepository, "admin@mail.com", "password", ROLE_ADMIN);
			registerUserIfNotExists(service, userRepository, "manager@mail.com", "password", ROLE_RH);
		};
	}


	private void registerUserIfNotExists(AuthenticationService service, UserRepository userRepository, String email, String password, RoleName role) {
		userRepository.findByEmail(email).ifPresentOrElse(
				user -> System.out.println(email + " already exists. Token: " + jwtService.generateToken(user)),
				() -> {
					var request = RegistrationRequest.builder()
							.email(email)
							.password(password)
							.role(role)
							.build();
					var token = service.register(request).getAccessToken();
					System.out.println("Created new user: " + email + ", Token: " + token);
				}
		);
	}

*/
}
