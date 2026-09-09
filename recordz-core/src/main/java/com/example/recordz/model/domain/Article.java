package com.example.recordz.model.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Article {

    private Long idArticle;
    private String auteur;
    private String marque;
    private String fabricant;
    private String nom;
    private String label;
    private Double prix;
    private BigDecimal prixAchat;
    private String pochette;
    private String pochette2;
    private String presound;
    private Integer refGenre;
    private Integer etat;
    private Integer refCategorie;
    private Integer refSubcategorie;
    private Integer owned;
    private Integer refStatut;
    private String date;
    private Long refDepot;
    private String refTaille;
    private Integer refCouleur;
    private Integer enchere;
    private Integer refConditionPayement;
    private Integer refModeDeLivraison;
    private String enchereDateDebut;
    private String enchereDateFin;
    private Integer vendu;
    private Integer refTypeEcran;
    private Integer dimension;
    private Integer poids;
    private Integer nbPorte;
    private Integer nbCheveaux;
    private Integer nbKm;
    private String premiereImmatriculation;
    private Integer annee;
    private String options;
    private Integer essenceOuDiesel;
    private Integer nbPiece;
    private Integer surfaceHabitable;
    private String superficieTerrain;
    private String anneeConstruction;
    private Long visites;
    private Long nbrEnchere;
    private String lang;
    private Integer refCanton;
    private String lieu;
    private String adresse;
    private String npa;
    private Integer refLocationOuAchat;
    private Integer refDepartement;
    private Integer refPays;
    private Integer refBoiteDeVitesse;
    private Byte clima;
    private BigDecimal processeur;
    private String ram;
    private String disqueDur;
    private Integer quantite;
    private Byte refProvenance;
    private Integer longueur;
    private Integer largeur;
    private Integer consomation;
    private String acteurs;
    private String realisateur;
    private String taille;
    private Integer refTypeDeJeux;
    private Integer refCepage;
    private Integer refPaysRegionVin;
    private Integer refTypeDeVin;
    private Integer refEtat;
    private BigDecimal fraisLivraison;
    private Integer wat;
    private Integer nbCylindre;

    public String getTx() {
        return tx;
    }

    public void setTx(String tx) {
        this.tx = tx;
    }

    public Integer getPub() {
        return pub;
    }

    public void setPub(Integer pub) {
        this.pub = pub;
    }

    private Integer pub;
    private String pubDateStart;
    private String pubDateEnd;
    private String tx;
    private String linkYoutube;
    private Long refAcheteur;

    // ── Champs ajoutés ────────────────────────────────────────
    private Integer refArticle;
    private Integer refVendeur;
    private BigDecimal montant;
    private LocalDateTime dateFermetureEnchere;
    

    public Article() {}

    // ── Getters & Setters ─────────────────────────────────────

    public Long getIdArticle() { return idArticle; }
    public void setIdArticle(Long idArticle) { this.idArticle = idArticle; }

    public Long getRefAcheteur() { return refAcheteur; }
    public void setRefAcheteur(Long refAcheteur) { this.refAcheteur = refAcheteur; }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }

    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }

    public BigDecimal getPrixAchat() { return prixAchat; }
    public void setPrixAchat(BigDecimal prixAchat) { this.prixAchat = prixAchat; }

    public String getPochette() { return pochette; }
    public void setPochette(String pochette) { this.pochette = pochette; }

    public String getPresound() { return presound; }
    public void setPresound(String presound) { this.presound = presound; }

    public Integer getRefGenre() { return refGenre; }
    public void setRefGenre(Integer refGenre) { this.refGenre = refGenre; }

    public Integer getEtat() { return etat; }
    public void setEtat(Integer etat) { this.etat = etat; }

    public Integer getRefCategorie() { return refCategorie; }
    public void setRefCategorie(Integer refCategorie) { this.refCategorie = refCategorie; }

    public Integer getRefSubcategorie() { return refSubcategorie; }
    public void setRefSubcategorie(Integer refSubcategorie) { this.refSubcategorie = refSubcategorie; }

    public Integer getOwned() { return owned; }
    public void setOwned(Integer owned) { this.owned = owned; }

    public Integer getRefStatut() { return refStatut; }
    public void setRefStatut(Integer refStatut) { this.refStatut = refStatut; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Long getRefDepot() { return refDepot; }
    public void setRefDepot(Long refDepot) { this.refDepot = refDepot; }

    public Integer getEnchere() { return enchere; }
    public void setEnchere(Integer enchere) { this.enchere = enchere; }

    public Integer getRefConditionPayement() { return refConditionPayement; }
    public void setRefConditionPayement(Integer refConditionPayement) { this.refConditionPayement = refConditionPayement; }

    public Integer getRefModeDeLivraison() { return refModeDeLivraison; }
    public void setRefModeDeLivraison(Integer refModeDeLivraison) { this.refModeDeLivraison = refModeDeLivraison; }

    public Integer getVendu() { return vendu; }
    public void setVendu(Integer vendu) { this.vendu = vendu; }

    public Long getVisites() { return visites; }
    public void setVisites(Long visites) { this.visites = visites; }

    public Long getNbrEnchere() { return nbrEnchere; }
    public void setNbrEnchere(Long nbrEnchere) { this.nbrEnchere = nbrEnchere; }

    public String getLang() { return lang; }
    public void setLang(String lang) { this.lang = lang; }

    public Integer getRefCanton() { return refCanton; }
    public void setRefCanton(Integer refCanton) { this.refCanton = refCanton; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getNpa() { return npa; }
    public void setNpa(String npa) { this.npa = npa; }

    public Integer getRefPays() { return refPays; }
    public void setRefPays(Integer refPays) { this.refPays = refPays; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public String getLinkYoutube() { return linkYoutube; }
    public void setLinkYoutube(String linkYoutube) { this.linkYoutube = linkYoutube; }

    public String getEnchereDateDebut() { return enchereDateDebut; }
    public void setEnchereDateDebut(String v) { this.enchereDateDebut = v; }

    public String getEnchereDateFin() { return enchereDateFin; }
    public void setEnchereDateFin(String v) { this.enchereDateFin = v; }

    public String getRefTaille() { return refTaille; }
    public void setRefTaille(String v) { this.refTaille = v; }

    public Integer getRefCouleur() { return refCouleur; }
    public void setRefCouleur(Integer v) { this.refCouleur = v; }

    public String getFabricant() { return this.fabricant; }
    public void setFabricant(String fabricant) { this.fabricant = fabricant; }

    // ── Véhicule ──────────────────────────────────────────────

    public Integer getNbCheveaux() { return nbCheveaux; }
    public void setNbCheveaux(Integer v) { this.nbCheveaux = v; }

    public Integer getNbCylindre() { return nbCylindre; }
    public void setNbCylindre(Integer v) { this.nbCylindre = v; }

    public Integer getNbKm() { return nbKm; }
    public void setNbKm(Integer v) { this.nbKm = v; }

    public String getPremiereImmatriculation() { return premiereImmatriculation; }
    public void setPremiereImmatriculation(String v) { this.premiereImmatriculation = v; }

    public Integer getEssenceOuDiesel() { return essenceOuDiesel; }
    public void setEssenceOuDiesel(Integer v) { this.essenceOuDiesel = v; }

    public Integer getRefBoiteDeVitesse() { return refBoiteDeVitesse; }
    public void setRefBoiteDeVitesse(Integer v) { this.refBoiteDeVitesse = v; }

    public Byte getClima() { return clima; }
    public void setClima(Byte v) { this.clima = v; }

    // ── Année (commun voiture, livres, DVD) ───────────────────

    public Integer getAnnee() { return annee; }
    public void setAnnee(Integer v) { this.annee = v; }

    // ── Immobilier ────────────────────────────────────────────

    public Integer getNbPiece() { return nbPiece; }
    public void setNbPiece(Integer v) { this.nbPiece = v; }

    public Integer getSurfaceHabitable() { return surfaceHabitable; }
    public void setSurfaceHabitable(Integer v) { this.surfaceHabitable = v; }

    public String getSuperficieTerrain() { return superficieTerrain; }
    public void setSuperficieTerrain(String v) { this.superficieTerrain = v; }

    public String getAnneeConstruction() { return anneeConstruction; }
    public void setAnneeConstruction(String v) { this.anneeConstruction = v; }

    public Integer getRefLocationOuAchat() { return refLocationOuAchat; }
    public void setRefLocationOuAchat(Integer v) { this.refLocationOuAchat = v; }

    public Integer getRefDepartement() { return refDepartement; }
    public void setRefDepartement(Integer v) { this.refDepartement = v; }

    // ── Vins ──────────────────────────────────────────────────

    public Integer getRefCepage() { return refCepage; }
    public void setRefCepage(Integer v) { this.refCepage = v; }

    public String getMillesime() { return tx; }
    public void setMillesime(String v) { this.tx = v; }

    public Integer getRefTypeDeVin() { return refTypeDeVin; }
    public void setRefTypeDeVin(Integer v) { this.refTypeDeVin = v; }

    public Integer getRefPaysRegionVin() { return refPaysRegionVin; }
    public void setRefPaysRegionVin(Integer v) { this.refPaysRegionVin = v; }

    // ── DVD ───────────────────────────────────────────────────

    public String getActeurs() { return acteurs; }
    public void setActeurs(String v) { this.acteurs = v; }

    public String getRealisateur() { return realisateur; }
    public void setRealisateur(String v) { this.realisateur = v; }

    public String getDuree() { return presound; }
    public void setDuree(String v) { this.presound = v; }

    // ── Jeux ──────────────────────────────────────────────────

    public Integer getRefTypeDeJeux() { return refTypeDeJeux; }
    public void setRefTypeDeJeux(Integer v) { this.refTypeDeJeux = v; }

    // ── TV / Écran ────────────────────────────────────────────

    public Integer getRefTypeEcran() { return refTypeEcran; }
    public void setRefTypeEcran(Integer v) { this.refTypeEcran = v; }

    public Integer getDimension() { return dimension; }
    public void setDimension(Integer v) { this.dimension = v; }

    // ── PC ────────────────────────────────────────────────────

    public BigDecimal getProcesseur() { return processeur; }
    public void setProcesseur(BigDecimal v) { this.processeur = v; }

    public String getRam() { return ram; }
    public void setRam(String v) { this.ram = v; }

    public String getDisqueDur() { return disqueDur; }
    public void setDisqueDur(String v) { this.disqueDur = v; }

    // ── Vêtements ─────────────────────────────────────────────

    public String getTaille() { return taille; }
    public void setTaille(String v) { this.taille = v; }

    // ── Cépage ────────────────────────────────────────────────

    public Integer getCepage() { return refCepage; }
    public void setCepage(Integer v) { this.refCepage = v; }

    // ── Getters & Setters des champs ajoutés ──────────────────

    public Integer getRefArticle() { return refArticle; }
    public void setRefArticle(Integer refArticle) { this.refArticle = refArticle; }

    public Integer getRefVendeur() { return refVendeur; }
    public void setRefVendeur(Integer refVendeur) { this.refVendeur = refVendeur; }

    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }

    public LocalDateTime getDateFermetureEnchere() { return dateFermetureEnchere; }
    public void setDateFermetureEnchere(LocalDateTime dateFermetureEnchere) { this.dateFermetureEnchere = dateFermetureEnchere; }

    public Integer getRefConditonPayement() { return refConditionPayement; }
    public void setRefConditonPayement(Integer refConditionPayement) { this.refConditionPayement = refConditionPayement; }

    public String getPochette2() {
        return pochette2;
    }

    public void setPochette2(String pochette2) {
        this.pochette2 = pochette2;
    }
}


