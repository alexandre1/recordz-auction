package com.example.recordz.model.domain;

import java.time.LocalDateTime;

public class Commentaire {

    private Long idCommentaire;
    private Long refArticle;
    private Long refEmetteur;
    private String question;
    private String texte;
    private LocalDateTime date;

    public Commentaire() {}

    public Long getIdCommentaire() { return idCommentaire; }
    public void setIdCommentaire(Long idCommentaire) { this.idCommentaire = idCommentaire; }

    public Long getRefArticle() { return refArticle; }
    public void setRefArticle(Long refArticle) { this.refArticle = refArticle; }

    public Long getRefEmetteur() { return refEmetteur; }
    public void setRefEmetteur(Long refEmetteur) { this.refEmetteur = refEmetteur; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
