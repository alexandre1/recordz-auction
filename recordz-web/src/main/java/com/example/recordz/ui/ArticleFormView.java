package com.example.recordz.ui;

import com.example.recordz.integration.video.ArticleVideoBroadcaster;
import com.example.recordz.integration.video.VideoUploadController;
import com.example.recordz.model.domain.dto.ArticleFormData;
import com.example.recordz.model.domain.dto.ArticleSaveResult;
import com.example.recordz.service.ArticleDynamicDataService;
import com.example.recordz.service.ArticleSubmitService;
import com.example.recordz.ui.layouts.MainLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@PermitAll
@Route(value = "ajouter-article", layout = MainLayout.class)
@PageTitle("Ajouter un article")
public class ArticleFormView extends VerticalLayout {

    private static final int QR_SIZE_PX = 180;

    private final ArticleVideoBroadcaster videoBroadcaster;

    // Token de session pour corréler la vidéo mobile à CE formulaire ouvert,
    // avant même que l'article n'existe en base (donc pas de customer_item_id
    // Entrupy disponible à ce stade).
    private final String videoSessionToken = VideoUploadController.newSessionToken();
    private String receivedVideoPath; // rempli quand la vidéo arrive

    private final Span videoStatusLabel = new Span();
    private ArticleVideoBroadcaster.Registration videoSubscription;

