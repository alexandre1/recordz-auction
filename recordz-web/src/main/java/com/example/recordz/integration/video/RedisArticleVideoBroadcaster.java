package com.example.recordz.integration.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * Pendant vidéo de RedisEntrupyStatusBroadcaster : même conteneur Redis
 * (RedisMessageListenerContainer, voir RedisListenerConfig), canal séparé
 * ("article:video") pour ne pas mélanger avec les statuts Entrupy.
 *
 * Activation :
 *   article:
 *     video:
 *       broadcaster:
 *         type: redis
 */
@Component
@ConditionalOnProperty(prefix = "article.video.broadcaster", name = "type", havingValue = "redis")
public class RedisArticleVideoBroadcaster extends AbstractArticleVideoBroadcaster {

    private static final String CHANNEL = "article:video";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisArticleVideoBroadcaster(StringRedisTemplate redisTemplate,
                                        RedisMessageListenerContainer listenerContainer,
                                        ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;

        MessageListener listener = (message, pattern) -> {
            try {
                String body = new String(message.getBody(), StandardCharsets.UTF_8);
                ArticleVideoMessage parsed = objectMapper.readValue(body, ArticleVideoMessage.class);
                dispatchLocally(parsed.sessionToken(), parsed.videoPath());
            } catch (Exception e) {
                // Un message illisible ne doit pas interrompre l'écoute des suivants.
            }
        };
        listenerContainer.addMessageListener(new MessageListenerAdapter(listener), new ChannelTopic(CHANNEL));
    }

    @Override
    public void broadcast(String sessionToken, String videoPath) {
        try {
            String payload = objectMapper.writeValueAsString(
                    new ArticleVideoMessage(sessionToken, videoPath));
            redisTemplate.convertAndSend(CHANNEL, payload);
        } catch (Exception e) {
            throw new RuntimeException("Erreur publication Redis de la vidéo article", e);
        }
    }
}
