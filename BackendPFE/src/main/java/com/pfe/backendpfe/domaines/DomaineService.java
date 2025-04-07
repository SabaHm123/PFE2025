package com.pfe.backendpfe.domaines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DomaineService {

    @Autowired
    private DomaineRepository domaineRepository;
    @Autowired
    private SousDomaineRepository sousDomaineRepository;

    public Domaine createDomaine(Domaine domaine) {
        return domaineRepository.save(domaine);
    }

    public List<Domaine> getAllDomaines() {
        return domaineRepository.findAll();
    }

    public Domaine getDomaineById(Long id) {
        return domaineRepository.findById(id).orElse(null);
    }

    public void deleteDomaine(Long id) {
        domaineRepository.deleteById(id);
    }
    public Domaine updateDomaine(Long id, Domaine updatedDomaine) {
        Domaine existingDomaine = domaineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domaine non trouvé avec l'ID : " + id));

        existingDomaine.setNom(updatedDomaine.getNom());

        return domaineRepository.save(existingDomaine);
    }
    // Ajouter un sous-domaine à un domaine existant
    public Domaine addSousDomaineToDomaine(Long domaineId, SousDomaine sousDomaine) {
        Domaine domaine = domaineRepository.findById(domaineId)
                .orElseThrow(() -> new RuntimeException("Domaine non trouvé avec l'ID : " + domaineId));

        sousDomaine.setDomaine(domaine);
        sousDomaineRepository.save(sousDomaine);

        domaine.getSousDomaines().add(sousDomaine);
        return domaineRepository.save(domaine);
    }

}
