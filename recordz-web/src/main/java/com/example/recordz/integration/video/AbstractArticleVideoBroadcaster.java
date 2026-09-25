package com.example.recordz.integration.video;

import com.vaadin.flow.component.UI;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

/**
 * Identique dans l'esprit à AbstractEntrupyStatusBroadcaster : gère le
 * registre local des composants Vaadin de CE pod en attente d'une vidéo pour
 * un token donné, et leur notification via UI.access().
 */
abstract class AbstractArticleVideoBroadcaster implements ArticleVideoBroadcaster {

    private final Map<String, Set<Consumer<String>>> localListeners = new ConcurrentHashMap<>();

    @Override
    public Registration register(String sessionToken, UI ui, Consumer<String> onVideoReady) {
        Set<Consumer<String>> subscribers =
                localListeners.computeIfAbsent(sessionToken, id -> new CopyOnWriteArraySet<>());

        Consumer<String> wrapped = videoPath -> ui.access(() -> onVideoReady.accept(videoPath));
        subscribers.add(wrapped);

        return () -> {
            Set<Consumer<String>> current = localListeners.get(sessionToken);
            if (current != null) {
                current.remove(wrapped);
                if (current.isEmpty()) localListeners.remove(sessionToken);
            }
        };
    }

    protected void dispatchLocally(String sessionToken, String videoPath) {
        Set<Consumer<String>> subscribers = localListeners.get(sessionToken);
        if (subscribers == null) return; // aucun formulaire ouvert ici pour ce token
        for (Consumer<String> subscriber : subscribers) {
            subscriber.accept(videoPath);
        }
    }
}
