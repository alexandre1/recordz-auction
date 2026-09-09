package com.example.recordz.model.domain;

import java.math.BigDecimal;
import java.util.List;

public class Referentiel {

    // ── Libelle ───────────────────────────────────────────────
    public static class Libelle {
        private Long idLibelle;
        private String libelle;

        public Libelle() {}
        public Libelle(Long idLibelle, String libelle) {
            this.idLibelle = idLibelle;
            this.libelle   = libelle;
        }
        public Long getIdLibelle() { return idLibelle; }
        public void setIdLibelle(Long v) { this.idLibelle = v; }
        public String getLibelle() { return libelle; }
        public void setLibelle(String v) { this.libelle = v; }
    }
    // ── CategorieLabel ────────────────────────────────────────
    public static class CategorieLabel {
        private Integer refCategorie;
        private Long    refLibelle;
        private Integer refLangue;

        public CategorieLabel() {}
        public CategorieLabel(Integer refCategorie, Long refLibelle, Integer refLangue) {
            this.refCategorie = refCategorie;
            this.refLibelle   = refLibelle;
            this.refLangue    = refLangue;
        }
        public Integer getRefCategorie() { return refCategorie; }
        public void setRefCategorie(Integer v) { this.refCategorie = v; }
        public Long getRefLibelle() { return refLibelle; }
        public void setRefLibelle(Long v) { this.refLibelle = v; }
        public Integer getRefLangue() { return refLangue; }
        public void setRefLangue(Integer v) { this.refLangue = v; }
    }

    // ── MainCategorieLabel ────────────────────────────────────
    public static class MainCategorieLabel {
        private Integer refMainCategorie;
        private Long    refLibelle;
        private Integer refLangue;

        public MainCategorieLabel() {}
        public MainCategorieLabel(Integer refMainCategorie, Long refLibelle, Integer refLangue) {
            this.refMainCategorie = refMainCategorie;
            this.refLibelle       = refLibelle;
            this.refLangue        = refLangue;
        }
        public Integer getRefMainCategorie() { return refMainCategorie; }
        public void setRefMainCategorie(Integer v) { this.refMainCategorie = v; }
        public Long getRefLibelle() { return refLibelle; }
        public void setRefLibelle(Long v) { this.refLibelle = v; }
        public Integer getRefLangue() { return refLangue; }
        public void setRefLangue(Integer v) { this.refLangue = v; }
    }

    // ── SubcategorieLabel ─────────────────────────────────────
    public static class SubcategorieLabel {
        private Integer refSubcategorie;
        private Integer refCategorie;
        private Long    refLibelle;
        private Integer refLangue;

        public SubcategorieLabel() {}
        public SubcategorieLabel(Integer refSubcategorie, Integer refCategorie,
                                 Long refLibelle, Integer refLangue) {
            this.refSubcategorie = refSubcategorie;
            this.refCategorie    = refCategorie;
            this.refLibelle      = refLibelle;
            this.refLangue       = refLangue;
        }
        public Integer getRefSubcategorie() { return refSubcategorie; }
        public void setRefSubcategorie(Integer v) { this.refSubcategorie = v; }
        public Integer getRefCategorie() { return refCategorie; }
        public void setRefCategorie(Integer v) { this.refCategorie = v; }
        public Long getRefLibelle() { return refLibelle; }
        public void setRefLibelle(Long v) { this.refLibelle = v; }
        public Integer getRefLangue() { return refLangue; }
        public void setRefLangue(Integer v) { this.refLangue = v; }
    }

    // ── Canton ────────────────────────────────────────────────
    public static class Canton {
        private Integer idCanton;
        private String  nom;

        public Canton() {}
        public Canton(Integer idCanton, String nom) {
            this.idCanton = idCanton;
            this.nom      = nom;
        }
        public Integer getIdCanton() { return idCanton; }
        public void setIdCanton(Integer v) { this.idCanton = v; }
        public String getNom() { return nom; }
        public void setNom(String v) { this.nom = v; }
    }

    // ── Departement ───────────────────────────────────────────
    public static class Departement {
        private Integer idDepartement;
        private String  nom;
        private String  code;

        public Departement() {}
        public Departement(Integer idDepartement, String nom, String code) {
            this.idDepartement = idDepartement;
            this.nom           = nom;
            this.code          = code;
        }
        public Integer getIdDepartement() { return idDepartement; }
        public void setIdDepartement(Integer v) { this.idDepartement = v; }
        public String getNom() { return nom; }
        public void setNom(String v) { this.nom = v; }
        public String getCode() { return code; }
        public void setCode(String v) { this.code = v; }
    }

