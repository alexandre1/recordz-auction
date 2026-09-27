package com.example.recordz.integration.video;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnProperty(prefix = "app.video.broadcaster", name = "type", havingValue = "memory", matchIfMissing = true)
public class InMemoryVideoTokenRegistry implements VideoTokenRegistry {

    private final Map<String, Instant> expiryByToken = new ConcurrentHashMap<>();
    private final long ttlMinutes;

    public InMemoryVideoTokenRegistry(@Value("${app.video.token-ttl-minutes:15}") long ttlMinutes) {
        this.ttlMinutes = ttlMinutes;
    }

    @Override
    public void issue(String token) {
        expiryByToken.put(token, Instant.now().plusSeconds(ttlMinutes * 60));
    }

    @Override
    public boolean isValid(String token) {
        Instant expiry = expiryByToken.get(token);
        if (expiry == null) return false;
        if (Instant.now().isAfter(expiry)) {
            expiryByToken.remove(token); // nettoyage paresseux des tokens expirés
            return false;
        }
        return true;
    }

    @Override
    public void consume(String token) {
        expiryByToken.remove(token); // usage unique : plus valide dès qu'un upload a réussi
    }
}
