package com.example.recordz.ui;

import com.example.recordz.integration.video.ArticleVideoBroadcaster;
import com.example.recordz.integration.video.VideoTokenRegistry;
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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

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

    private static final Set<Integer> CATEGORIES_REQUIRANT_VIDEO = Set.of(1, 2, 35);

    private VerticalLayout waitingState;
    private HorizontalLayout confirmedState;

    private final VerticalLayout videoPanel;
    
    public ArticleFormView(ArticleDynamicDataService dataService,
                           ArticleSubmitService submitService,
                           ArticleVideoBroadcaster videoBroadcaster,
                           VideoTokenRegistry tokenRegistry) {

        this.videoBroadcaster = videoBroadcaster;
        tokenRegistry.issue(videoSessionToken);
        this.videoPanel = buildVideoCapturePanel();
        this.videoPanel.setVisible(false); // masqué tant qu'aucune catégorie pertinente n'est choisie

        String lang = resolveLang();

        setPadding(true);
        setSpacing(true);

        removeAll();
        add((buildSecondaryNav()));

        DynamicArticleForm form = new DynamicArticleForm(dataService, lang);
        form.setOnCategoryChanged(catId ->
                videoPanel.setVisible(catId != null && CATEGORIES_REQUIRANT_VIDEO.contains(catId)));
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

                // Synchronisation avec le flux vidéo Redis/mémoire (voir
                // videoBroadcaster.register() plus bas dans le constructeur) :
                // si une vidéo a été reçue avant que le vendeur ne clique sur
                // "Publier", son chemin temporaire est transmis ici. Champ
                // public simple, pas de setter — receivedVideoPath reste null
                // si aucune vidéo n'a été envoyée (cas normal, vidéo optionnelle).
                data.videoPath = receivedVideoPath;

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
        add(videoPanel);
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
     * Panneau de type "Vérifiez sur votre téléphone" (comme la connexion
     * Google par QR code) : carte centrée, QR sur fond blanc, indicateur
     * d'attente animé, puis bascule vers une confirmation verte une fois la
     * vidéo reçue via videoBroadcaster.
     */
    private VerticalLayout buildVideoCapturePanel() {
        VerticalLayout panel = new VerticalLayout();
        panel.setPadding(true);
        panel.setSpacing(false);
        panel.setWidth("340px");
        panel.setAlignItems(Alignment.CENTER);
        panel.getStyle()
                .set("border", "1px solid var(--lumo-contrast-20pct)")
                .set("border-radius", "var(--lumo-border-radius-l)")
                .set("margin-bottom", "16px")
                .set("box-shadow", "0 1px 4px rgba(0,0,0,0.08)")
                .set("text-align", "center");

        Icon phoneIcon = VaadinIcon.MOBILE_RETRO.create();
        phoneIcon.setSize("32px");
        phoneIcon.getStyle().set("color", "#6100C1").set("margin-bottom", "8px");
        panel.add(phoneIcon);

        Span title = new Span("Confirmez depuis votre téléphone");
        title.getStyle().set("font-weight", "600").set("font-size", "1rem").set("margin-bottom", "4px");
        panel.add(title);

        Span subtitle = new Span("Scannez ce code avec l'appareil photo de votre téléphone pour filmer l'article.");
        subtitle.getStyle()
                .set("font-size", "0.82rem")
                .set("color", "var(--lumo-secondary-text-color)")
                .set("margin-bottom", "16px")
                .set("max-width", "280px");
        panel.add(subtitle);

        // QR sur fond blanc avec léger cadre, comme la boîte QR de Google.
        Div qrBox = new Div();
        qrBox.getStyle()
                .set("background", "white")
                .set("padding", "12px")
                .set("border-radius", "12px")
                .set("border", "1px solid var(--lumo-contrast-10pct)");
        Image qr = new Image(buildQrResource(buildMobileUploadUrl()), "QR code capture vidéo");
        qr.setWidth(QR_SIZE_PX + "px");
        qr.setHeight(QR_SIZE_PX + "px");
        qrBox.add(qr);
        panel.add(qrBox);

        // État "en attente" : petite barre de progression indéterminée
        // (spinner) + texte — visible par défaut.
        waitingState = new VerticalLayout();
        waitingState.setPadding(false);
        waitingState.setSpacing(false);
        waitingState.setAlignItems(Alignment.CENTER);
        waitingState.getStyle().set("margin-top", "16px").set("width", "100%");

        ProgressBar spinner = new ProgressBar();
        spinner.setIndeterminate(true);
        spinner.setWidth("160px");
        waitingState.add(spinner);

        Span waitingLabel = new Span("En attente de confirmation…");
        waitingLabel.getStyle().set("font-size", "0.82rem").set("color", "var(--lumo-secondary-text-color)")
                .set("margin-top", "6px");
        waitingState.add(waitingLabel);

        panel.add(waitingState);

        // État "confirmé" : coche verte + texte — masqué par défaut, affiché
        // à la réception de la vidéo (onVideoReceived), le QR et le spinner
        // disparaissant à ce moment-là (même logique que Google qui replace
        // le QR par une confirmation une fois le téléphone approuvé).
        Icon checkIcon = VaadinIcon.CHECK_CIRCLE.create();
        checkIcon.setSize("22px");
        checkIcon.getStyle().set("color", "#1a7f37");
        Span confirmedLabel = new Span("Vidéo confirmée");
        confirmedLabel.getStyle().set("font-weight", "600").set("color", "#1a7f37");
        confirmedState = new HorizontalLayout(checkIcon, confirmedLabel);
        confirmedState.setAlignItems(Alignment.CENTER);
        confirmedState.setSpacing(true);
        confirmedState.getStyle().set("margin-top", "16px");
        confirmedState.setVisible(false);
        panel.add(confirmedState);

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
