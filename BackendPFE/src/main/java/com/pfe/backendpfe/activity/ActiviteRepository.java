package com.pfe.backendpfe.activity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActiviteRepository extends JpaRepository<Activite, Long> {
}
