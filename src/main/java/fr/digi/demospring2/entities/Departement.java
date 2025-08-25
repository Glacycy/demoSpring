package fr.digi.demospring2.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DEPARTEMENT")
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "le code ne doit pas être vide")
    @Size(min = 2, max = 3, message = "Le code du département doit contenir 2 ou 3 caractères")
    @Column(name = "code", unique = true)
    private String code;

    @NotBlank(message = "le nom ne doit pas être vide")
    @Size(min = 2, message = "Le nom du département doit contenir au moins 2 caractères")
    @Column(name = "nom")
    private String nom;

    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Ville> villes = new ArrayList<>();

    //Constructeurs
    public Departement() {}

    public Departement(String code, String nom) {
        this.code = code;
        this.nom = nom;
    }

    public Departement(Integer id, String code, String nom) {
        this.id = id;
        this.code = code;
        this.nom = nom;
    }

    //Getters
    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getNom() {
        return nom;
    }

    public List<Ville> getVilles() {
        return villes;
    }

    //Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    //Méthodes utilitaires
    public void addVille(Ville ville) {
        villes.add(ville);
        ville.setDepartement(this);
    }

    public void removeVille(Ville ville) {
        villes.remove(ville);
        ville.setDepartement(null);
    }
}
