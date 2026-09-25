package com.example.recordz.service;

import com.example.recordz.model.redis.PreAnalysisSession;
import com.example.recordz.model.redis.PreAnalysisSession.MediaCheck;
import com.example.recordz.model.redis.PreAnalysisSession.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
public class PreAnalysisService {

    private static final String KEY_PREFIX = "precheck:";

    /** Angles/vidéos requis avant de pouvoir soumettre à Entrupy — à ajuster par catégorie si besoin. */
    private static final Set<String> ANGLES_REQUIS = Set.of("face", "logo", "numero-serie", "video-360");

    private final RedisTemplate<String, PreAnalysisSession> redisTemplate;
    private final Duration sessionTtl;

    public PreAnalysisService(RedisTemplate<String, PreAnalysisSession> redisTemplate,
                              @Value("${entrupy.precheck.ttl-minutes:120}") long ttlMinutes) {
        this.redisTemplate = redisTemplate;
        this.sessionTtl = Duration.ofMinutes(ttlMinutes);
    }

    public PreAnalysisSession creerSession(String draftArticleId, String customerItemId) {
        PreAnalysisSession session = new PreAnalysisSession();
        session.setDraftArticleId(draftArticleId);
        session.setCustomerItemId(customerItemId);
        sauvegarder(session);
        return session;
    }

    public PreAnalysisSession getSession(String draftArticleId) {
        return redisTemplate.opsForValue().get(key(draftArticleId));
    }

    /**
     * Ajoute un média capturé et son résultat de contrôle qualité rapide.
     * Le contrôle qualité lui-même (flou, luminosité, cadrage) n'est PAS fait ici :
     * brancher un vrai check (lib de vision, appel à un micro-service) dans
     * ArticleMediaQualityChecker et lui passer les bytes de l'upload.
     */
    public PreAnalysisSession ajouterMedia(String draftArticleId, String fileName, String contentType,
                                           String angle, boolean qualityOk, String rejectReason) {
        PreAnalysisSession session = getSession(draftArticleId);
        if (session == null) {
            throw new IllegalStateException("Aucune session de pré-analyse pour " + draftArticleId
                    + " — appeler creerSession() d'abord.");
        }
        session.getMedia().add(new MediaCheck(fileName, contentType, angle, qualityOk, rejectReason));
        session.setStatus(calculerStatut(session));
        session.setUpdatedAt(Instant.now());
        sauvegarder(session);
        return session;
    }

    public boolean estPretPourEntrupy(String draftArticleId) {
        PreAnalysisSession session = getSession(draftArticleId);
        return session != null && session.getStatus() == Status.PRET_POUR_ENTRUPY;
    }

    public void marquerSoumisEntrupy(String draftArticleId) {
        PreAnalysisSession session = getSession(draftArticleId);
        if (session != null) {
            session.setStatus(Status.SOUMIS_ENTRUPY);
            session.setUpdatedAt(Instant.now());
            sauvegarder(session);
        }
    }

    public void supprimerSession(String draftArticleId) {
        redisTemplate.delete(key(draftArticleId));
    }

    private Status calculerStatut(PreAnalysisSession session) {
        List<MediaCheck> media = session.getMedia();
        boolean tousLesAnglesOk = ANGLES_REQUIS.stream().allMatch(angle ->
                media.stream().anyMatch(m -> angle.equals(m.getAngle()) && m.isQualityOk()));
        return tousLesAnglesOk ? Status.PRET_POUR_ENTRUPY : Status.EN_COURS;
    }

    private void sauvegarder(PreAnalysisSession session) {
        redisTemplate.opsForValue().set(key(session.getDraftArticleId()), session, sessionTtl);
    }

    private String key(String draftArticleId) {
        return KEY_PREFIX + draftArticleId;
    }
}
