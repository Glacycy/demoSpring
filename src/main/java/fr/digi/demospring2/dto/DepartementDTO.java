package fr.digi.demospring2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO pour créer un département
 */
public class DepartementDTO {

    @NotBlank(message = "Le code ne doit pas être vide")
    @Size(min = 2, max = 3, message = "Le code du département doit contenir 2 ou 3 caractères")
    private String code;

    @NotBlank(message = "Le nom ne doit pas être vide")
    @Size(min = 2, message = "Le nom du département doit contenir au moins 2 caractères")
    private String nom;

    private long nbHabitants;

    //Constructeurs
    public DepartementDTO() {}

    public DepartementDTO(String code, String nom, long nbHabitants) {
        this.code = code;
        this.nom = nom;
        this.nbHabitants = nbHabitants;
    }

    //Getters
    public String getCode() {
        return code;
    }
    public String getNom() {
        return nom;
    }
    public long getNbHabitants() {
        return nbHabitants;
    }

    //Setters
    public void setCode(String code) {
        this.code = code;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setNbHabitants(long nbHabitants) {
        this.nbHabitants = nbHabitants;
    }
}
