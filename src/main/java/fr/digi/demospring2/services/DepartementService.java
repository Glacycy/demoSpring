package fr.digi.demospring2.services;

import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.entities.Ville;
import fr.digi.demospring2.repositories.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartementService {

    @Autowired
    private DepartementRepository departementRepository;

    /**
     * Méthode qui retourne la liste des départements avec leurs villes
     * @return List<Departement>
     */
    public List<Departement> extractDepartements() {
        return departementRepository.findAllWithVilles();
    }

    /**
     * Méthode qui retourne un département par son Id
     * @param idDpt - id du département
     * @return Departement
     */
    public Departement extractDepartement(int idDpt) {
        Optional<Departement> departement = departementRepository.findByIdWithVilles(idDpt);
        return departement.orElse(null);
    }

    /**
     * Méthode qui retourne un département par son code
     * @param codeDpt - code du département
     * @return Departement
     */
    public Departement extractDepartementByCode(String codeDpt) {
        Optional<Departement> departement = departementRepository.findByCodeWithVilles(codeDpt);
        return departement.orElse(null);
    }

    /**
     * Méthode qui insère un département et retourne la liste des départements
     * @param dpt - département à insérer
     * @return List<Departement>
     */
    public List<Departement> insertDepartement(Departement dpt) {
        departementRepository.save(dpt);
        return extractDepartements();
    }

    /**
     * Méthode qui modifie un département et retourne la liste des départements
     * @param idDpt - id du département à modifier
     * @param dptEdited - département modifié
     * @return List<Departement>
     */
    public List<Departement> modifierDepartement(int idDpt, Departement dptEdited) {
        Optional<Departement> dptOptional = departementRepository.findById(idDpt);
        if (dptOptional.isPresent()) {
            Departement dptExistant = dptOptional.get();
            dptExistant.setCode(dptEdited.getCode());
            dptExistant.setNom(dptEdited.getNom());
            departementRepository.save(dptExistant);
        }
        return extractDepartements();
    }

    /**
     * Méthode qui supprime un département et retourne la liste des départements
     * @param idDpt - id du département à supprimer
     * @return List<Departement>
     */
    public List<Departement> supprimerDepartement(int idDpt) {
        if (departementRepository.existsById(idDpt)) {
            departementRepository.deleteById(idDpt);
        }
        return extractDepartements();
    }

    /**
     * Méthode qui retourne les n plus grandes villes d'un département
     * @param idDpt - id du département
     * @param n - nombre de villes à retourner
     * @return List<Ville>
     */
    public List<Ville> getNPlusGrandesVilles(int idDpt, int n) {
        List<Ville> toutes = departementRepository.findTopVillesByDepartementId(idDpt);
        return toutes.stream().limit(n).toList();
    }

    /**
     * Méthode qui retourne les villes d'un département ayant une population comprise entre deux valeurs
     * @param idDpt - id du département
     * @param min - population minimale
     * @param max - population maximale
     * @return List<Ville>
     */
    public List<Ville> getVillesParPopulation(int idDpt, int min, int max) {
        return departementRepository.findVillesByDepartementIdAndPopulationBetween(idDpt, min, max);
    }
}