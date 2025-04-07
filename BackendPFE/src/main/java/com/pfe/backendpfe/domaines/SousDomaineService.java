package com.pfe.backendpfe.domaines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SousDomaineService {

    @Autowired
    private SousDomaineRepository sousDomaineRepository;

    @Autowired
    private DomaineRepository domaineRepository;

    public SousDomaine createSousDomaine(SousDomaine sousDomaine, Long domaineId) {
        Domaine domaine = domaineRepository.findById(domaineId).orElse(null);
        if (domaine != null) {
            sousDomaine.setDomaine(domaine);
            return sousDomaineRepository.save(sousDomaine);
        }
        return null;
    }


    public List<SousDomaine> getSousDomainesByDomaine(Long domaineId) {
        Domaine domaine = domaineRepository.findById(domaineId).orElse(null);
        if (domaine != null) {
            return sousDomaineRepository.findByDomaine(domaine);
        }
        return Collections.emptyList();
    }

    public List<SousDomaine> getAllSousDomaines() {
        return sousDomaineRepository.findAll();
    }
    public SousDomaine updateSousDomaine(Long id, SousDomaine newSousDomaine) {
        SousDomaine existingSousDomaine = sousDomaineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sous-domaine non trouvé avec l'ID : " + id));

        existingSousDomaine.setNom(newSousDomaine.getNom());

        return sousDomaineRepository.save(existingSousDomaine);
    }
    public SousDomaine updateSousDomaineByDomaine(Long id, Long domaineId, SousDomaine updatedSousDomaine) {
        SousDomaine existingSousDomaine = sousDomaineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sous-domaine non trouvé avec l'ID : " + id));

        Domaine domaine = domaineRepository.findById(domaineId)
                .orElseThrow(() -> new RuntimeException("Domaine non trouvé avec l'ID : " + domaineId));

        existingSousDomaine.setNom(updatedSousDomaine.getNom());
        existingSousDomaine.setDomaine(domaine); // Mise à jour du domaine associé

        return sousDomaineRepository.save(existingSousDomaine);
    }
    @Autowired
    private SousDomaineRepository subDomainRepository;

    public List<SousDomaine> getAllSubDomains() {
        return subDomainRepository.findAll();
    }

    public List<SousDomaine> getSubDomainsByCategory(Long categoryId) {
        return subDomainRepository.findAll().stream()
                .filter(sub -> sub.getCategory() != null && sub.getCategory().getId().equals(categoryId))
                .collect(Collectors.toList());
    }

    public SousDomaine saveSubDomain(SousDomaine subDomain) {
        return subDomainRepository.save(subDomain);
    }
    public void deleteSubDomain(Long subDomainId) {
        sousDomaineRepository.deleteById(subDomainId);
    }


}
