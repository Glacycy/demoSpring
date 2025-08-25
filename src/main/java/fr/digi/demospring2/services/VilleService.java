package fr.digi.demospring2.services;

import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.entities.Ville;
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
     * Méthode qui ajoute une ville
     * @param ville ville à ajouter
     * @return Ville la ville sauvegardée
     */
    @Transactional
    public Ville insertVille(Ville ville) {
        if (ville.getDepartement() != null && ville.getDepartement().getId() != null) {
            Departement dpt = dptService.extractDepartement(ville.getDepartement().getId());
            if(dpt == null) {
                throw new RuntimeException("Le département avec l'id " + ville.getDepartement().getId() + " n'existe pas");
            }
            ville.setDepartement(dpt);
        } else {
            throw new RuntimeException("Une ville doit obligatoirement être associée à un département");
        }

        return villeRepository.save(ville);
    }

    /**
     * Méthode qui modifie une ville
     * @param ville ville à modifier
     * @return Ville la ville modifiée
     */
    @Transactional
    public Ville modifierVille(Ville ville) {
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
     */
    @Transactional(readOnly = true)
    public List<Ville> findVillesStartingWith(String prefix) {
        return villeRepository.findByNomStartingWithIgnoreCaseWithDepartement(prefix);
    }

    /**
     * Recherche des villes avec population supérieure à min
     * @param min population minimale
     * @return List<Ville>
     */
    @Transactional(readOnly = true)
    public List<Ville> findVillesPopulationMin(int min) {
        return villeRepository.findByNbHabitantsGreaterThanOrderByNbHabitantsDescWithDepartement(min);
    }

    /**
     * Recherche des villes avec population entre min et max
     * @param min population minimale
     * @param max population maximale
     * @return List<Ville>
     */
    @Transactional(readOnly = true)
    public List<Ville> findVillesPopulationBetween(int min, int max) {
        return villeRepository.findByNbHabitantsBetweenOrderByNbHabitantsDescWithDepartement(min, max);
    }

    /**
     * Recherche des villes d'un département avec population supérieure à min
     * @param departementId id du département
     * @param min population minimale
     * @return List<Ville>
     */
    @Transactional(readOnly = true)
    public List<Ville> findVillesDepartementPopulationMin(Integer departementId, int min) {
        return villeRepository.findByDepartementIdAndNbHabitantsGreaterThanOrderByNbHabitantsDescWithDepartement(departementId, min);
    }

    /**
     * Recherche des villes d'un département avec population entre min et max
     * @param departementId id du département
     * @param min population minimale
     * @param max population maximale
     * @return List<Ville>
     */
    @Transactional(readOnly = true)
    public List<Ville> findVillesDepartementPopulationBetween(Integer departementId, int min, int max) {
        return villeRepository.findByDepartementIdAndNbHabitantsBetweenOrderByNbHabitantsDescWithDepartement(departementId, min, max);
    }

    /**
     * Recherche des n villes les plus peuplées d'un département
     * @param departementId id du département
     * @param n nombre de villes à retourner
     * @return List<Ville>
     */
    @Transactional(readOnly = true)
    public List<Ville> findTopNVillesDepartement(Integer departementId, int n) {
        List<Ville> toutes = villeRepository.findByDepartementIdOrderByNbHabitantsDescWithDepartement(departementId);
        return toutes.stream().limit(n).toList();
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