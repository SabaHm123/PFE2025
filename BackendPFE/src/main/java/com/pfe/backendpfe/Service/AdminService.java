package com.pfe.backendpfe.Service;

import com.pfe.backendpfe.user.Grade;
import com.pfe.backendpfe.user.User;
import com.pfe.backendpfe.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AdminService {
    @Autowired
    private UserRepository repository;

    public List<User> getAllUsers() {
        return repository.findAll();

    }
    public User getUserById(Long id) {
        return repository.findById(id).get();
    }
    public User addUser(User user) {
        if (user.getGrade() == null) {
            user.setGrade(Grade.FOUNDATION); // Valeur par défaut si aucun grade n'est précisé
        }
        return repository.save(user);
    }
    /*public User updateUser(User user) {
        System.out.println("Hello : " + user);
        return repository.save(user);
    }*/
    public User updateUser(User user) {
        Optional<User> existingUser = repository.findById(Long.valueOf(user.getId()));

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();

            userToUpdate.setRole(user.getRole());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setUsername(user.getUsername());

            if (user.getGrade() != null) {
                userToUpdate.setGrade(user.getGrade());
            }

            return repository.save(userToUpdate);
        } else {
            throw new EntityNotFoundException("Utilisateur non trouvé");
        }
    }
    public void deleteUser(Long id) {
        User user = repository.findById(id).get();
        user.getRoles().forEach(status ->
                status.getUsers().remove(user)
        );

        repository.deleteById(id);
    }
}
