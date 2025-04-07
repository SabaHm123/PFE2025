package com.pfe.backendpfe.competence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pfe.backendpfe.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Competence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComptence;
    private String nameCompetence;
    private  String categorie;
    private String description;
    private  String Technologies;
    private  String niveau;
    private LocalDate dateAcquired;
    private String niveauActuel;
    private Integer anneesExperience;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("competences") // Empêche la boucle infinie lors du retour JSON
    private User user;

}
