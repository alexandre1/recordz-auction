package com.example.recordz.integration.video;

import com.vaadin.flow.component.UI;

import java.util.function.Consumer;

/**
 * Même principe que EntrupyStatusBroadcaster, mais pour la vidéo capturée sur
 * le mobile du vendeur pendant le remplissage du formulaire d'ajout d'article
 * (avant que l'article n'existe en base, donc corrélé par un token de session
 * généré côté vue — pas par un customer_item_id).
 */
public interface ArticleVideoBroadcaster {

    /** Appelé par ArticleFormView à l'attachement, pour ce token de session. */
    Registration register(String sessionToken, UI ui, Consumer<String> onVideoReady);

    /** Appelé par VideoUploadController une fois le fichier reçu et stocké. */
    void broadcast(String sessionToken, String videoPath);

    @FunctionalInterface
    interface Registration {
        void remove();
    }
}
