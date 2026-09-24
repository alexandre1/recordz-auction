package com.example.recordz.entrupy;

import com.example.recordz.config.EntrupyProperties;
import com.example.recordz.service.ArticleAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

@Service
public class EntrupyWebhookService {

    private static final Logger log = LoggerFactory.getLogger(EntrupyWebhookService.class);

    private final EntrupyProperties props;
    private final ObjectMapper objectMapper;
    private final ArticleAuthenticationService articleAuthenticationService; // à adapter à ton service existant

    public EntrupyWebhookService(EntrupyProperties props,
                                 ObjectMapper objectMapper,
                                 ArticleAuthenticationService articleAuthenticationService) {
        this.props = props;
        this.objectMapper = objectMapper;
        this.articleAuthenticationService = articleAuthenticationService;
    }

    public boolean isSignatureValid(String rawPayload, String receivedSignature) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                    props.webhookSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] computed = mac.doFinal(rawPayload.getBytes(StandardCharsets.UTF_8));
            String computedHex = HexFormat.of().formatHex(computed);

            return MessageDigest.isEqual(
                    computedHex.getBytes(StandardCharsets.UTF_8),
                    receivedSignature.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("Erreur lors de la vérification de la signature Entrupy", e);
            return false;
        }
    }

    public void processEvent(String rawPayload) {
        try {
            EntrupyWebhookEvent event = objectMapper.readValue(rawPayload, EntrupyWebhookEvent.class);

            switch (event.event()) {
                case "session.completed" -> handleSessionCompleted(event.data());
                case "support.message" -> log.info("Message support Entrupy reçu, non traité pour l'instant");
                case "certificate.transfer" -> log.info("Transfert de certificat Entrupy reçu, non traité pour l'instant");
                default -> log.warn("Événement Entrupy inconnu reçu : {}", event.event());
            }
        } catch (Exception e) {
            log.error("Erreur lors du parsing du webhook Entrupy : {}", rawPayload, e);
        }
    }

    private void handleSessionCompleted(EntrupySessionPayload payload) {
        if (payload == null || payload.customerItemId() == null) {
            log.warn("Payload Entrupy incomplet, customer_item_id manquant");
            return;
        }

        boolean isAuthentic = "authentic".equalsIgnoreCase(payload.status().result());

        log.info("Résultat Entrupy pour l'article {} : {}",
                payload.customerItemId(), payload.status().result());

        articleAuthenticationService.updateAuthenticationStatus(
                payload.customerItemId(),
                payload.entrupyId(),
                payload.certificateUrl(),
                isAuthentic
        );
    }
}