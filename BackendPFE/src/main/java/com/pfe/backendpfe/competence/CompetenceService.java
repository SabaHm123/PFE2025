package com.pfe.backendpfe.competence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CompetenceService {
    @Autowired
    private CompetenceRepository competenceRepository;

    public List<Competence> getAllCompetences() {
        return competenceRepository.findAll();
    }

    public Competence getCompetenceById(Long id) {
        return competenceRepository.findById(id).orElse(null);
    }

    public Competence saveCompetence(Competence competence) {
        return competenceRepository.save(competence);
    }

    public void deleteCompetence(Long id) {
        competenceRepository.deleteById(id);
    }

    private static final Map<String, Integer> niveaux = Map.of(
            "Basic", 1,
            "Competent", 2,
            "Advanced", 3,
            "Expert", 4
    );

    public String verifierNiveau(String niveauActuel, int anneesExperience) {
        // Déterminer le niveau requis en fonction des années d'expérience
        String niveauRequis = determinerNiveauRequis(anneesExperience);

        // Vérifier si le niveau actuel et le niveau requis existent dans la carte
        Integer indexActuel = niveaux.get(niveauActuel); // Renvoie null si le niveauActuel n'est pas trouvé
        Integer indexRequis = niveaux.get(niveauRequis); // Renvoie null si le niveauRequis n'est pas trouvé

        // Si l'un des niveaux n'est pas trouvé, retourner "NOK"
        if (indexActuel == null || indexRequis == null) {
            return "NOK";
        }

        // Comparer les niveaux actuels et requis en termes d'index
        return (indexActuel >= indexRequis) ? "OK" : "NOK";
    }

    public String determinerNiveauRequis(int anneesExperience) {
        // Déterminer le niveau requis en fonction des années d'expérience
        if (anneesExperience <= 1) return "Basic";
        if (anneesExperience <= 3) return "Competent";
        if (anneesExperience <= 5) return "Advanced";
        return "Expert";
    }
}
