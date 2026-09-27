package com.example.recordz.integration.video;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

/**
 * Sert la page mobile ouverte en scannant le QR code affiché par
 * ArticleFormView. Volontairement minimale : un simple <input type="file">
 * avec capture="environment", qui ouvre directement l'appareil photo natif
 * du téléphone (pas de MediaRecorder / prévisualisation live — plus simple,
 * fonctionne sur tous les navigateurs mobiles sans JS complexe).
 *
 * Le fichier choisi est envoyé par fetch() en multipart vers
 * VideoUploadController (/api/article-video/{token}), déjà en place.
 */
@RestController
public class MobileVideoCaptureController {

    private static final Pattern TOKEN_PATTERN = Pattern.compile("^[0-9a-fA-F-]{36}$");

    private final VideoTokenRegistry tokenRegistry;

    public MobileVideoCaptureController(VideoTokenRegistry tokenRegistry) {
        this.tokenRegistry = tokenRegistry;
    }

    @GetMapping(value = "/mobile/video-capture/{token}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> capturePage(@PathVariable String token) {

        if (!TOKEN_PATTERN.matcher(token).matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_HTML)
                    .body(errorPage("Lien invalide. Retournez sur l'ordinateur et scannez à nouveau le QR code."));
        }

        if (!tokenRegistry.isValid(token)) {
            return ResponseEntity.status(HttpStatus.GONE)
                    .contentType(MediaType.TEXT_HTML)
                    .body(errorPage("Ce lien a expiré ou a déjà été utilisé. "
                            + "Retournez sur l'ordinateur pour générer un nouveau QR code."));
        }

        String html = """
            <!DOCTYPE html>
            <html lang="fr">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1">
              <title>Filmer l'article</title>
              <style>
                body {
                  font-family: -apple-system, Roboto, Arial, sans-serif;
                  background: #f6f2f3;
                  margin: 0;
                  padding: 24px 16px;
                  color: #1a1a1a;
                }
                .card {
                  background: white;
                  border-radius: 16px;
                  padding: 20px;
                  max-width: 420px;
                  margin: 0 auto;
                  border: 1px solid #e0d5e6;
                }
                h1 { font-size: 1.2rem; color: #6100C1; margin-top: 0; }
                p { font-size: 0.9rem; color: #444; line-height: 1.4; }
                input[type=file] {
                  width: 100%%;
                  margin: 16px 0;
                  font-size: 0.9rem;
                }
                button {
                  width: 100%%;
                  padding: 12px;
                  font-size: 1rem;
                  border: none;
                  border-radius: 24px;
                  background: #6100C1;
                  color: white;
                  cursor: pointer;
                }
                button:disabled { background: #c9a8e6; }
                #status {
                  margin-top: 16px;
                  font-size: 0.9rem;
                  text-align: center;
                }
                .ok   { color: #1a7f37; font-weight: bold; }
                .err  { color: #cc0000; font-weight: bold; }
              </style>
            </head>
            <body>
              <div class="card">
                <h1>Filmer l'article</h1>
                <p>Filmez ou choisissez une courte vidéo de l'article, puis appuyez sur Envoyer.
                   Une fois terminé, vous pouvez fermer cette page et revenir sur votre ordinateur.</p>

                <input id="videoInput" type="file" accept="video/*" capture="environment">
                <button id="sendBtn" onclick="upload()">Envoyer</button>

                <div id="status"></div>
              </div>

              <script>
                async function upload() {
                  const input = document.getElementById('videoInput');
                  const btn = document.getElementById('sendBtn');
                  const status = document.getElementById('status');

                  if (!input.files || input.files.length === 0) {
                    status.textContent = 'Choisissez d\\'abord une vidéo.';
                    status.className = 'err';
                    return;
                  }

                  const formData = new FormData();
                  formData.append('file', input.files[0]);

                  btn.disabled = true;
                  status.textContent = 'Envoi en cours…';
                  status.className = '';

                  try {
                    const response = await fetch('/api/article-video/%s', {
                      method: 'POST',
                      body: formData
                    });

                    if (response.ok) {
                      status.textContent = 'Vidéo envoyée ! Vous pouvez fermer cette page.';
                      status.className = 'ok';
                      btn.style.display = 'none';
                      input.style.display = 'none';
                    } else {
                      status.textContent = 'Erreur lors de l\\'envoi (code ' + response.status + '). Réessayez.';
                      status.className = 'err';
                      btn.disabled = false;
                    }
                  } catch (e) {
                    status.textContent = 'Erreur réseau. Vérifiez votre connexion et réessayez.';
                    status.className = 'err';
                    btn.disabled = false;
                  }
                }
              </script>
            </body>
            </html>
            """.formatted(token);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    private String errorPage(String message) {
        return """
            <!DOCTYPE html>
            <html lang="fr">
            <head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Lien invalide</title></head>
            <body style="font-family: sans-serif; padding: 24px; text-align: center;">
              <p>%s</p>
            </body>
            </html>
            """.formatted(message);
    }
}
