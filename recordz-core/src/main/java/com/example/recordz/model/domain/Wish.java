package com.example.recordz.model.domain;

import java.time.LocalDateTime;

public class Wish {

    private Long idWish;
    private Long refWishList;
    private Long refUser;
    private String auteur;
    private String marque;
    private String nom;
    private String label;
    private Integer prix;
    private String pochette;
    private String presound;
    private Integer refGenre;
    private String etat;
    private Integer refCategorie;
    private Integer refSubcategorie;
    private Integer owned;
    private Integer refStatut;
    private LocalDateTime date;
    private Long refDepot;
    private Integer enchere;
    private Integer refConditionPayement;
    private Integer refModeDeLivraison;
    private LocalDateTime enchereDateDebut;
    private LocalDateTime enchereDateFin;
    private Integer vendu;
    private String lang;
    private Integer refCanton;
    private Integer refPays;

    public Wish() {}

    public Long getIdWish() { return idWish; }
    public void setIdWish(Long idWish) { this.idWish = idWish; }

    public Long getRefWishList() { return refWishList; }
    public void setRefWishList(Long refWishList) { this.refWishList = refWishList; }

    public Long getRefUser() { return refUser; }
    public void setRefUser(Long refUser) { this.refUser = refUser; }

    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public Integer getPrix() { return prix; }
    public void setPrix(Integer prix) { this.prix = prix; }

    public Integer getRefCategorie() { return refCategorie; }
    public void setRefCategorie(Integer refCategorie) { this.refCategorie = refCategorie; }

    public Integer getVendu() { return vendu; }
    public void setVendu(Integer vendu) { this.vendu = vendu; }

    public String getLang() { return lang; }
    public void setLang(String lang) { this.lang = lang; }

    public Integer getRefCanton() { return refCanton; }
    public void setRefCanton(Integer refCanton) { this.refCanton = refCanton; }

    public Integer getRefPays() { return refPays; }
    public void setRefPays(Integer refPays) { this.refPays = refPays; }
}
