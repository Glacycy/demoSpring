package fr.digi.demospring2.services;

import fr.digi.demospring2.dao.VilleDao;
import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VilleService {

    @Autowired
    private VilleDao villeDao;

    @Autowired
    private DepartementService dptService;

    /**
     * Méthode qui retourne la liste des villes
     * @return List<Ville>
     */
    public List<Ville> extractVilles() {
        return villeDao.extractVilles();
    }

    /**
     * Méthode qui retourne une ville par son id
     * @param idVille - id de la ville
     * @return Ville
     */
    public Ville extractVille(int idVille) {
        return villeDao.extractVille(idVille);
    }

    /**
     * Méthode qui retourne une ville par son nom
     * @param nom - nom de la ville
     * @return Ville
     */
    public Ville extractVille(String nom) {
        return villeDao.extractVille(nom);
    }

    /**
     * Méthode qui ajoute une ville et retourne la liste des villes après insertion
     * @param ville - ville à ajouter
     * @return List<Ville>
     */
    public List<Ville> insertVille(Ville ville) {

        if (ville.getDepartement() != null && ville.getDepartement().getId() != null) {
            Departement dpt = dptService.extractDepartement(ville.getDepartement().getId());
            if(dpt == null) {
                throw new RuntimeException("Le département avec l'id" + ville.getDepartement().getId() + " n'existe pas");
            }
            ville.setDepartement(dpt);
        } else {
            throw new RuntimeException("Une ville doit obligatoirement être associée à un département");
        }

        villeDao.insertVille(ville);
        return villeDao.extractVilles();
    }

    /**
     * Méthode qui modifie la ville dont l'id est passé en param
     * @param idVille - id de la ville à modifier
     * @param villeModifiee - ville modifiée
     * @return List<Ville>
     */
    public List<Ville> modifierVille(int idVille, Ville villeModifiee) {
        Ville villeExistante = villeDao.extractVille(idVille);
        if (villeExistante != null) {
            villeExistante.setNom(villeModifiee.getNom());
            villeExistante.setNbHabitants(villeModifiee.getNbHabitants());
            villeDao.modifierVille(villeExistante);
        }
        return villeDao.extractVilles();
    }

    /**
     * Méthode qui supprime la ville dont l'id est passé en param et retourne la liste des villes restantes
     * @param idVille - id de la ville à supprimer
     * @return List<Ville>
     */
    public List<Ville> supprimerVille(int idVille) {
        villeDao.supprimerVille(idVille);
        return villeDao.extractVilles();
    }

}
