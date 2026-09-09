package com.example.recordz.model.domain;

import java.time.LocalDate;

public class Commande {

    private Long idCommande;
    private String sessionId;
    private Long clientRef;
    private LocalDate date;
    private Integer refModeDePayement;
    private LocalDate datePayement;
    private Long refVendeur;
    private Integer refStatut;
    private Integer refModeDeLivraison;

    public Commande() {}

    public Long getIdCommande() { return idCommande; }
    public void setIdCommande(Long idCommande) { this.idCommande = idCommande; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public Long getClientRef() { return clientRef; }
    public void setClientRef(Long clientRef) { this.clientRef = clientRef; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getRefModeDePayement() { return refModeDePayement; }
    public void setRefModeDePayement(Integer refModeDePayement) { this.refModeDePayement = refModeDePayement; }

    public LocalDate getDatePayement() { return datePayement; }
    public void setDatePayement(LocalDate datePayement) { this.datePayement = datePayement; }

    public Long getRefVendeur() { return refVendeur; }
    public void setRefVendeur(Long refVendeur) { this.refVendeur = refVendeur; }

    public Integer getRefStatut() { return refStatut; }
    public void setRefStatut(Integer refStatut) { this.refStatut = refStatut; }

    public Integer getRefModeDeLivraison() { return refModeDeLivraison; }
    public void setRefModeDeLivraison(Integer refModeDeLivraison) { this.refModeDeLivraison = refModeDeLivraison; }
}
