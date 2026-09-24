
package com.example.recordz.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "entrupy.api")
public record EntrupyProperties(String token, String baseUrl, String webhookSecret) {
    public EntrupyProperties {
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "https://api.entrupy.com";
        }
    }
}