    // ── PaysPresent ───────────────────────────────────────────
    public static class PaysPresent {
        private Integer idPaysPresent;
        private String  nom;

        public PaysPresent() {}
        public PaysPresent(Integer idPaysPresent, String nom) {
            this.idPaysPresent = idPaysPresent;
            this.nom           = nom;
        }
        public Integer getIdPaysPresent() { return idPaysPresent; }
        public void setIdPaysPresent(Integer v) { this.idPaysPresent = v; }
        public String getNom() { return nom; }
        public void setNom(String v) { this.nom = v; }
    }

    // ── PaysRegionVin ─────────────────────────────────────────
    public static class PaysRegionVin {
        private Integer idPaysRegionVin;
        private Integer refPays;
        private Integer parentId;
        private String  nom;

        public PaysRegionVin() {}
        public PaysRegionVin(Integer idPaysRegionVin, Integer refPays,
                             Integer parentId, String nom) {
            this.idPaysRegionVin = idPaysRegionVin;
            this.refPays         = refPays;
            this.parentId        = parentId;
            this.nom             = nom;
        }
        public Integer getIdPaysRegionVin() { return idPaysRegionVin; }
        public void setIdPaysRegionVin(Integer v) { this.idPaysRegionVin = v; }
        public Integer getRefPays() { return refPays; }
        public void setRefPays(Integer v) { this.refPays = v; }
        public Integer getParentId() { return parentId; }
        public void setParentId(Integer v) { this.parentId = v; }
        public String getNom() { return nom; }
        public void setNom(String v) { this.nom = v; }
    }

    // ── Cepage ────────────────────────────────────────────────
    public static class Cepage {
        private Integer idCepage;
        private Integer refPaysRegionVin;
        private Integer refTypeDeVin;
        private String  nom;

        public Cepage() {}
        public Cepage(Integer idCepage, Integer refPaysRegionVin,
                      Integer refTypeDeVin, String nom) {
            this.idCepage         = idCepage;
            this.refPaysRegionVin = refPaysRegionVin;
            this.refTypeDeVin     = refTypeDeVin;
            this.nom              = nom;
        }
        public Integer getIdCepage() { return idCepage; }
        public void setIdCepage(Integer v) { this.idCepage = v; }
        public Integer getRefPaysRegionVin() { return refPaysRegionVin; }
        public void setRefPaysRegionVin(Integer v) { this.refPaysRegionVin = v; }
        public Integer getRefTypeDeVin() { return refTypeDeVin; }
        public void setRefTypeDeVin(Integer v) { this.refTypeDeVin = v; }
        public String getNom() { return nom; }
        public void setNom(String v) { this.nom = v; }
    }

    // ── ConditionLivraison ────────────────────────────────────
    public static class ConditionLivraison {
        private Integer    refModeDeLivraison;
        private Long       refLibelle;
        private Integer    refLangue;
        private BigDecimal frais;

        public ConditionLivraison() {}
        public ConditionLivraison(Integer refModeDeLivraison, Long refLibelle,
                                  Integer refLangue, BigDecimal frais) {
            this.refModeDeLivraison = refModeDeLivraison;
            this.refLibelle         = refLibelle;
            this.refLangue          = refLangue;
            this.frais              = frais;
        }
        public Integer getRefModeDeLivraison() { return refModeDeLivraison; }
        public void setRefModeDeLivraison(Integer v) { this.refModeDeLivraison = v; }
        public Long getRefLibelle() { return refLibelle; }
        public void setRefLibelle(Long v) { this.refLibelle = v; }
        public Integer getRefLangue() { return refLangue; }
        public void setRefLangue(Integer v) { this.refLangue = v; }
        public BigDecimal getFrais() { return frais; }
        public void setFrais(BigDecimal v) { this.frais = v; }
    }

    // ── ConditionPayement ─────────────────────────────────────
    public static class ConditionPayement {
        private Integer refConditionPayement;
        private Long    refLibelle;
        private Integer refLangue;

