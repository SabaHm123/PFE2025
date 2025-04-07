package com.pfe.backendpfe.user;

import com.pfe.backendpfe.Service.AdminService;

import com.pfe.backendpfe.activity.Activite;
import com.pfe.backendpfe.activity.ActiviteRepository;
import com.pfe.backendpfe.activity.ActiviteService;
import com.pfe.backendpfe.auth.AuthenticationService;
import com.pfe.backendpfe.competence.Competence;
import com.pfe.backendpfe.competence.CompetenceService;
import com.pfe.backendpfe.email.EmailService;
import com.pfe.backendpfe.email.EmailTemplateName;
import com.pfe.backendpfe.role.Role;
import com.pfe.backendpfe.role.RoleName;
import com.pfe.backendpfe.role.RoleRepository;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/admin")
@Slf4j
//@PreAuthorize("hasRole('ADMIN')")


public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private  AdminService adminService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ActiviteRepository activiteRepository;
    @Autowired
    private ActiviteService activiteService;




    @Autowired
    public AdminController(AdminService adminService,EmailService emailService , ActiviteService activiteService) {
        this.adminService = adminService;
        this.emailService = emailService;
        this.activiteService = activiteService;

    }


  /*  @PostMapping("/assign-role")
    public ResponseEntity<String> assignRole(@RequestParam Long userId, @RequestParam String roleName) {
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body("Utilisateur introuvable !");
        }

        Role newRole = roleRepository.findByName(RoleName.valueOf("ROLE_" + roleName.toUpperCase()))
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));

        user.getRoles().clear();
        user.getRoles().add(newRole);

        user.setEnabled(true);
        userService.saveUser(user);

        return ResponseEntity.ok("Rôle attribué avec succès !");
    }*/
    // Vérifier si l'utilisateur est approuvé


    @PutMapping("/update-role/{userId}")
    public ResponseEntity<?> updateUserRole(@PathVariable Long userId, @RequestBody UpdateRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Role role = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new RuntimeException("Rôle introuvable"));

        user.getRoles().clear();
        user.getRoles().add(role);
        userRepository.save(user);

        return ResponseEntity.ok("Rôle mis à jour avec succès !");
    }

    /*  @PostMapping("/send-confirmation/{userId}")
       public ResponseEntity<String> sendConfirmationEmail(@PathVariable Long userId) {
           User user = userRepository.findById(userId)
                   .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

           try {
               String confirmationUrl = "http://localhost:4200/login?user=" + user.getId();
               emailService.sendEmail(
                       user.getEmail(),
                       EmailTemplateName.CONFIRM_EMAIL,
                       confirmationUrl,
                       "Confirmation de votre compte"
               );
               return ResponseEntity.ok("E-mail de confirmation envoyé avec succès à " + user.getEmail());
           } catch (MessagingException e) {
               return ResponseEntity.status(500).body("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
           }
       }*/
    @PostMapping("/send-confirmation")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    public ResponseEntity<String> sendConfirmationEmail(@RequestParam String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        try {
            String token = UUID.randomUUID().toString();
            user.setConfirmationToken(token);
            userRepository.save(user);

            String confirmationUrl = "http://localhost:4200/login?token=" + token;

            emailService.sendEmail(
                    user.getEmail(),
                    EmailTemplateName.CONFIRM_EMAIL,
                    confirmationUrl,
                    "Confirmation de votre compte"
            );

            return ResponseEntity.ok("E-mail de confirmation envoyé avec succès à " + user.getEmail());
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
        }
    }

    @GetMapping("/confirm-account")

    public ResponseEntity<String> confirmAccount(@RequestParam String token) {
        User user = userRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new RuntimeException("Token invalide ou expiré"));

        user.setEnabled(true);
        user.setConfirmationToken(null); // ✅ Supprime le token après confirmation
        userRepository.save(user);

        return ResponseEntity.ok("Compte activé avec succès !");
    }




    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public List<User> getAllUser() {
        return adminService.getAllUsers();
    }

    @GetMapping("users/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        // Mapping de l'entité User à UserResponse (DTO)
        UserResponse response = new UserResponse();
        response.setId(Long.valueOf(user.getId()));
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setActiviteId(user.getActivite() != null ? user.getActivite().getId() : null); // Récupérer l'ID de l'activité

        // Ajouter le nom de l'activité si elle existe
        if (user.getActivite() != null) {
            response.setActiviteName(user.getActivite().getName()); // Assurez-vous d'avoir un setter pour activiteName dans UserResponse
        }

        return ResponseEntity.ok(response);
    }


    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public User addUser(@RequestBody User user) {
        return adminService.addUser(user);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updatedUser = adminService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/pending-users")
    public ResponseEntity<List<User>> getPendingUsers() {
        List<User> pendingUsers = userRepository.findByRole(RoleName.ROLE_ADMIN);
        return ResponseEntity.ok(pendingUsers);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @Transactional
    @PutMapping("/approve-and-send-confirmation/{userId}")
    public ResponseEntity<String> approveAssignRoleAndSendEmail(
            @PathVariable Long userId,
            @RequestBody @Validated UpdateRoleRequest request) {

        try {
            log.info("Requête reçue : {}", request);

            // 🔹 Vérification si l'ID de l'activité est valide
            if (request.getActiviteId() == null) {
                log.error("ID de l'activité est nul pour l'utilisateur ID : {}", userId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'ID de l'activité ne peut pas être nul.");
            }

            // 🔹 Vérification de l'existence de l'utilisateur
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
            log.info("Utilisateur trouvé : {}", user);

            // 🔹 Vérification de l'existence du rôle demandé
            Role role = roleRepository.findByName(request.getRoleName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rôle introuvable"));
            log.info("Rôle trouvé : {}", role);

            // 🔹 Approuver et mettre à jour le rôle et l'activité
            user.setApproved(true);
            user.getRoles().clear();
            user.getRoles().add(role);

            // 🔹 Vérification de l'existence de l'activité
            Activite activite = activiteRepository.findById(request.getActiviteId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activité introuvable"));
            log.info("Activité trouvée : {}", activite);

            user.setActivite(activite);

            // 🔹 Générer et sauvegarder le token de confirmation
            String token = UUID.randomUUID().toString();
            user.setConfirmationToken(token);
            userRepository.save(user);
            log.info("Utilisateur mis à jour avec succès : {}", user);

            // 🔹 Générer le lien de confirmation et envoyer l'email
            String confirmationUrl = "http://localhost:4200/login?token=" + token;
            log.info("Lien de confirmation généré : {}", confirmationUrl);

            emailService.sendEmail(
                    user.getEmail(),
                    EmailTemplateName.CONFIRM_EMAIL,
                    confirmationUrl,
                    "Confirmation de votre compte et attribution d'activité"
            );

            // 🔹 Réponse avec succès
            return ResponseEntity.ok("Compte approuvé, rôle mis à jour, activité assignée et email de confirmation envoyé à " + user.getEmail());

        } catch (MessagingException e) {
            log.error("Erreur lors de l'envoi de l'email pour l'utilisateur ID : {} : {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'envoi de l'email : " + e.getMessage());



        } catch (Exception e) {
            log.error("Une erreur est survenue lors de la mise à jour de l'utilisateur ID : {} : {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue : " + e.getMessage());
        }
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @GetMapping("/roles")
    public ResponseEntity<List<String>> getAllRoles() {
        List<String> roles = Arrays.stream(RoleName.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }


    @Autowired
    private CompetenceService competenceService;
    @GetMapping("/{id}/competences")
    public List<Map<String, String>> getCompetences(@PathVariable Long id) {
        User collaborateur = userRepository.findById(id).orElseThrow();
        List<Map<String, String>> result = new ArrayList<>();


        for (Competence competence : collaborateur.getCompetences()) {
            Map<String, String> map = new HashMap<>();
            map.put("nom", competence.getNameCompetence());
            map.put("niveauActuel", competence.getNiveauActuel());
            map.put("anneesExperience", String.valueOf(competence.getAnneesExperience()));
            map.put("niveauRequis", competenceService.determinerNiveauRequis(competence.getAnneesExperience()));
            map.put("status", competenceService.verifierNiveau(competence.getNiveauActuel(), competence.getAnneesExperience()));
            String niveauRequis = competenceService.determinerNiveauRequis(competence.getAnneesExperience());
            result.add(map);
        }
        return result;
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RH')")

    @GetMapping("/users/competences")
    public ResponseEntity<List<User>> getAllUsersWithCompetences() {
        List<User> users = userService.getAllUsersWithCompetences();
        return ResponseEntity.ok(users);
    }
}


