package fr.digi.demospring2.services;

import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.entities.Ville;
import fr.digi.demospring2.exceptions.FunctionalException;
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
     * Valide les données d'un département selon les règles métier
     * @param departement le département à valider
     * @throws FunctionalException si la validation échoue
     */
    private void validateDepartement(Departement departement) throws FunctionalException {
        // Vérifier que le nom est obligatoire et comporte au moins 3 lettres
        if (departement.getNom() == null || departement.getNom().trim().length() < 3) {
            throw new FunctionalException("Le nom du département est obligatoire et comporte au moins 3 lettres");
        }

        // Vérifier l'unicité du nom du département
        Optional<Departement> dptExistant = departementRepository.findByNom(departement.getNom());
        if (dptExistant.isPresent() && !dptExistant.get().getId().equals(departement.getId())) {
            throw new FunctionalException("Le nom du département est unique");
        }
    }

    /**
     * Méthode qui insère un département et retourne la liste des départements
     * @param dpt - département à insérer
     * @return List<Departement>
     * @throws FunctionalException si la validation échoue
     */
    public List<Departement> insertDepartement(Departement dpt) throws FunctionalException {
        // Validation métier
        validateDepartement(dpt);

        departementRepository.save(dpt);
        return extractDepartements();
    }

    /**
     * Méthode qui modifie un département et retourne la liste des départements
     * @param idDpt - id du département à modifier
     * @param dptEdited - département modifié
     * @return List<Departement>
     * @throws FunctionalException si la validation échoue
     */
    public List<Departement> modifierDepartement(int idDpt, Departement dptEdited) throws FunctionalException {
        Optional<Departement> dptOptional = departementRepository.findById(idDpt);
        if (dptOptional.isPresent()) {
            Departement dptExistant = dptOptional.get();
            dptExistant.setCode(dptEdited.getCode());
            dptExistant.setNom(dptEdited.getNom());

            // Validation métier
            validateDepartement(dptExistant);

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
     * @throws FunctionalException si le département n'existe pas
     */
    public List<Ville> getNPlusGrandesVilles(int idDpt, int n) throws FunctionalException {
        Departement departement = extractDepartement(idDpt);
        if (departement == null) {
            throw new FunctionalException("Le département avec l'id " + idDpt + " n'existe pas");
        }

        List<Ville> toutes = departementRepository.findTopVillesByDepartementId(idDpt);
        List<Ville> result = toutes.stream().limit(n).toList();

        if (result.isEmpty()) {
            throw new FunctionalException("Aucune ville trouvée dans le département " + departement.getCode());
        }

        return result;
    }

    /**
     * Méthode qui retourne les villes d'un département ayant une population comprise entre deux valeurs
     * @param idDpt - id du département
     * @param min - population minimale
     * @param max - population maximale
     * @return List<Ville>
     * @throws FunctionalException si le département n'existe pas ou aucune ville trouvée
     */
    public List<Ville> getVillesParPopulation(int idDpt, int min, int max) throws FunctionalException {
        Departement departement = extractDepartement(idDpt);
        if (departement == null) {
            throw new FunctionalException("Le département avec l'id " + idDpt + " n'existe pas");
        }

        List<Ville> villes = departementRepository.findVillesByDepartementIdAndPopulationBetween(idDpt, min, max);
        if (villes.isEmpty()) {
            throw new FunctionalException("Aucune ville n'a une population comprise entre " + min + " et " + max + " dans le département " + departement.getCode());
        }

        return villes;
    }
}