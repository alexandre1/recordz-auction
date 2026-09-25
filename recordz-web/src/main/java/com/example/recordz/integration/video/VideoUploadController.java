package com.example.recordz.integration.video;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Reçoit la vidéo capturée sur le mobile du vendeur (page mobile ouverte via
 * le QR code affiché par ArticleFormView), la stocke, puis notifie le
 * formulaire — resté ouvert côté desktop — via ArticleVideoBroadcaster.
 *
 * Endpoint volontairement non authentifié (le mobile n'est pas connecté au
 * compte du vendeur) : la sécurité repose sur l'imprévisibilité du token
 * (UUID), généré côté serveur et jamais réutilisable après consommation.
 * Pour aller plus loin, on pourrait ajouter une expiration (ex. 15 minutes)
 * et invalider le token après le premier upload réussi — non fait ici.
 */
@RestController
@RequestMapping("/api/article-video")
public class VideoUploadController {

    private final ArticleVideoBroadcaster broadcaster;
    private final Path videoTempDir;

    // N'accepte que des tokens de la forme d'un UUID, pour éviter qu'un chemin
    // de fichier arbitraire soit injecté via l'URL.
    private static final Pattern TOKEN_PATTERN =
            Pattern.compile("^[0-9a-fA-F-]{36}$");

    public VideoUploadController(ArticleVideoBroadcaster broadcaster,
                                 @Value("${app.video-temp.dir}") String videoTempDirPath) {
        this.broadcaster = broadcaster;
        this.videoTempDir = Paths.get(videoTempDirPath);
    }

    @PostMapping("/{sessionToken}")
    public ResponseEntity<Void> upload(@PathVariable String sessionToken,
                                       @RequestParam("file") MultipartFile file) {

        if (!TOKEN_PATTERN.matcher(sessionToken).matches()) {
            return ResponseEntity.badRequest().build();
        }
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Files.createDirectories(videoTempDir);

            String ext = resolveExtension(file.getOriginalFilename());
            String filename = sessionToken + ext;
            Path dest = videoTempDir.resolve(filename);

            try (var in = file.getInputStream()) {
                Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
            }

            // Chemin transmis à la vue puis, à la publication, à
            // ArticleSubmitService pour être déplacé/rattaché à l'article
            // définitif. À ce stade ce n'est qu'un fichier temporaire.
            broadcaster.broadcast(sessionToken, dest.toString());

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String resolveExtension(String originalFilename) {
        if (originalFilename == null || originalFilename.isBlank()) return ".mp4";
        int dot = originalFilename.lastIndexOf('.');
        if (dot >= 0 && dot < originalFilename.length() - 1) {
            return "." + originalFilename.substring(dot + 1).toLowerCase();
        }
        return ".mp4";
    }

    /** Utilitaire pour générer un nouveau token de session côté vue. */
    public static String newSessionToken() {
        return UUID.randomUUID().toString();
    }
}
