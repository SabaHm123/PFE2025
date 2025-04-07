package com.pfe.backendpfe.user;

import com.pfe.backendpfe.role.Role;
import com.pfe.backendpfe.role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByConfirmationToken(String confirmationToken);

    List<User> findByRole(RoleName role);
    @Query("SELECT u FROM User u JOIN FETCH u.competences")


    List<User> findAllWithCompetences();


}
