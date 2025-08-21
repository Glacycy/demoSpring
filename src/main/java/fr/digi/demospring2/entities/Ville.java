package fr.digi.demospring2.entities;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class Ville {

    @Positive(message = "l'id doit être strictement positif")
    private Integer id;
    @NotBlank(message = "le nom ne doit pas être vide")
    @Size(min = 2, message = "Le nom de la ville doit contenir au moins 2 caractères")
    private String nom;

    @Min(value = 1, message = "Le nombre d'habitants doit être supérieur ou égal à 1")
    private int nbHabitants;

    public Ville() {}

    //Constructor
    public Ville(Integer id,String nom, int nbHabitants) {
        this.id = id;
        this.nom = nom;
        this.nbHabitants = nbHabitants;
    }

    //Getters
    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }
    public int getNbHabitants() {
        return nbHabitants;
    }

    //Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setNbHabitants(int nbHabitants) {
        this.nbHabitants = nbHabitants;
    }
}
