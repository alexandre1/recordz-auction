package com.example.recordz.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * Remplace EntrupyRedisConfig : ce conteneur d'écoute Redis Pub/Sub est
 * maintenant partagé par RedisEntrupyStatusBroadcaster (canal
 * "entrupy:status") et RedisArticleVideoBroadcaster (canal "article:video"),
 * chacun y ajoutant son propre listener sur son propre canal.
 *
 * @ConditionalOnBean(RedisConnectionFactory.class) : ce bean n'est créé que
 * si spring-boot-starter-data-redis est présent ET qu'une connexion Redis
 * est configurée/disponible — pas besoin de propriété dédiée en plus, et
 * un seul conteneur existe quel que soit le nombre de broadcasters actifs.
 *
 * Supprimez l'ancien fichier EntrupyRedisConfig.java : les deux classes
 * déclarent un bean RedisMessageListenerContainer, ce qui provoquerait un
 * conflit si elles étaient actives toutes les deux en même temps.
 */
@Configuration
public class RedisListenerConfig {

    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }
}
