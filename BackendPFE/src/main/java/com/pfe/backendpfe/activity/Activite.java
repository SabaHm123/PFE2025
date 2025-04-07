package com.pfe.backendpfe.activity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pfe.backendpfe.user.User;
import jakarta.persistence.*;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)  // Activation de l'audit
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)  // Précision du format
    private Date createdDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "activite", cascade = CascadeType.ALL)
    private List<User> users;  // Liste des utilisateurs liés à cette activité
}
