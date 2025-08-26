package fr.digi.demospring2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO pour recevoir les données des départements depuis l'API externe
 */
public class DepartementApiDTO {

    /**
     * Code du département
     */
    @JsonProperty("code")
    private String code;

    /**
     * Nom du département
     */
    @JsonProperty("nom")
    private String nom;

    /**
     * Code de la région à laquelle appartient le département
     */
    @JsonProperty("codeRegion")
    private String codeRegion;

    // Constructeurs
    public DepartementApiDTO() {}

    public DepartementApiDTO(String code, String nom, String codeRegion) {
        this.code = code;
        this.nom = nom;
        this.codeRegion = codeRegion;
    }

    // Getters et Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodeRegion() {
        return codeRegion;
    }

    public void setCodeRegion(String codeRegion) {
        this.codeRegion = codeRegion;
    }

    @Override
    public String toString() {
        return "DepartementApiDTO{" +
                "code='" + code + '\'' +
                ", nom='" + nom + '\'' +
                ", codeRegion='" + codeRegion + '\'' +
                '}';
    }
}