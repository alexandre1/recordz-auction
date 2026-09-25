package com.example.recordz.integration.video;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/** Actif par défaut (dev local, ou déploiement mono-pod). */
@Component
@ConditionalOnProperty(prefix = "article.video.broadcaster", name = "type", havingValue = "memory", matchIfMissing = true)
public class InMemoryArticleVideoBroadcaster extends AbstractArticleVideoBroadcaster {

    @Override
    public void broadcast(String sessionToken, String videoPath) {
        dispatchLocally(sessionToken, videoPath);
    }
}
