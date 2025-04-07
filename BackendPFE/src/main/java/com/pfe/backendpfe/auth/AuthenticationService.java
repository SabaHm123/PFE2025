package com.pfe.backendpfe.auth;

import com.pfe.backendpfe.email.EmailService;
import com.pfe.backendpfe.email.EmailTemplateName;
import com.pfe.backendpfe.role.Role;
import com.pfe.backendpfe.role.RoleName;
import com.pfe.backendpfe.role.RoleRepository;
import com.pfe.backendpfe.security.JwtService;
import com.pfe.backendpfe.user.Token;
import com.pfe.backendpfe.user.TokenrRepository;
import com.pfe.backendpfe.user.User;
import com.pfe.backendpfe.user.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j

@Service
@RequiredArgsConstructor

public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final  RoleRepository roleRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenrRepository tokenrRepository;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Value("${application.mailing.frontend.activation-url:NOT_FOUND}")
    private String activationUrl;
    @Value("${application.mailing.frontend.forgot-url:NOT_FOUND}")
    private String forgotUrl;
    @PostConstruct
    public void init() {
        System.out.println("Activation URL: " + activationUrl);
        System.out.println("Forgot URL: " + forgotUrl);
    }




    /*public void register(RegistrationRequest request) throws MessagingException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé !");
        }

        Role pendingRole = roleRepository.findByName(RoleName.ROLE_PENDING)
                .orElseThrow(() -> new IllegalArgumentException("Le rôle PENDING n'existe pas dans la base"));
        String generatedUsername = request.getEmail().split("@")[0];

        var user = User.builder()
                .username(generatedUsername)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accoutLocked(false)
                .enabled(false)
                .roles(Set.of(pendingRole))
                .build();

        userRepository.save(user);
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var userDetails = (UserDetails) auth.getPrincipal();

        var user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        var claims = new HashMap<String, Object>();
        claims.put("fullname", user.FullName());
        var jwtToken = jwtService.generateToken(claims, user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }*/
    public AuthenticationResponse register(RegistrationRequest request) {
        String email = request.getEmail().trim();
        if (email.isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Adresse e-mail invalide !");
        }
        String localPart = email.split("@")[0];
        String[] parts = localPart.split("\\.");
        String firstname = parts.length > 0 ? parts[0] : "";
        String lastname = parts.length > 1 ? parts[1] : "";

        if (lastname.isEmpty()) {
            lastname = String.valueOf((int) (Math.random() * 100));
        }

        String username = firstname + lastname;
        int attempt = 1;
        String originalUsername = username;
        while (userRepository.findByUsername(username).isPresent()) {
            username = originalUsername + attempt;
            attempt++;
        }

        Role pendingRole = roleRepository.findByName(RoleName.ROLE_PENDING)
                .orElseThrow(() -> new IllegalArgumentException("Le rôle PENDING n'existe pas dans la base"));

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Ce username est déjà utilisé !");
        }

        User user = User.builder()
                .username(username)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accoutLocked(false)
                .enabled(false)
                .isApproved(false)
                .pendingApproval(true)
                .roles(Set.of(pendingRole))
                .build();

        try {
            userRepository.save(user);
            System.out.println("Utilisateur enregistré : " + user.getEmail());
        } catch (Exception e) {
            System.out.println("Erreur lors de l'enregistrement : " + e.getMessage());
        }

        var token = jwtService.generateToken(user);
        System.out.println("Token JWT généré : " + token);

        return new AuthenticationResponse(token);
    }

    /*public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), request.getPassword())
        );

        // Génération du token JWT
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }*/

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Vérifier les identifiants
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        // Générer le token JWT
        String token = jwtService.generateToken(user);

        // Récupérer les rôles de l'utilisateur
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getRoleName().name()) // ✅ Récupère RoleName depuis Role
                .collect(Collectors.toList());


        // ✅ Vérifier que userID et roles ne sont pas null
        System.out.println("UserID: " + user.getId());
        System.out.println("Roles: " + roles);

        // Retourner la réponse avec token, ID et rôles
        return new AuthenticationResponse(token, user.getId(), roles);
    }
    // This is just for frontend side handling
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        response.setStatus(HttpServletResponse.SC_OK);
    }



    /*private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                (String) newToken,
                "Account activation",
                "Reset Your Password"

        );
    }*/





    private Object generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivateCode(6);

        Token token = new Token(
                generatedToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                null, // validatedAt sera nul au début
                user
        );

        tokenrRepository.save(token);
        return generatedToken;
    }


    private String generateActivateCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for(int i = 0 ; i< length; i++){
            int randomIndex = secureRandom.nextInt(characters.length());//0..9
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

 /*   public void approveAccount(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isPendingApproval()) {
            throw new RuntimeException("User account is not pending approval");
        }

        user.setEnabled(true);
        user.setPendingApproval(false);
        userRepository.save(user);

        // Envoyer un email après l'approbation
      //  emailService.sendApprovalEmail(user.getEmail(), user.getUsername());
    }
*/







    public void forgotPassword(String email) throws MessagingException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var newToken = generateAndSaveResetToken(user);

        log.debug("Email trouvé dans la base de données : {}", user.getEmail());


        emailService.sendEmail(
                user.getEmail(),
                EmailTemplateName.RESET_PASSWORD,
                "http://localhost:4200/reset-password?token=" + newToken,
                "Réinitialisation de votre mot de passe");



    }

    private String generateResetCode(int length) {
        String characters = "0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder codeBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }


    private Object generateAndSaveResetToken(User user) {
        String generatedToken = generateActivateCode(6);

        Token token = new Token(
                generatedToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                null,
                user
        );

        tokenrRepository.save(token);
        return generatedToken;
    }

    public void resetPassword(String token, String newPassword) {
        Token savedToken = tokenrRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (LocalDateTime.now().isAfter(savedToken.getExpiredAt())) {
            throw new RuntimeException("Reset token has expired.");
        }

        var user = userRepository.findById(Long.valueOf(savedToken.getUser().getId()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword)); // Correction ici
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenrRepository.save(savedToken);
    }



}
