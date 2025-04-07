package com.pfe.backendpfe.competence;

import com.pfe.backendpfe.user.User;
import com.pfe.backendpfe.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/competences")
public class CompetenceController {
    @Autowired
    private CompetenceService competenceService;
    @Autowired
    private CompetenceRepository competenceRepository;
    @Autowired
    private UserRepository userRepository;
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

    @GetMapping
    public List<Competence> getAllCompetences() {
        return competenceService.getAllCompetences();
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public Competence getCompetenceById(@PathVariable Long id) {
        return competenceService.getCompetenceById(id);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/add-competence")
    public ResponseEntity<?> addCompetence(@RequestBody Map<String, Object> payload) {
        System.out.println("Données reçues : " + payload);

        // Vérifier si l'userId est présent
        if (!payload.containsKey("userId") || payload.get("userId") == null) {
            return ResponseEntity.badRequest().body("User ID cannot be null");
        }

        Long userId = Long.valueOf(payload.get("userId").toString());

        // Vérifier si l'utilisateur existe en base
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Création de l'objet Competence
        Competence competence = new Competence();
        competence.setNameCompetence((String) payload.get("nameCompetence"));
        competence.setCategorie((String) payload.get("categorie"));
        competence.setDescription((String) payload.get("description"));
        competence.setTechnologies((String) payload.get("Technologies"));
        competence.setNiveau((String) payload.get("niveau"));
        competence.setDateAcquired(LocalDate.now()); // Exemple de date
        competence.setUser(user); // Associer l'utilisateur trouvé

        // Sauvegarde en base
        Competence savedCompetence = competenceRepository.save(competence);
        return ResponseEntity.ok(savedCompetence);
    }


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public Competence updateCompetence(@PathVariable Long id, @RequestBody Competence competence) {
        System.out.println("Competence received: " + competence);
        competence.setIdComptence(id);
        return competenceService.saveCompetence(competence);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteCompetence(@PathVariable Long id) {
        competenceService.deleteCompetence(id);
    }
}
