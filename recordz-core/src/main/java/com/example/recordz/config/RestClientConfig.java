package com.example.recordz.config;

import com.example.recordz.config.EntrupyProperties; // adapte au package réel dans recordz-core
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(EntrupyProperties.class)
public class RestClientConfig {

    @Bean
    public RestClient entrupyClient(EntrupyProperties props) {
        return RestClient.builder()
                .baseUrl(props.baseUrl())
                .defaultHeader("Authorization", "Token " + props.token())
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}