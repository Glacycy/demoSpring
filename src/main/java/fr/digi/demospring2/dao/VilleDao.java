package fr.digi.demospring2.dao;

import fr.digi.demospring2.entities.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class VilleDao {

    @PersistenceContext
    private EntityManager em;

    /**
     * Méthode qui retourne la liste des villes
     * @return List<Ville>
     */
    public List<Ville> extractVilles() {
        TypedQuery<Ville> query = em.createQuery("select v from Ville v", Ville.class);
        return query.getResultList();
    }

    /**
     * Méthode qui retourne une ville par son id
     * @param idVille - id de la ville
     * @return Ville
     */
    public Ville extractVille(int idVille) {
        return em.find(Ville.class, idVille);
    }

    /**
     * Méthode qui retourne une ville par son nom
     * @param nom - nom de la ville
     * @return Ville
     */
    public Ville extractVille(String nom) {
        TypedQuery<Ville> query = em.createQuery(
                "select v from Ville v where v.nom = :nom", Ville.class);
        query.setParameter("nom", nom);

        List<Ville> villes = query.getResultList();
        return villes.isEmpty() ? null : villes.getFirst();
    }

    /**
     * Méthode qui ajoute une ville
     * @param ville - ville à ajouter
     * @return Ville
     */
    public Ville insertVille(Ville ville) {
        em.persist(ville);
        return ville;
    }

    /**
     * Méthode qui modifie une ville
     * @param ville - ville à modifier
     * @return Ville
     */
    public Ville modifierVille(Ville ville) {
        return em.merge(ville);
    }

    /**
     * Méthode qui supprime une ville
     * @param idVille - ville à supprimer
     * @return boolean
     */
    public boolean supprimerVille(int idVille) {
        Ville ville = em.find(Ville.class, idVille);
        if (ville != null) {
            em.remove(ville);
            return true;
        }
        return false;
    }
}