    public ArticleFormView(ArticleDynamicDataService dataService,
                           ArticleSubmitService submitService,
                           ArticleVideoBroadcaster videoBroadcaster) {

        this.videoBroadcaster = videoBroadcaster;

        String lang = resolveLang();

        setPadding(true);
        setSpacing(true);

        removeAll();
        add((buildSecondaryNav()));

        DynamicArticleForm form = new DynamicArticleForm(dataService, lang);
        form.getStyle()
                .set("border-radius", "20px")
                .set("border", "1px solid #6100C1")
                .set("overflow", "hidden")
                .set("width", "600px")
                .set("margin-bottom", "24px");

        Button saveButton   = new Button("Publier l'article");
        Button cancelButton = new Button("Annuler");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickListener(e -> {
            // 1. Validation
            if (!form.isValid()) {
                Notification.show("Veuillez corriger les erreurs avant de publier.",
                        3000, Notification.Position.TOP_CENTER);
                return;
            }

            // 2. Collecte + persistance
            try {

                ArticleFormData data = form.collectValues();

                // TODO : ajouter un champ videoPath (ou équivalent) à
                // ArticleFormData, ainsi qu'une colonne correspondante sur la
                // table article, pour qu'ArticleSubmitService.save() déplace
                // ce fichier temporaire vers le stockage définitif et
                // l'associe à l'id_article généré. Non fait ici faute de
                // visibilité sur ArticleFormData / le schéma exact.
                // data.setVideoPath(receivedVideoPath);


                ArticleSaveResult result = submitService.save(data);


                String message = result.pendingEntrupyAuthentication()
                        ? "Article #" + result.articleId() + " enregistré — en attente de vérification d'authenticité."
                        : "Article #" + result.articleId() + " publié avec succès !";

                Notification notif = Notification.show(
                        message, 4000, Notification.Position.TOP_CENTER);
                notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                // Redirection vers la fiche de l'article
                saveButton.getUI().ifPresent(ui -> ui.navigate("detail/" + result.articleId()));

            } catch (Exception ex) {
                // Afficher la cause racine
                Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                Notification notif = Notification.show(
                        "Erreur : " + cause.getMessage(),
                        8000, Notification.Position.TOP_CENTER);
                notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        cancelButton.addClickListener(e ->
                cancelButton.getUI().ifPresent(ui -> ui.navigate(""))
        );

        add(form, new HorizontalLayout(saveButton, cancelButton));
        add(buildVideoCapturePanel());
        addAttachListener(ev -> videoSubscription = videoBroadcaster.register(
                videoSessionToken, UI.getCurrent(), this::onVideoReceived));
        addDetachListener(ev -> {
            if (videoSubscription != null) {
                videoSubscription.remove();
                videoSubscription = null;
            }
        });
    }

    /**
     * Panneau QR affiché en haut du formulaire : le vendeur scanne pour
     * ouvrir la page mobile de capture vidéo (à construire séparément,
     * elle POST le fichier sur /api/article-video/{token}).
     */
    private VerticalLayout buildVideoCapturePanel() {
        VerticalLayout panel = new VerticalLayout();
        panel.setPadding(true);
        panel.setSpacing(true);
        panel.setWidth("310px");
        panel.getStyle()
                .set("border", "1px solid var(--lumo-contrast-20pct)")
                .set("border-radius", "var(--lumo-border-radius-l)")
                .set("margin-bottom", "16px");

        panel.add(new Span("Vidéo de l'article (optionnel)"));

        Image qr = new Image(buildQrResource(buildMobileUploadUrl()), "QR code capture vidéo");
        qr.setWidth(QR_SIZE_PX + "px");
        qr.setHeight(QR_SIZE_PX + "px");
        panel.add(qr);

        panel.add(new Span("Scannez pour filmer l'article depuis votre téléphone."));

        videoStatusLabel.setText("En attente d'une vidéo…");
        panel.add(videoStatusLabel);

        return panel;
    }

    private void onVideoReceived(String videoPath) {
        this.receivedVideoPath = videoPath;
        videoStatusLabel.setText("Vidéo reçue ✔");
    }

    /**
     * URL absolue de la page mobile, construite à partir de la requête
     * courante (fonctionne aussi bien en local qu'en ngrok, tant que
     * server.forward-headers-strategy=framework est actif — voir la
     * discussion précédente sur le même souci pour l'OAuth).
     *
     * TODO : /mobile/video-capture/{token} est une page à créer (formulaire
     * HTML simple avec <input type="file" accept="video/*" capture>, ou
     * MediaRecorder pour un enregistrement direct dans le navigateur) — non
     * fournie ici, je peux vous la préparer séparément.
     */
    private String buildMobileUploadUrl() {
        VaadinRequest request = VaadinRequest.getCurrent();
        String scheme = request.isSecure() ? "https" : "http";
        String host = request.getHeader("Host") != null ? request.getHeader("Host") : "localhost";
        return scheme + "://" + host + "/mobile/video-capture/" + videoSessionToken;
    }

    private StreamResource buildQrResource(String content) {
        return new StreamResource("article-video-qr.png", () -> {
            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, QR_SIZE_PX, QR_SIZE_PX);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                MatrixToImageWriter.writeToStream(matrix, "PNG", out);
                return new ByteArrayInputStream(out.toByteArray());
            } catch (WriterException | IOException e) {
                throw new RuntimeException("Erreur génération QR code vidéo", e);
            }
        });
    }

    private static String resolveLang() {
        try {
            VaadinSession session = VaadinSession.getCurrent();
            if (session != null) {
                String lang = (String) session.getAttribute("lang");
                if (lang != null && !lang.isBlank()) return lang.toLowerCase();
            }
        } catch (Exception ignored) {}
        return "fr";
    }

    private HorizontalLayout buildSecondaryNav() {
        HorizontalLayout nav = new HorizontalLayout();
        nav.getStyle()
                .set("color", "#0000CC")
                .set("font-size", "0.85rem");
        nav.add(
                navLink("Informations",       "/mon-compte/informations"),
                navLink("Mes encheres",       "/encheres"),
                navLink("Ajouter un article", "/ajouter-article"),
                navLink("Faire de la publicité", "/publicite"),
                navLink("Calendrier de mes ventes", "/calendrier")
        );
        return nav;
    }

    private Anchor navLink(String text, String href) {
        Anchor a = new Anchor(href, text);
        a.getStyle()
                .set("color", "#0000CC")
                .set("font-size", "0.85rem")
                .set("text-decoration", "none");
        return a;
    }

}
