package com.pfe.backendpfe.domaines;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SousDomaineRepository extends JpaRepository<SousDomaine, Long> {
    List<SousDomaine> findByDomaine(Domaine domaine);
    List<SousDomaine> findByCategory(Category categorie);

}
