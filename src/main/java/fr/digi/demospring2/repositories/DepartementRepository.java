package fr.digi.demospring2.repositories;

import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.entities.Ville;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DepartementRepository extends CrudRepository<Departement, Integer> {

    /**
     * Recherche d'un département par son code
     * @param code le code du département
     * @return Optional<Departement>
     */
    Optional<Departement> findByCode(String code);

    /**
     * Recherche d'un département par son nom
     * @param nom le nom du département
     * @return Optional<Departement>
     */
    Optional<Departement> findByNom(String nom);

    /**
     * Recherche de tous les départements avec leurs villes chargées
     * @return List<Departement> avec villes chargées
     */
    @Query("SELECT DISTINCT d FROM Departement d LEFT JOIN FETCH d.villes")
    List<Departement> findAllWithVilles();

    /**
     * Recherche d'un département par ID avec ses villes chargées
     * @param id l'ID du département
     * @return Optional<Departement> avec villes chargées
     */
    @Query("SELECT d FROM Departement d LEFT JOIN FETCH d.villes WHERE d.id = :id")
    Optional<Departement> findByIdWithVilles(@Param("id") Integer id);

    /**
     * Recherche d'un département par code avec ses villes chargées
     * @param code le code du département
     * @return Optional<Departement> avec villes chargées
     */
    @Query("SELECT d FROM Departement d LEFT JOIN FETCH d.villes WHERE d.code = :code")
    Optional<Departement> findByCodeWithVilles(@Param("code") String code);

    /**
     * Recherche des villes d'un département triées par nombre d'habitants descendant
     * @param departementId l'ID du département
     * @return List<Ville> avec département chargé
     */
    @Query("SELECT v FROM Ville v JOIN FETCH v.departement WHERE v.departement.id = :departementId ORDER BY v.nbHabitants DESC")
    List<Ville> findTopVillesByDepartementId(@Param("departementId") Integer departementId);

    /**
     * Recherche des villes d'un département avec population entre min et max
     * @param departementId l'ID du département
     * @param min population minimale
     * @param max population maximale
     * @return List<Ville> avec département chargé
     */
    @Query("SELECT v FROM Ville v JOIN FETCH v.departement WHERE v.departement.id = :departementId AND v.nbHabitants BETWEEN :min AND :max ORDER BY v.nbHabitants DESC")
    List<Ville> findVillesByDepartementIdAndPopulationBetween(
            @Param("departementId") Integer departementId,
            @Param("min") int min,
            @Param("max") int max);
}