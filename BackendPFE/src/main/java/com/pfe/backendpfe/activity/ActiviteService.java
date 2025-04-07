package com.pfe.backendpfe.activity;

import com.pfe.backendpfe.competence.Competence;
import com.pfe.backendpfe.competence.CompetenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ActiviteService {

    private final ActiviteRepository activiteRepository;

    public ActiviteService(ActiviteRepository activiteRepository) {
        this.activiteRepository = activiteRepository;
    }

    public List<Activite> getAllActivity() {
        return activiteRepository.findAll();
    }

    public Activite getActivityById(Long id) {
        return activiteRepository.findById(id).orElse(null);
    }

    public Activite saveActivity(Activite activite) {
        return activiteRepository.save(activite);
    }

    public void deleteActivite(Long id) {
        activiteRepository.deleteById(id);
    }
    public Activite updateActivity(Long id, Activite activite) {
        if (activiteRepository.existsById(id)) {
            activite.setId(id);
            return activiteRepository.save(activite);
        }
        return null;
    }

}

