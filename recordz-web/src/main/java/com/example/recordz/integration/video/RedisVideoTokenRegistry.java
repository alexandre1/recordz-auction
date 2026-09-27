package com.example.recordz.integration.video;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Utilise le TTL natif de Redis plutôt qu'un nettoyage applicatif : la clé
 * expire toute seule, et un pod quelconque peut vérifier/consommer le token,
 * pas seulement celui qui l'a émis.
 */
@Component
@ConditionalOnProperty(prefix = "app.video.broadcaster", name = "type", havingValue = "redis")
public class RedisVideoTokenRegistry implements VideoTokenRegistry {

    private static final String KEY_PREFIX = "article:video:token:";

    private final StringRedisTemplate redisTemplate;
    private final long ttlMinutes;

    public RedisVideoTokenRegistry(StringRedisTemplate redisTemplate,
                                   @Value("${app.video.token-ttl-minutes:15}") long ttlMinutes) {
        this.redisTemplate = redisTemplate;
        this.ttlMinutes = ttlMinutes;
    }

    @Override
    public void issue(String token) {
        redisTemplate.opsForValue().set(KEY_PREFIX + token, "1", Duration.ofMinutes(ttlMinutes));
    }

    @Override
    public boolean isValid(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(KEY_PREFIX + token));
    }

    @Override
    public void consume(String token) {
        redisTemplate.delete(KEY_PREFIX + token); // usage unique
    }
}
