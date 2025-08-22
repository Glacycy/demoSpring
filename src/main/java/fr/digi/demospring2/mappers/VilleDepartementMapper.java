package fr.digi.demospring2.mappers;

import fr.digi.demospring2.dto.DepartementDTO;
import fr.digi.demospring2.dto.VilleDTO;
import fr.digi.demospring2.entities.Departement;
import fr.digi.demospring2.entities.Ville;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe de mapping des villes et des départements
 */
@Component
public class VilleDepartementMapper {

    /**
     * Convertit une ville en villeDTO
     * @param ville - ville à convertir
     * @return VilleDTO
     */
    public VilleDTO toVilleDTO(Ville ville) {
        if (ville == null) {
            return null;
        }

        String codeDpt = ville.getDepartement() != null ? ville.getDepartement().getCode() : null;
        String nomDpt = ville.getDepartement() != null ? ville.getDepartement().getNom() : null;
        Integer dptId = ville.getDepartement() != null ? ville.getDepartement().getId() : null;
        return new VilleDTO(
                ville.getNom(),
                ville.getNbHabitants(),
                codeDpt,
                nomDpt,
                dptId
        );
    }

    /**
     * Convertit une liste de ville en liste de villeDTO
     * @param villes - villes à convertir
     * @return List<VilleDTO>
     */
    public List<VilleDTO> toVilleDTO(List<Ville> villes) {
        return villes.stream()
                .map(this::toVilleDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit villeDTO en ville
     * @param villeDTO - villeDTO à convertir
     * @param departement - département de la ville
     * @return Ville
     */
    public Ville toVille(VilleDTO villeDTO, Departement departement) {
        if (villeDTO == null) {
            return null;
        }

        return new Ville(
                villeDTO.getNom(),
                villeDTO.getNbHabitants(),
                departement
        );
    }

    /**
     * Convertit un département en departementDTO
     * @param departement - département à convertir
     * @return DepartementDTO
     */
    public DepartementDTO toDepartementDTO(Departement departement) {
        if (departement == null) {
            return null;
        }

        long totalHabitants = departement.getVilles().stream()
                .mapToLong(Ville::getNbHabitants)
                .sum();

        return new DepartementDTO(
                departement.getCode(),
                departement.getNom(),
                totalHabitants
        );
    }

    /**
     * Convertit une liste de département en liste de departementDTO
     * @param departements - departements à convertir
     * @return List<DepartementDTO>
     */
    public List<DepartementDTO> toDepartementDTO(List<Departement> departements) {
        return departements.stream()
                .map(this::toDepartementDTO)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité ville avec les données de villeDTO
     * @param ville - ville à mettre à jour
     * @param villeDTO - villeDTO contenant les nouvelles données
     */
    public void updateVilleFromDTO(Ville ville, VilleDTO villeDTO) {
        if (ville != null && villeDTO != null) {
            ville.setNom(villeDTO.getNom());
            ville.setNbHabitants(villeDTO.getNbHabitants());
        }
    }
    /**
     * Met à jour une entité département avec les données de départementDTO
     * @param departement - département à mettre à jour
     * @param departementDTO - départementDTO contenant les nouvelles données
     */
    public void updateDepartementFromDTO(Departement departement, DepartementDTO departementDTO) {
        if (departement != null && departementDTO != null) {
            departement.setCode(departementDTO.getCode());
            departement.setNom(departementDTO.getNom());
        }
    }
}
