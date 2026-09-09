package com.example.recordz.model.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Enchere {

    private Long idEnchere;
    private Long refArticle;
    private Long refEnchereur;
    private BigDecimal prix;
    private LocalDateTime dateEnchere;

    public Enchere() {}

    public Long getIdEnchere() { return idEnchere; }
    public void setIdEnchere(Long idEnchere) { this.idEnchere = idEnchere; }

    public Long getRefArticle() { return refArticle; }
    public void setRefArticle(Long refArticle) { this.refArticle = refArticle; }

    public Long getRefEnchereur() { return refEnchereur; }
    public void setRefEnchereur(Long refEnchereur) { this.refEnchereur = refEnchereur; }

    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }

    public LocalDateTime getDateEnchere() { return dateEnchere; }
    public void setDateEnchere(LocalDateTime dateEnchere) { this.dateEnchere = dateEnchere; }
}
