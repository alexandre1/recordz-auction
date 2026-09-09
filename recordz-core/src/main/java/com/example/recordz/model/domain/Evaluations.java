package com.example.recordz.model.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Evaluations {

    // ── EvaluationAchat ───────────────────────────────────────

    public static class EvaluationAchat {
        private Long idEvaluationAchat;
        private Long refVendeur;
        private Long refAcheteur;
        private Long refArticle;
        private Byte note;
        private String commentaire;
        private LocalDateTime date;

        public Long getIdEvaluationAchat() { return idEvaluationAchat; }
        public void setIdEvaluationAchat(Long v) { this.idEvaluationAchat = v; }
        public Long getRefVendeur() { return refVendeur; }
        public void setRefVendeur(Long v) { this.refVendeur = v; }
        public Long getRefAcheteur() { return refAcheteur; }
        public void setRefAcheteur(Long v) { this.refAcheteur = v; }
        public Long getRefArticle() { return refArticle; }
        public void setRefArticle(Long v) { this.refArticle = v; }
        public Byte getNote() { return note; }
        public void setNote(Byte v) { this.note = v; }
        public String getCommentaire() { return commentaire; }
        public void setCommentaire(String v) { this.commentaire = v; }
        public LocalDateTime getDate() { return date; }
        public void setDate(LocalDateTime v) { this.date = v; }
    }

    // ── EvaluationVente ───────────────────────────────────────

    public static class EvaluationVente {
        private Long idEvaluationVente;
        private Long refVendeur;
        private Long refAcheteur;
        private Long refArticle;
        private Byte note;
        private String commentaire;
        private LocalDateTime date;

        public Long getIdEvaluationVente() { return idEvaluationVente; }
        public void setIdEvaluationVente(Long v) { this.idEvaluationVente = v; }
        public Long getRefVendeur() { return refVendeur; }
        public void setRefVendeur(Long v) { this.refVendeur = v; }
        public Long getRefAcheteur() { return refAcheteur; }
        public void setRefAcheteur(Long v) { this.refAcheteur = v; }
        public Long getRefArticle() { return refArticle; }
        public void setRefArticle(Long v) { this.refArticle = v; }
        public Byte getNote() { return note; }
        public void setNote(Byte v) { this.note = v; }
        public String getCommentaire() { return commentaire; }
        public void setCommentaire(String v) { this.commentaire = v; }
        public LocalDateTime getDate() { return date; }
        public void setDate(LocalDateTime v) { this.date = v; }
    }

    // ── EvaluationArticle ─────────────────────────────────────

    public static class EvaluationArticle {
        private Long idEvaluationArticle;
        private Long refArticle;
        private Long refVendeur;
        private Long refAcheteur;
        private Integer note;
        private LocalDate date;

        public Long getIdEvaluationArticle() { return idEvaluationArticle; }
        public void setIdEvaluationArticle(Long v) { this.idEvaluationArticle = v; }
        public Long getRefArticle() { return refArticle; }
        public void setRefArticle(Long v) { this.refArticle = v; }
        public Long getRefVendeur() { return refVendeur; }
        public void setRefVendeur(Long v) { this.refVendeur = v; }
        public Long getRefAcheteur() { return refAcheteur; }
        public void setRefAcheteur(Long v) { this.refAcheteur = v; }
        public Integer getNote() { return note; }
        public void setNote(Integer v) { this.note = v; }
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate v) { this.date = v; }
    }
}
