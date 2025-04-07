package com.pfe.backendpfe.auth;

import com.pfe.backendpfe.activity.Activite;
import com.pfe.backendpfe.activity.ActiviteRepository;
import com.pfe.backendpfe.email.EmailService;
import com.pfe.backendpfe.role.RoleName;
import com.pfe.backendpfe.user.User;
import com.pfe.backendpfe.user.UserRepository;
import com.pfe.backendpfe.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("auth")
//@Tag(name = "Authentication")
public class AuthenticationController {
    @Autowired
    private UserService userService;
    private final AuthenticationService service;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private  final ActiviteRepository activiteRepository;
    @Autowired
    public AuthenticationController(AuthenticationService service, UserRepository userRepository, EmailService emailService, ActiviteRepository activiteRepository) {
        this.service = service;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.activiteRepository = activiteRepository;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        // Fetch the activite if it's provided in the request
        if (request.getActiviteId() != null) {
            Activite activite = activiteRepository.findById(Long.valueOf(request.getActiviteId()))
                    .orElseThrow(() -> new RuntimeException("Activity not found"));
            request.setActivite(activite); // Set the activite in the request object
        }

        service.register(request);  // Continue with the registration process
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/Authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {

        AuthenticationResponse response = service.authenticate(request);
        return ResponseEntity.ok(response);
    }


   /* @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String token
    ) throws MessagingException {
        service.activateAccount(token);
    }*/

   /* @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) throws MessagingException {

        service.forgotPassword(email);
        return ResponseEntity.ok("Reset password email sent successfully.");
    }*/

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) throws MessagingException {
        String email = request.get("email");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required."));
        }

        service.forgotPassword(email);

        // Retourner une réponse JSON au lieu d'un texte brut
        return ResponseEntity.ok(Map.of("message", "Reset password email sent successfully."));
    }




    /* @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {

        service.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password has been successfully reset.");
    }*/

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        if (token == null || newPassword == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token et mot de passe sont requis."));
        }

        log.info("Tentative de réinitialisation avec le token : {}", token);

        try {
            service.resetPassword(token, newPassword);
            return ResponseEntity.ok(Map.of("message", "Password has been successfully reset."));
        } catch (UsernameNotFoundException e) {
            log.error("Utilisateur non trouvé pour le token : {}", token);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Utilisateur non trouvé."));
        } catch (Exception e) {
            log.error("Erreur lors de la réinitialisation du mot de passe", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Erreur interne du serveur."));
        }
    }


}
