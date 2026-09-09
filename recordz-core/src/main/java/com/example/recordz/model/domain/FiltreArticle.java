package com.example.recordz.model.domain;

public class FiltreArticle {
    // ── Communs ───────────────────────────────────────────
    public String  nom;
    public String  marque;
    public Integer refCategorie;
    public Integer refSousCategorie;
    public Double  prixMin;
    public Double  prixMax;
    public boolean encheresOnly;
    public Integer     page     = 1;
    public Integer     pageSize = 50;

    // ── Véhicule ──────────────────────────────────────────
    public Integer nbKmMax;
    public Integer anneeMin;
    public Integer anneeMax;
    public Integer nbCheveauxMin;
    public Integer refEssence;
    public Integer refBoiteDeVitesse;

    // ── Immobilier ────────────────────────────────────────
    public Integer nbPieceMin;
    public Integer surfaceMin;
    public Integer surfaceMax;
    public String  lieu;
    public String  npa;

    // ── Vins ──────────────────────────────────────────────
    public Integer millesimeMin;
    public Integer millesimeMax;
    public Integer refTypeDeVin;

    // ── DVD ───────────────────────────────────────────────
    public String realisateur;
    public String acteurs;

    // ── Livres ────────────────────────────────────────────
    public String auteur;
    public String editeur;

    // ── Jeux ──────────────────────────────────────────────
    public Integer refTypeDeJeux;

    // ── TV ────────────────────────────────────────────────
    public Integer dimensionMin;
    public Integer dimensionMax;
    public Integer refTypeEcran;

    // ── PC ────────────────────────────────────────────────
    public String ram;
    public String processeurMin;

    // ── Vêtements ─────────────────────────────────────────
    public String taille;
}
