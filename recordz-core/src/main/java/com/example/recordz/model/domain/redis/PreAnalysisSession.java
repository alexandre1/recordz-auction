package com.example.recordz.model.redis;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * État éphémère (Redis, TTL) d'une session de capture photo/vidéo côté
 * mobile, AVANT l'appel réel à Entrupy. Permet à l'app mobile de reprendre
 * une capture interrompue et d'afficher un feedback immédiat sans taper
 * MySQL à chaque photo.
 */
public class PreAnalysisSession implements Serializable {

    public enum Status {
        EN_COURS,           // capture en cours, angles/vidéos manquants
        PRET_POUR_ENTRUPY,  // tous les médias requis sont OK
        SOUMIS_ENTRUPY,     // envoyé à Entrupy, en attente du webhook
        REJETE              // qualité insuffisante après N tentatives
    }

    private String draftArticleId;
    private String customerItemId;
    private Status status = Status.EN_COURS;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private List<MediaCheck> media = new ArrayList<>();

    public static class MediaCheck implements Serializable {
        private String fileName;
        private String contentType;     // image/jpeg, video/mp4, ...
        private String angle;           // "face", "logo", "numero-serie", "video-360", etc.
        private boolean qualityOk;
        private String rejectReason;    // null si qualityOk = true
        private Instant capturedAt = Instant.now();

        public MediaCheck() {}

        public MediaCheck(String fileName, String contentType, String angle,
                          boolean qualityOk, String rejectReason) {
            this.fileName = fileName;
            this.contentType = contentType;
            this.angle = angle;
            this.qualityOk = qualityOk;
            this.rejectReason = rejectReason;
        }

        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }
        public String getContentType() { return contentType; }
        public void setContentType(String contentType) { this.contentType = contentType; }
        public String getAngle() { return angle; }
        public void setAngle(String angle) { this.angle = angle; }
        public boolean isQualityOk() { return qualityOk; }
        public void setQualityOk(boolean qualityOk) { this.qualityOk = qualityOk; }
        public String getRejectReason() { return rejectReason; }
        public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
        public Instant getCapturedAt() { return capturedAt; }
        public void setCapturedAt(Instant capturedAt) { this.capturedAt = capturedAt; }
    }

    public String getDraftArticleId() { return draftArticleId; }
    public void setDraftArticleId(String draftArticleId) { this.draftArticleId = draftArticleId; }
    public String getCustomerItemId() { return customerItemId; }
    public void setCustomerItemId(String customerItemId) { this.customerItemId = customerItemId; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public List<MediaCheck> getMedia() { return media; }
    public void setMedia(List<MediaCheck> media) { this.media = media; }
}
