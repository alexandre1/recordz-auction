package com.example.recordz.model.domain;

import java.time.LocalDate;

public class Boutique {

    private Long idBoutique;
    private String nom;
    private LocalDate dateCreation;
    private Long refPersonne;
    private Integer refMainCategorie;
    private String login;

    public Boutique() {}

    public Long getIdBoutique() { return idBoutique; }
    public void setIdBoutique(Long idBoutique) { this.idBoutique = idBoutique; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }

    public Long getRefPersonne() { return refPersonne; }
    public void setRefPersonne(Long refPersonne) { this.refPersonne = refPersonne; }

    public Integer getRefMainCategorie() { return refMainCategorie; }
    public void setRefMainCategorie(Integer refMainCategorie) { this.refMainCategorie = refMainCategorie; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
}
