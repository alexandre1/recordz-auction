package com.example.recordz.model.domain;

import java.time.LocalDate;

public class Personne {

    private Long idPersonne;
    private String nomUtilisateur;
    private String motDePasse;
    private String nom;
    private String prenom;
    private String adresse;
    private Integer npa;
    private String ville;
    private String pays;
    private String email;
    private String noTelephone;
    private Integer active;
    private Integer level;
    private String refHost;
    private String lang;
    private String banque;
    private String noCompte;
    private String iban;
    private String msnMessenger;
    private String skypeName;
    private String dateStart;
    private LocalDate dateExpiration;
    private Integer refCanton;
    private String paypalMe;

    public Personne() {}

    // ── Getters & Setters ─────────────────────────────────────

    public Long getIdPersonne() { return idPersonne; }
    public void setIdPersonne(Long idPersonne) { this.idPersonne = idPersonne; }

    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public Integer getNpa() { return npa; }
    public void setNpa(Integer npa) { this.npa = npa; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNoTelephone() { return noTelephone; }
    public void setNoTelephone(String noTelephone) { this.noTelephone = noTelephone; }

    public Integer getActive() { return active; }
    public void setActive(Integer active) { this.active = active; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public String getRefHost() { return refHost; }
    public void setRefHost(String refHost) { this.refHost = refHost; }

    public String getLang() { return lang; }
    public void setLang(String lang) { this.lang = lang; }

    public String getBanque() { return banque; }
    public void setBanque(String banque) { this.banque = banque; }

    public String getNoCompte() { return noCompte; }
    public void setNoCompte(String noCompte) { this.noCompte = noCompte; }

    public String getIban() { return iban; }
    public void setIban(String iban) { this.iban = iban; }

    public String getDateStart() { return dateStart; }
    public void setDateStart(String dateStart) { this.dateStart = dateStart; }

    public LocalDate getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDate dateExpiration) { this.dateExpiration = dateExpiration; }

    public Integer getRefCanton() { return refCanton; }
    public void setRefCanton(Integer refCanton) { this.refCanton = refCanton; }

    public String getPaypalMe() { return paypalMe; }
    public void setPaypalMe(String paypalMe) { this.paypalMe = paypalMe; }
}
