package com.example.recordz.web;

import com.example.recordz.model.redis.PreAnalysisSession;
import com.example.recordz.service.PreAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * API consommée par l'app mobile pendant la capture, AVANT l'appel final
 * à Entrupy (qui reste géré par ArticleSubmitService côté "Publier").
 *
 * Flux typique côté mobile :
 *  1. POST /api/precheck/{draftArticleId}/init?customerItemId=...
 *  2. POST /api/precheck/{draftArticleId}/media   (une fois par photo/vidéo)
 *  3. GET  /api/precheck/{draftArticleId}          (poll ou après chaque upload)
 *  4. quand status = PRET_POUR_ENTRUPY -> déclenche le vrai submit Entrupy
 */
@RestController
@RequestMapping("/api/precheck")
public class MediaPrecheckController {

    private final PreAnalysisService preAnalysisService;

    public MediaPrecheckController(PreAnalysisService preAnalysisService) {
        this.preAnalysisService = preAnalysisService;
    }

    @PostMapping("/{draftArticleId}/init")
    public PreAnalysisSession init(@PathVariable String draftArticleId,
                                   @RequestParam String customerItemId) {
        return preAnalysisService.creerSession(draftArticleId, customerItemId);
    }

    @PostMapping(value = "/{draftArticleId}/media", consumes = "multipart/form-data")
    public ResponseEntity<PreAnalysisSession> ajouterMedia(
            @PathVariable String draftArticleId,
            @RequestParam String angle,
            @RequestParam MultipartFile fichier) throws IOException {

        // TODO: brancher ici le vrai contrôle qualité (flou/luminosité/cadrage)
        // sur fichier.getBytes(). Pour l'instant : contrôle basique taille/format.
        QualityResult qc = controleQualiteBasique(fichier);

        PreAnalysisSession session = preAnalysisService.ajouterMedia(
                draftArticleId, fichier.getOriginalFilename(), fichier.getContentType(),
                angle, qc.ok(), qc.motifRejet());

        return ResponseEntity.ok(session);
    }

    @GetMapping("/{draftArticleId}")
    public ResponseEntity<PreAnalysisSession> getStatut(@PathVariable String draftArticleId) {
        PreAnalysisSession session = preAnalysisService.getSession(draftArticleId);
        return session != null ? ResponseEntity.ok(session) : ResponseEntity.notFound().build();
    }

    private QualityResult controleQualiteBasique(MultipartFile fichier) {
        if (fichier.isEmpty()) {
            return new QualityResult(false, "Fichier vide");
        }
        if (fichier.getSize() > 20 * 1024 * 1024) {
            return new QualityResult(false, "Fichier trop volumineux (> 20 Mo)");
        }
        String type = fichier.getContentType();
        boolean typeValide = type != null && (type.startsWith("image/") || type.startsWith("video/"));
        return typeValide
                ? new QualityResult(true, null)
                : new QualityResult(false, "Format non supporté : " + type);
    }

    private record QualityResult(boolean ok, String motifRejet) {}
}
