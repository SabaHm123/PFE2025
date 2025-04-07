package com.pfe.backendpfe.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Long activiteId; // Ajouter l'ID de l'activité
    private String activiteName;  // Ajoutez ce champ pour stocker le nom de l'activité

    // Getters et setters
}
