package com.pfe.backendpfe.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TokenrRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);
}