        public ConditionPayement() {}
        public ConditionPayement(Integer refConditionPayement, Long refLibelle, Integer refLangue) {
            this.refConditionPayement = refConditionPayement;
            this.refLibelle           = refLibelle;
            this.refLangue            = refLangue;
        }
        public Integer getRefConditionPayement() { return refConditionPayement; }
        public void setRefConditionPayement(Integer v) { this.refConditionPayement = v; }
        public Long getRefLibelle() { return refLibelle; }
        public void setRefLibelle(Long v) { this.refLibelle = v; }
        public Integer getRefLangue() { return refLangue; }
        public void setRefLangue(Integer v) { this.refLangue = v; }
    }

    // ── BoiteDeVitesse ────────────────────────────────────────
    public static class BoiteDeVitesse {
        private Integer refBoiteDeVitesse;
        private Long    refLibelle;
        private Integer refLangue;

        public BoiteDeVitesse() {}
        public BoiteDeVitesse(Integer refBoiteDeVitesse, Long refLibelle, Integer refLangue) {
            this.refBoiteDeVitesse = refBoiteDeVitesse;
            this.refLibelle        = refLibelle;
            this.refLangue         = refLangue;
        }
        public Integer getRefBoiteDeVitesse() { return refBoiteDeVitesse; }
        public void setRefBoiteDeVitesse(Integer v) { this.refBoiteDeVitesse = v; }
        public Long getRefLibelle() { return refLibelle; }
        public void setRefLibelle(Long v) { this.refLibelle = v; }
        public Integer getRefLangue() { return refLangue; }
        public void setRefLangue(Integer v) { this.refLangue = v; }
    }

    // ── TypeEssence ───────────────────────────────────────────
    public static class TypeEssence {
        private Integer refTypeEssence;
        private Long    refLibelle;
        private Integer refLangue;

        public TypeEssence() {}
        public TypeEssence(Integer refTypeEssence, Long refLibelle, Integer refLangue) {
            this.refTypeEssence = refTypeEssence;
            this.refLibelle     = refLibelle;
            this.refLangue      = refLangue;
        }
        public Integer getRefTypeEssence() { return refTypeEssence; }
        public void setRefTypeEssence(Integer v) { this.refTypeEssence = v; }
        public Long getRefLibelle() { return refLibelle; }
        public void setRefLibelle(Long v) { this.refLibelle = v; }
        public Integer getRefLangue() { return refLangue; }
        public void setRefLangue(Integer v) { this.refLangue = v; }
    }

    // ── Genre ─────────────────────────────────────────────────
    public static class Genre {
        private Integer idGenre;
        private String  genre;

        public Genre() {}
        public Genre(Integer idGenre, String genre) {
            this.idGenre = idGenre;
            this.genre   = genre;
        }
        public Integer getIdGenre() { return idGenre; }
        public void setIdGenre(Integer v) { this.idGenre = v; }
        public String getGenre() { return genre; }
        public void setGenre(String v) { this.genre = v; }
    }

    // ── Depot ─────────────────────────────────────────────────
    public static class Depot {
        private Long    idDepot;
        private String  nom;
        private String  adresse;
        private String  ville;
        private Integer npa;
        private Integer refPays;
        private String  telephone;
        private String  email;
        private Long    refResponsable;

        public Depot() {}
        public Depot(Long idDepot, String nom, String adresse, String ville,
                     Integer npa, Integer refPays, String telephone,
                     String email, Long refResponsable) {
            this.idDepot        = idDepot;
            this.nom            = nom;
            this.adresse        = adresse;
            this.ville          = ville;
            this.npa            = npa;
            this.refPays        = refPays;
            this.telephone      = telephone;
            this.email          = email;
            this.refResponsable = refResponsable;
        }
        public Long getIdDepot() { return idDepot; }
        public void setIdDepot(Long v) { this.idDepot = v; }
        public String getNom() { return nom; }
        public void setNom(String v) { this.nom = v; }
        public String getAdresse() { return adresse; }
        public void setAdresse(String v) { this.adresse = v; }
        public String getVille() { return ville; }
        public void setVille(String v) { this.ville = v; }
        public Integer getNpa() { return npa; }
        public void setNpa(Integer v) { this.npa = v; }
        public Integer getRefPays() { return refPays; }
        public void setRefPays(Integer v) { this.refPays = v; }
        public String getTelephone() { return telephone; }
        public void setTelephone(String v) { this.telephone = v; }
        public String getEmail() { return email; }
        public void setEmail(String v) { this.email = v; }
        public Long getRefResponsable() { return refResponsable; }
        public void setRefResponsable(Long v) { this.refResponsable = v; }
    }
}
