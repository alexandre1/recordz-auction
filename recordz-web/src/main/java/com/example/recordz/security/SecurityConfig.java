package com.example.recordz.security;

import com.example.recordz.ui.LoginView;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                    // ✅ Headers séparé
                    .headers(headers -> headers
                            .contentSecurityPolicy(csp -> csp
                                    .policyDirectives("frame-src 'self' https://www.youtube.com https://www.youtube-nocookie.com")
                            )
                    )
                    // ✅ Ressources statiques — contexte séparé
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    "/images/**",
                                    "/favicon.ico"
                            ).permitAll()
                    )
                    // ✅ VaadinSecurityConfigurer gère le reste
                    .with(VaadinSecurityConfigurer.vaadin(), cfg -> cfg
                            .loginView("/login")
                    )
                    // ✅ OAuth2 Google
                    .oauth2Login(oauth2 -> oauth2
                            .defaultSuccessUrl("/", true)
                    )
                    .build();
        }
}
