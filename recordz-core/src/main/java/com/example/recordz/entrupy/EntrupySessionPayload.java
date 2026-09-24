package com.example.recordz.entrupy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EntrupySessionPayload(
        @JsonProperty("entrupy_id") String entrupyId,
        @JsonProperty("customer_item_id") String customerItemId,
        @JsonProperty("certificate_url") String certificateUrl,
        EntrupyStatus status
) {}