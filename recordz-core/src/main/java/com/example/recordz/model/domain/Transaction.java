package com.example.recordz.model.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private Long idTransaction;
    private Long refAcheteur;
    private Long refVendeur;
    private LocalDateTime date;
    private BigDecimal prix;
    private Integer validate;
    private Long refArticle;

    public Transaction() {}

    public Long getIdTransaction() { return idTransaction; }
    public void setIdTransaction(Long idTransaction) { this.idTransaction = idTransaction; }

    public Long getRefAcheteur() { return refAcheteur; }
    public void setRefAcheteur(Long refAcheteur) { this.refAcheteur = refAcheteur; }

    public Long getRefVendeur() { return refVendeur; }
    public void setRefVendeur(Long refVendeur) { this.refVendeur = refVendeur; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }

    public Integer getValidate() { return validate; }
    public void setValidate(Integer validate) { this.validate = validate; }

    public Long getRefArticle() { return refArticle; }
    public void setRefArticle(Long refArticle) { this.refArticle = refArticle; }
}
