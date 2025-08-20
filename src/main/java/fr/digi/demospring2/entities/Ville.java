package fr.digi.demospring2.entities;

public class Ville {
    private Integer id;
    private String nom;
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
