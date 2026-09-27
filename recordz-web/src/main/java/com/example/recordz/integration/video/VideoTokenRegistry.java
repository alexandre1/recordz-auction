package com.example.recordz.integration.video;

/**
 * Gère la durée de vie et l'usage unique du token de session vidéo, pour se
 * rapprocher du niveau de sécurité d'une connexion Google par QR code :
 *  - le lien expire après un délai (voir app.video.token-ttl-minutes)
 *  - une fois une vidéo effectivement reçue, le token est consommé et ne
 *    peut plus servir pour un second upload
 *
 * Deux implémentations, même principe que les broadcasters : InMemory (dev,
 * mono-pod) et Redis (prod multi-pods, le pod qui sert la page mobile n'est
 * pas forcément celui qui reçoit l'upload).
 */
public interface VideoTokenRegistry {

    /** Appelé par ArticleFormView à la génération du token. */
    void issue(String token);

    /** Vérifié par MobileVideoCaptureController (avant d'afficher la page)
     *  et par VideoUploadController (avant d'accepter le fichier). */
    boolean isValid(String token);

    /** Appelé par VideoUploadController une fois la vidéo effectivement reçue. */
    void consume(String token);
}
