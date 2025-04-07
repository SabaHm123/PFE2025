package com.pfe.backendpfe.user;

import com.pfe.backendpfe.auth.AuthenticationService;
import com.pfe.backendpfe.auth.RegistrationRequest;
import com.pfe.backendpfe.role.Role;
import com.pfe.backendpfe.role.RoleName;
import com.pfe.backendpfe.role.RoleRepository;
import com.pfe.backendpfe.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public void run(String... args) {
        // Vérifier et insérer les rôles si nécessaire
        if (roleRepository.count() == 0) {
            System.out.println("Insertion des rôles...");
            roleRepository.save(new Role(RoleName.ROLE_ADMIN));
            roleRepository.save(new Role(RoleName.ROLE_RH));
            roleRepository.save(new Role(RoleName.ROLE_COLLAB));
            roleRepository.save(new Role(RoleName.ROLE_PENDING));
        }

        // Vérifier que l'admin existe avant de le créer
        registerUserIfNotExists("admin@mail.com", "admin123", RoleName.ROLE_ADMIN);
        registerUserIfNotExists("manager@mail.com", "password", RoleName.ROLE_RH);
    }

    private void registerUserIfNotExists(String email, String password, RoleName roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Le rôle " + roleName + " n'existe pas."));

        userRepository.findByEmail(email).ifPresentOrElse(
                user -> System.out.println(email + " already exists."),
                () -> {
                    var request = RegistrationRequest.builder()
                            .email(email)
                            .password(passwordEncoder.encode(password)) // S'assurer que le mot de passe est bien encodé
                            .role(roleName)
                            .build();
                    authenticationService.register(request);
                    System.out.println("Utilisateur créé: " + email);
                }
        );
    }
}
