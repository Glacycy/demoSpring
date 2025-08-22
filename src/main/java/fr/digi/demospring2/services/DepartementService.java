package fr.digi.demospring2.services;

import fr.digi.demospring2.dao.DepartementDao;
import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementService {

    @Autowired
    private DepartementDao dptDao;

    /**
     * Méthode qui retourne la liste des départements avec leurs villes
     * @return List<Departement>
     */
    public List<Departement> extractDepartements() {
        return dptDao.extractDepartements();
    }

    /**
     * Méthode qui retourne un département par son Id
     * @param idDpt - id du département
     * @return Departement
     */
    public Departement extractDepartement(int idDpt) {
        return dptDao.extractDepartement(idDpt);
    }

    /**
     * Méthode qui retourne un département par son code
     * @param codeDpt - code du département
     * @return Departement
     */
    public Departement extractDepartementByCode(String codeDpt) {
        return dptDao.extractDepartementByCode(codeDpt);
    }

    /**
     * Méthode qui insère un département et retourne la liste des départements
     * @param dpt - département à insérer
     * @return List<Departement>
     */
    public List<Departement> insertDepartement(Departement dpt) {
        dptDao.insertDepartement(dpt);
        return dptDao.extractDepartements();
    }

    /**
     * Méthode qui modifie un département et retourne la liste des départements
     * @param idDpt - id du département à modifier
     * @param dptEdited - département modifié
     * @return List<Departement>
     */
    public List<Departement> modifierDepartement(int idDpt, Departement dptEdited) {
        Departement dptExistant = dptDao.extractDepartement(idDpt);
        if (dptExistant != null) {
            dptExistant.setCode(dptEdited.getCode());
            dptExistant.setNom(dptEdited.getNom());
            dptDao.modifierDepartement(dptExistant);
        }
        return dptDao.extractDepartements();
    }

    /**
     * Méthode qui supprime un département et retourne la liste des départements
     * @param idDpt - id du département à supprimer
     * @return List<Departement>
     */
    public List<Departement> supprimerDepartement(int idDpt) {
        dptDao.supprimerDepartement(idDpt);
        return dptDao.extractDepartements();
    }

    /**
     * Méthode qui retourne les n plus grandes villes d'un département
     * @param idDpt - id du département
     * @param n - nombre de villes à retourner
     * @return List<Ville>
     */
    public List<Ville> getNPlusGrandesVilles(int idDpt, int n) {
        return dptDao.getNPlusGrandesVilles(idDpt, n);
    }

    /**
     * Méthode qui retourne les villes d'un département ayant une population comprise entre deux valeurs
     * @param idDpt - id du département
     * @param min - population minimale
     * @param max - population maximale
     * @return List<Ville>
     */
    public List<Ville> getVillesParPopulation(int idDpt, int min, int max) {
        return dptDao.getVillesParPopulation(idDpt, min, max);
    }
}
