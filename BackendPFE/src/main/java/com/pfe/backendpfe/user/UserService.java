package com.pfe.backendpfe.user;

import com.pfe.backendpfe.role.Role;
import com.pfe.backendpfe.role.RoleName;
import com.pfe.backendpfe.role.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service

public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;


    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
    @Transactional


    public void assignRoleToUser(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Role role = roleRepository.findByName(RoleName.valueOf(roleName))
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));

        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }

        // Empêcher l'ajout multiple de ROLE_ADMIN et ROLE_RH
        if ((role.getName() == RoleName.ROLE_ADMIN || role.getName() == RoleName.ROLE_RH)
                && user.getRoles().stream().anyMatch(r -> r.getName() == role.getName())) {
            throw new RuntimeException("L'utilisateur possède déjà le rôle " + roleName);
        }

        user.getRoles().add(role);
        saveUser(user);    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsersWithCompetences() {
        return userRepository.findAllWithCompetences();
    }

}
