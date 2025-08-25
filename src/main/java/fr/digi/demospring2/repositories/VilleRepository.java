package fr.digi.demospring2.repositories;

import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.entities.Ville;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VilleRepository extends CrudRepository<Ville, Integer>, PagingAndSortingRepository<Ville, Integer> {

    // ========== MÉTHODES AVEC JOIN FETCH POUR ÉVITER LazyInitializationException ==========

    /**
     * Recherche d'une ville par ID avec chargement EAGER du département
     * @param id ID de la ville
     * @return Optional<Ville> avec département chargé
     */
    @Query("SELECT v FROM Ville v JOIN FETCH v.departement WHERE v.id = :id")
    Optional<Ville> findByIdWithDepartement(@Param("id") Integer id);

    /**
     * Recherche d'une ville par nom avec chargement EAGER du département
     * @param nom nom de la ville
     * @return Ville avec département chargé
     */
    @Query("SELECT v FROM Ville v JOIN FETCH v.departement WHERE v.nom = :nom")
    Ville findByNomWithDepartement(@Param("nom") String nom);

    /**
     * Recherche paginée de toutes les villes avec chargement EAGER du département
     * @param pageable paramètres de pagination
     * @return Page<Ville> avec les départements chargés
     */
    @Query("SELECT v FROM Ville v JOIN FETCH v.departement")
    Page<Ville> findAllWithDepartement(Pageable pageable);

    /**
     * Recherche des villes dont le nom commence par un préfixe avec département
     * @param prefix préfixe du nom
     * @return List<Ville> avec départements chargés
     */
    @Query("SELECT v FROM Ville v JOIN FETCH v.departement WHERE UPPER(v.nom) LIKE UPPER(CONCAT(:prefix, '%'))")
    List<Ville> findByNomStartingWithIgnoreCaseWithDepartement(@Param("prefix") String prefix);

    /**
     * Recherche des villes par population minimale avec département
     * @param min population minimale
     * @return List<Ville> avec départements chargés
     */
    @Query("SELECT v FROM Ville v JOIN FETCH v.departement WHERE v.nbHabitants > :min ORDER BY v.nbHabitants DESC")
    List<Ville> findByNbHabitantsGreaterThanOrderByNbHabitantsDescWithDepartement(@Param("min") int min);

    /**
     * Recherche des villes par population entre min et max avec département
     * @param min population minimale
     * @param max population maximale
     * @return List<Ville> avec départements chargés
     */
    @Query("SELECT v FROM Ville v JOIN FETCH v.departement WHERE v.nbHabitants BETWEEN :min AND :max ORDER BY v.nbHabitants DESC")
    List<Ville> findByNbHabitantsBetweenOrderByNbHabitantsDescWithDepartement(@Param("min") int min, @Param("max") int max);

    /**
     * Recherche des villes d'un département par population minimale avec département
     * @param departementId ID du département
     * @param min population minimale
     * @return List<Ville> avec départements chargés
     */
    @Query("SELECT v FROM Ville v JOIN FETCH v.departement WHERE v.departement.id = :departementId AND v.nbHabitants > :min ORDER BY v.nbHabitants DESC")
    List<Ville> findByDepartementIdAndNbHabitantsGreaterThanOrderByNbHabitantsDescWithDepartement(@Param("departementId") Integer departementId, @Param("min") int min);

    /**
     * Recherche des villes d'un département par population entre min et max avec département
     * @param departementId ID du département
     * @param min population minimale
     * @param max population maximale
     * @return List<Ville> avec départements chargés
     */
    @Query("SELECT v FROM Ville v JOIN FETCH v.departement WHERE v.departement.id = :departementId AND v.nbHabitants BETWEEN :min AND :max ORDER BY v.nbHabitants DESC")
    List<Ville> findByDepartementIdAndNbHabitantsBetweenOrderByNbHabitantsDescWithDepartement(@Param("departementId") Integer departementId, @Param("min") int min, @Param("max") int max);

    /**
     * Recherche des villes d'un département triées par population avec département
     * @param departementId ID du département
     * @return List<Ville> avec départements chargés
     */
    @Query("SELECT v FROM Ville v JOIN FETCH v.departement WHERE v.departement.id = :departementId ORDER BY v.nbHabitants DESC")
    List<Ville> findByDepartementIdOrderByNbHabitantsDescWithDepartement(@Param("departementId") Integer departementId);

    /**
     * Recherche des villes par département avec département
     * @param departementId ID du département
     * @return List<Ville> avec départements chargés
     */
    @Query("SELECT v FROM Ville v JOIN FETCH v.departement WHERE v.departement.id = :departementId")
    List<Ville> findByDepartementIdWithDepartement(@Param("departementId") Integer departementId);

    /**
     * Recherche des villes par code de département avec département
     * @param code code du département
     * @return List<Ville> avec départements chargés
     */
    @Query("SELECT v FROM Ville v JOIN FETCH v.departement WHERE v.departement.code = :code")
    List<Ville> findByDepartementCodeWithDepartement(@Param("code") String code);

    // ========== MÉTHODES HÉRITÉES (PEUVENT CAUSER LazyInitializationException) ==========

    Ville findByNom(String nom);
    List<Ville> findByDepartement(Departement departement);
    List<Ville> findByDepartementId(Integer departementId);
    List<Ville> findByDepartementCode(String code);
    List<Ville> findByDepartementNom(String nom);

    List<Ville> findByNomStartingWithIgnoreCase(String prefix);

    List<Ville> findByNbHabitantsGreaterThanOrderByNbHabitantsDesc(int min);

    List<Ville> findByNbHabitantsBetweenOrderByNbHabitantsDesc(int min, int max);

    List<Ville> findByDepartementIdAndNbHabitantsGreaterThanOrderByNbHabitantsDesc(Integer departementId, int min);

    List<Ville> findByDepartementIdAndNbHabitantsBetweenOrderByNbHabitantsDesc(Integer departementId, int min, int max);

    List<Ville> findByDepartementIdOrderByNbHabitantsDesc(Integer departementId);

    @Query("SELECT v FROM Ville v WHERE v.departement.id = :departementId ORDER BY v.nbHabitants DESC")
    List<Ville> findTopNVillesByDepartementOrderByNbHabitantsDesc(@Param("departementId") Integer departementId, Pageable pageable);

    Page<Ville> findAll(Pageable pageable);
}