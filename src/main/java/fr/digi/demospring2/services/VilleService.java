package fr.digi.demospring2.services;

import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.entities.Ville;
import fr.digi.demospring2.exceptions.FunctionalException;
import fr.digi.demospring2.repositories.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VilleService {

    @Autowired
    private VilleRepository villeRepository;

    @Autowired
    private DepartementService dptService;

    /**
     * Méthode qui retourne la liste de toutes les villes
     * @return List<Ville>
     */
    public List<Ville> extractVilles() {
        return (List<Ville>) villeRepository.findAll();
    }

    /**
     * Méthode qui retourne la liste paginée des villes avec les départements chargés
     * Cette version résout le problème de LazyInitializationException
     *
     * @param pageable paramètres de pagination
     * @return Page<Ville> avec les départements chargés
     */
    @Transactional(readOnly = true)
    public Page<Ville> extractVillesPaginated(Pageable pageable) {
        return villeRepository.findAllWithDepartement(pageable);
    }

    /**
     * Méthode qui retourne une ville par son id avec département chargé
     * @param idVille id de la ville
     * @return Ville
     */
    @Transactional(readOnly = true)
    public Ville extractVille(int idVille) {
        Optional<Ville> ville = villeRepository.findByIdWithDepartement(idVille);
        return ville.orElse(null);
    }

    /**
     * Méthode qui retourne une ville par son nom avec département chargé
     * @param nom nom de la ville
     * @return Ville
     */
    @Transactional(readOnly = true)
    public Ville extractVille(String nom) {
        return villeRepository.findByNomWithDepartement(nom);
    }

    /**
     * Valide les données d'une ville selon les règles métier
     * @param ville la ville à valider
     * @throws FunctionalException si la validation échoue
     */
    private void validateVille(Ville ville) throws FunctionalException {
        if (ville.getNbHabitants() < 10) {
            throw new FunctionalException("La ville doit avoir au moins 10 habitants");
        }

        if (ville.getNom() == null || ville.getNom().trim().length() < 2) {
            throw new FunctionalException("La ville doit avoir un nom contenant au moins 2 lettres");
        }

        if (ville.getDepartement() == null || ville.getDepartement().getCode() == null) {
            throw new FunctionalException("Le département est obligatoire");
        }

        if (ville.getDepartement().getCode().length() != 2) {
            throw new FunctionalException("Le code département doit obligatoirement faire 2 caractères");
        }

        Ville villeExistante = villeRepository.findByNomAndDepartementId(ville.getNom(), ville.getDepartement().getId());
        if (villeExistante != null && (ville.getId() == null || !villeExistante.getId().equals(ville.getId()))) {
            throw new FunctionalException("Le nom de la ville doit être unique pour un département donné");
        }
    }

    /**
     * Méthode qui ajoute une ville avec validation
     * @param ville ville à ajouter
     * @return Ville la ville sauvegardée
     * @throws FunctionalException si la validation échoue
     */
    @Transactional
    public Ville insertVille(Ville ville) throws FunctionalException {
        if (ville.getDepartement() != null && ville.getDepartement().getId() != null) {
            Departement dpt = dptService.extractDepartement(ville.getDepartement().getId());
            if(dpt == null) {
                throw new FunctionalException("Le département avec l'id " + ville.getDepartement().getId() + " n'existe pas");
            }
            ville.setDepartement(dpt);
        } else {
            throw new FunctionalException("Une ville doit obligatoirement être associée à un département");
        }

        // Validation métier
        validateVille(ville);

        return villeRepository.save(ville);
    }

    /**
     * Méthode qui modifie une ville avec validation
     * @param ville ville à modifier
     * @return Ville la ville modifiée
     * @throws FunctionalException si la validation échoue
     */
    @Transactional
    public Ville modifierVille(Ville ville) throws FunctionalException {
        // Validation métier
        validateVille(ville);

        return villeRepository.save(ville);
    }

    /**
     * Méthode qui supprime une ville par son id
     * @param idVille id de la ville à supprimer
     * @return boolean true si suppression réussie
     */
    @Transactional
    public boolean supprimerVille(int idVille) {
        if (villeRepository.existsById(idVille)) {
            villeRepository.deleteById(idVille);
            return true;
        }
        return false;
    }

    /**
     * Recherche des villes dont le nom commence par un préfixe
     * @param prefix préfixe du nom
     * @return List<Ville>
     * @throws FunctionalException si aucune ville n'est trouvée
     */
    @Transactional(readOnly = true)
    public List<Ville> findVillesStartingWith(String prefix) throws FunctionalException {
        List<Ville> villes = villeRepository.findByNomStartingWithIgnoreCaseWithDepartement(prefix);
        if (villes.isEmpty()) {
            throw new FunctionalException("Aucune ville dont le nom commence par " + prefix + " n'a été trouvée");
        }
        return villes;
    }

    /**
     * Recherche des villes avec population supérieure à min
     * @param min population minimale
     * @return List<Ville>
     * @throws FunctionalException si aucune ville n'est trouvée
     */
    @Transactional(readOnly = true)
    public List<Ville> findVillesPopulationMin(int min) throws FunctionalException {
        List<Ville> villes = villeRepository.findByNbHabitantsGreaterThanOrderByNbHabitantsDescWithDepartement(min);
        if (villes.isEmpty()) {
            throw new FunctionalException("Aucune ville n'a une population supérieure à " + min);
        }
        return villes;
    }

    /**
     * Recherche des villes avec population entre min et max
     * @param min population minimale
     * @param max population maximale
     * @return List<Ville>
     * @throws FunctionalException si aucune ville n'est trouvée
     */
    @Transactional(readOnly = true)
    public List<Ville> findVillesPopulationBetween(int min, int max) throws FunctionalException {
        List<Ville> villes = villeRepository.findByNbHabitantsBetweenOrderByNbHabitantsDescWithDepartement(min, max);
        if (villes.isEmpty()) {
            throw new FunctionalException("Aucune ville n'a une population comprise entre " + min + " et " + max);
        }
        return villes;
    }

    /**
     * Recherche des villes d'un département avec population supérieure à min
     * @param departementId id du département
     * @param min population minimale
     * @return List<Ville>
     * @throws FunctionalException si aucune ville n'est trouvée
     */
    @Transactional(readOnly = true)
    public List<Ville> findVillesDepartementPopulationMin(Integer departementId, int min) throws FunctionalException {
        Departement departement = dptService.extractDepartement(departementId);
        if (departement == null) {
            throw new FunctionalException("Le département avec l'id " + departementId + " n'existe pas");
        }

        List<Ville> villes = villeRepository.findByDepartementIdAndNbHabitantsGreaterThanOrderByNbHabitantsDescWithDepartement(departementId, min);
        if (villes.isEmpty()) {
            throw new FunctionalException("Aucune ville n'a une population supérieure à " + min + " dans le département " + departement.getCode());
        }
        return villes;
    }

    /**
     * Recherche des villes d'un département avec population entre min et max
     * @param departementId id du département
     * @param min population minimale
     * @param max population maximale
     * @return List<Ville>
     * @throws FunctionalException si aucune ville n'est trouvée
     */
    @Transactional(readOnly = true)
    public List<Ville> findVillesDepartementPopulationBetween(Integer departementId, int min, int max) throws FunctionalException {
        Departement departement = dptService.extractDepartement(departementId);
        if (departement == null) {
            throw new FunctionalException("Le département avec l'id " + departementId + " n'existe pas");
        }

        List<Ville> villes = villeRepository.findByDepartementIdAndNbHabitantsBetweenOrderByNbHabitantsDescWithDepartement(departementId, min, max);
        if (villes.isEmpty()) {
            throw new FunctionalException("Aucune ville n'a une population comprise entre " + min + " et " + max + " dans le département " + departement.getCode());
        }
        return villes;
    }

    /**
     * Recherche des n villes les plus peuplées d'un département
     * @param departementId id du département
     * @param n nombre de villes à retourner
     * @return List<Ville>
     * @throws FunctionalException si aucune ville n'est trouvée
     */
    @Transactional(readOnly = true)
    public List<Ville> findTopNVillesDepartement(Integer departementId, int n) throws FunctionalException {
        Departement departement = dptService.extractDepartement(departementId);
        if (departement == null) {
            throw new FunctionalException("Le département avec l'id " + departementId + " n'existe pas");
        }

        List<Ville> toutes = villeRepository.findByDepartementIdOrderByNbHabitantsDescWithDepartement(departementId);
        List<Ville> result = toutes.stream().limit(n).toList();

        if (result.isEmpty()) {
            throw new FunctionalException("Aucune ville trouvée dans le département " + departement.getCode());
        }

        return result;
    }

    /**
     * Recherche des villes par département
     * @param departementId id du département
     * @return List<Ville>
     */
    @Transactional(readOnly = true)
    public List<Ville> findVillesByDepartement(Integer departementId) {
        return villeRepository.findByDepartementIdWithDepartement(departementId);
    }

    /**
     * Recherche des villes par code de département
     * @param codeDepartement code du département
     * @return List<Ville>
     */
    @Transactional(readOnly = true)
    public List<Ville> findVillesByDepartementCode(String codeDepartement) {
        return villeRepository.findByDepartementCodeWithDepartement(codeDepartement);
    }
}