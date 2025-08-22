package fr.digi.demospring2.dao;

import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.entities.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class DepartementDao {

    @PersistenceContext
    private EntityManager em;

    /**
     * Méthode qui retourne la liste des départements avec leurs villes
     * @return List<Departement>
     */
    public List<Departement> extractDepartements() {
        TypedQuery<Departement> query = em.createQuery(
                "SELECT DISTINCT d FROM Departement d LEFT JOIN FETCH d.villes", Departement.class);
        return query.getResultList();
    }

    /**
     * Méthode qui retourne un département par son Id
     * @param idDpt - id du département
     * @return Departement
     */
    public Departement extractDepartement(int idDpt) {
        TypedQuery<Departement> query = em.createQuery(
                "SELECT d FROM Departement d LEFT JOIN FETCH d.villes WHERE d.id = :idDpt", Departement.class);
        query.setParameter("idDpt", idDpt);

        List<Departement> departements = query.getResultList();
        return departements.isEmpty() ? null : departements.getFirst();
    }

    /**
     * Méthode qui retourne un département par son code
     * @param codeDpt - code du département
     * @return Departement
     */
    public Departement extractDepartementByCode(String codeDpt) {
        TypedQuery<Departement> query = em.createQuery(
                "SELECT d FROM Departement d LEFT JOIN FETCH d.villes WHERE d.code = :codeDpt", Departement.class);
        query.setParameter("codeDpt", codeDpt);

        List<Departement> departements = query.getResultList();
        return departements.isEmpty() ? null : departements.getFirst();
    }

    /**
     * Méthode qui insère un département
     * @param dpt - département à insérer
     * @return Departement
     */
    public Departement insertDepartement(Departement dpt) {
        em.persist(dpt);
        return dpt;
    }

    /**
     * Méthode qui modifie un département
     * @param dpt - département à modifier
     * @return Departement
     */
    public Departement modifierDepartement(Departement dpt) {
        return em.merge(dpt);
    }

    /**
     * Méthode qui supprime un département
     * @param idDpt - id du département à supprimer
     * @return boolean
     */
    public boolean supprimerDepartement(int idDpt) {
        Departement dpt = em.find(Departement.class, idDpt);
        if (dpt != null) {
            em.remove(dpt);
            return true;
        }
        return false;
    }

    /**
     * Méthode qui retourne les n plus grandes villes d'un département
     * @param idDpt - id du département
     * @param n - nombre de villes à retourner
     * @return List<Ville>
     */
    public List<Ville> getNPlusGrandesVilles(int idDpt, int n) {
        TypedQuery<Ville> query = em.createQuery(
                "SELECT v FROM Ville v JOIN FETCH v.departement WHERE v.departement.id = :idDpt ORDER BY v.nbHabitants DESC", Ville.class);
        query.setParameter("idDpt", idDpt);
        query.setMaxResults(n);
        return query.getResultList();
    }

    /**
     * Méthode qui retourne les villes d'un département ayant une population comprise entre deux valeurs
     * @param idDpt - id du département
     * @param min - population minimale
     * @param max - population maximale
     * @return List<Ville>
     */
    public List<Ville> getVillesParPopulation(int idDpt, int min, int max) {
        TypedQuery<Ville> query = em.createQuery(
                "SELECT v FROM Ville v JOIN FETCH v.departement WHERE v.departement.id = :idDpt AND v.nbHabitants BETWEEN :min AND :max ORDER BY v.nbHabitants DESC", Ville.class);
        query.setParameter("idDpt", idDpt);
        query.setParameter("min", min);
        query.setParameter("max", max);
        return query.getResultList();
    }
}
