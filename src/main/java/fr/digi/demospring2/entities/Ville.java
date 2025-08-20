package fr.digi.demospring2.entities;

public class Ville {
    private String nom;
    private int nbHabitants;

    //Constructor
    public Ville(String nom, int nbHabitants) {
        this.nom = nom;
        this.nbHabitants = nbHabitants;
    }

    //Getters
    public String getNom() {
        return nom;
    }
    public int getNbHabitants() {
        return nbHabitants;
    }

    //Setters
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setNbHabitants(int nbHabitants) {
        this.nbHabitants = nbHabitants;
    }
}
