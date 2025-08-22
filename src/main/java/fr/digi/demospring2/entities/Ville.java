package fr.digi.demospring2.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "VILLE")
public class Ville {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive(message = "l'id doit être strictement positif")
    private Integer id;

    @NotBlank(message = "le nom ne doit pas être vide")
    @Size(min = 2, message = "Le nom de la ville doit contenir au moins 2 caractères")
    @Column(name = "nom")
    private String nom;

    @Min(value = 1, message = "Le nombre d'habitants doit être supérieur ou égal à 1")
    @Column(name = "nb_Habitants")
    private int nbHabitants;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departement_id", nullable = false)
    @JsonBackReference
    private Departement departement;

    //Constructeurs
    public Ville() {}

    public Ville(String nom, int nbHabitants, Departement departement) {
        this.nom = nom;
        this.nbHabitants = nbHabitants;
        this.departement = departement;
    }

    public Ville(Integer id,String nom, int nbHabitants, Departement departement) {
        this.id = id;
        this.nom = nom;
        this.nbHabitants = nbHabitants;
        this.departement = departement;
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
    public Departement getDepartement() {
        return departement;
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
    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
}
