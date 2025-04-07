package com.pfe.backendpfe.user;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pfe.backendpfe.activity.Activite;
import com.pfe.backendpfe.competence.Competence;
import com.pfe.backendpfe.role.Role;
import com.pfe.backendpfe.role.RoleName;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
@Entity
@Table(name = "_user")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails, Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //private String firstname;
    //private String lastname;
    private String username;
    private LocalDate birthdate;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    private String password;
    @Column(nullable = false)
    private Boolean accoutLocked = false;
    private Boolean enabled;
    private String confirmationToken;
    private RoleName role;
    private boolean isApproved = false; // Statut d'approbation
    private boolean pendingApproval; // Ajout de ce champ
    @Enumerated(EnumType.STRING) // Stocke le grade sous forme de texte dans la base
    private Grade grade;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime LastModifiedDate;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activite_id")
    private Activite activite;
    public User( String username, String email, String password, LocalDate birthdate, Boolean enabled, Boolean accoutLocked) {
        this.username = username;
       // this.firstname = firstname;
      //  this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.birthdate = birthdate;
        this.enabled = enabled;
        this.accoutLocked = accoutLocked;
    }
   public boolean isPendingApproval() {
        return pendingApproval;
    }

    public void setPendingApproval(boolean pendingApproval) {
        this.pendingApproval = pendingApproval;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role != null ? role.getAuthorities() : Collections.emptyList();
    }


  /*  @Override
    public String getName() {
        return email;
    }*/
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accoutLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    public String FullName(){
        return username;
    }
    public String getEmail() {
        return email;
    }

    public void setUsername(String admin) {
    }

    @Override
    public String getName() {
        return username;
    }

    @OneToMany(mappedBy = "user")
    private List<Competence> competences;
}
