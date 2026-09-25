package com.example.recordz.model.domain.dto;

import java.io.InputStream;

/**
 * DTO plat qui collecte toutes les valeurs saisies dans DynamicArticleForm.
 * Correspond 1-pour-1 aux colonnes de la table `article`.
 */
public class ArticleFormData {

    // ---- Champs communs ----
    public String  nom;
    public String  label;           // description
    public double  prix;
    public Integer refConditionPayement;
    public Integer refModeDelivraison;
    public Integer refEtat;
    public Integer quantite;
    public String  marque;          // fabricant

    // ---- Catégorie / sous-catégorie ----
    public int     refCategorie;
    public Integer refSubcategorie;

    // ---- Enchère ----
    public boolean enchere;
    public String  enchereDateDebut;
    public String  enchereDateFin;

    // ---- Image ----
    public String      pochette    = "";  // nom original du fichier uploadé
    public InputStream pochetteStream;   // contenu binaire (depuis MemoryBuffer)
    public String      pochetteContentType; // ex: "image/jpeg"

    // ---- Voiture / Moto ----
    public Integer nbCheveaux;
    public Integer nbCylindre;
    public Integer nbKm;
    public String  annee;
    public String  premiereImmatriculation;
    public Integer essenceOuDiesel;   // ref_type_essence
    public Integer refBoiteDeVitesse;
    public Boolean clima;

    // ---- Livres ----
    public String auteur;
    public String editeur;          // stocké dans `marque` pour livres

    // ---- Immobilier ----
    public Integer refLocationOuAchat;
    public Integer refPays;
    public Integer refCanton;
    public Integer refDepartement;
    public String  lieu;
    public String  adresse;
    public String  npa;
    public Integer nbPiece;
    public Integer surfaceHabitable;
    public String  superficieTerrain;
    public String  anneeConstruction;

    // ---- Vins ----
    public Integer refPaysRegionVin;   // pays
    public Integer refCepage;
    public Integer refTypeDeVin;
    public String  millesime;          // stocké dans `annee`

    // ---- Jeux ----
    public Integer refTypeDeJeux;

    // ---- DVD ----
    public String  acteurs;
    public String  realisateur;
    public String  duree;

    // ---- TV / Écran ----
    public Integer refTypeEcran;
    public Integer dimension;

    // ---- PC ----
    public Double  processeur;
    public String  ram;
    public String  disqueDur;

    // ---- Vêtements ----
    public String  taille;

    public String videoPath;
}
