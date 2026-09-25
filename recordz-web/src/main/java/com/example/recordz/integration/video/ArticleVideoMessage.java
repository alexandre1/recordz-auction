package com.example.recordz.integration.video;

/** Message échangé sur le canal Redis "article:video". */
record ArticleVideoMessage(String sessionToken, String videoPath) {}
