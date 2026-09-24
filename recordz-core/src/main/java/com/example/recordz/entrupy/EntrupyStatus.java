package com.example.recordz.entrupy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EntrupyStatus(
        String result,   // ex. "authentic", "not_authentic", "inconclusive"
        Boolean flag
) {}