package com.example.recordz.web.webhook;

import com.example.recordz.entrupy.EntrupyWebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks/entrupy")
public class EntrupyWebhookController {

    private final EntrupyWebhookService webhookService;

    public EntrupyWebhookController(EntrupyWebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping
    public ResponseEntity<Void> handleWebhook(
            @RequestHeader("Entrupy-Signature") String signature,
            @RequestBody String rawPayload) {

        if (!webhookService.isSignatureValid(rawPayload, signature)) {
            return ResponseEntity.status(401).build();
        }

        webhookService.processEvent(rawPayload);
        return ResponseEntity.ok().build();
    }
}