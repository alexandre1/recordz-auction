package com.example.recordz.model.domain.dto;

public record ArticleSaveResult(
        int articleId,
        boolean pendingEntrupyAuthentication
) {}