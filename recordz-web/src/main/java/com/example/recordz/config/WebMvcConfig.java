package com.example.recordz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sert le répertoire physique d'upload sous l'URL /images/articles/**.
 *
 * Sans cette config, Vaadin ne trouve pas les fichiers écrits à runtime
 * dans META-INF/resources car ils ne font pas partie du classpath compilé.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Normalise le chemin : file:E:/recordz/.../articles/
        String location = uploadDir.endsWith("/") || uploadDir.endsWith("\\")
            ? "file:" + uploadDir
            : "file:" + uploadDir + "/";

        registry.addResourceHandler("/images/articles/**")
                .addResourceLocations(location)
                .setCachePeriod(3600); // cache 1h côté navigateur
    }
}
