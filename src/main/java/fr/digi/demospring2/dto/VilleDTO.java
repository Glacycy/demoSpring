package fr.digi.demospring2.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO pour créer une ville avec son département
 */
public class VilleDTO {

    @NotBlank(message = "Le nom ne doit pas être vide")
    @Size(min = 2, message = "Le nom de la ville doit contenir au moins 2 caractères")
    private String nom;

    @Min(value = 1, message = "Le nombre d'habitants doitêtre supérieur ou égal à 1")
    private int nbHabitants;

    @NotNull(message = "L'id du département est obligatoire")
    private Integer departementId;

    //Constructeurs
    public VilleDTO() {}

    public VilleDTO(String nom, int nbHabitants, Integer departementId) {
        this.nom = nom;
        this.nbHabitants = nbHabitants;
        this.departementId = departementId;
    }

    //Getters
    public String getNom() {
        return nom;
    }
    public int getNbHabitants() {
        return nbHabitants;
    }
    public Integer getDepartementId() {
        return departementId;
    }

    //Setters
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setNbHabitants(int nbHabitants) {
        this.nbHabitants = nbHabitants;
    }
    public void setDepartementId(Integer departementId) {
        this.departementId = departementId;
    }

}
