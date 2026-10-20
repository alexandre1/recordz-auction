package com.example.recordz.ui;


import com.example.recordz.model.domain.*;
import com.example.recordz.model.domain.Article;
import com.example.recordz.service.CalendrierService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.Autocomplete;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.mail.SimpleMailMessage;
import com.example.recordz.service.ArticleService;
import com.example.recordz.service.PersonneService;
import com.example.recordz.service.ReferenceService;
import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.Phonenumber;
import com.vaadin.flow.component.datepicker.DatePicker;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import com.vaadin.flow.component.timepicker.TimePicker;
import org.vaadin.stefan.fullcalendar.*;

import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


@Route(value = "detail/:articleId", layout = MainLayout.class)
@PermitAll
public class DetailArticleView extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle {

    /**
     * Source des images de drapeaux.
     * - CDN : "https://flagcdn.com/w40/%s.png" (nécessite img-src https://flagcdn.com si CSP)
     * - Local : "/flags/%s.svg" avec les SVG de flag-icons placés dans
     *   src/main/resources/META-INF/resources/flags/ (ex. ch.svg, fr.svg…)
     */
    private static final String FLAG_URL_PATTERN = "https://flagcdn.com/w40/%s.png";

    private final ArticleService articleService;
    private final ReferenceService referenceService;
    private final PersonneService personneService;
    private final JavaMailSender mailSender;
    private final CalendrierService calendrierService;

    private Article article;
    private Personne personne; // vendeur de l'article (peut être null)

    public DetailArticleView(ArticleService articleService, ReferenceService referenceService,
                             PersonneService personneService,
                             JavaMailSender mailSender, CalendrierService calendrierService) {
        this.articleService = articleService;
        this.mailSender = mailSender;
        this.referenceService = referenceService;
        this.personneService = personneService;
        this.calendrierService = calendrierService;
    }

    @Override
    public String getPageTitle() {
        return getTranslation("detail.pagetitle");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String idStr = event.getRouteParameters().get("articleId").orElse("0");
        try {
            Long id = Long.valueOf(idStr);

            articleService.findByIdAndTrackVisit(id).ifPresentOrElse(
                    a -> {
                        this.article = a;

                        // ── Enregistrement de la visite (utilisateur connecté uniquement) ──
                        try {
                            var auth = SecurityContextHolder.getContext().getAuthentication();
                            if (auth != null && auth.getPrincipal() instanceof OidcUser oidcUser) {
                                String email = oidcUser.getAttribute("email");
                                String nom = oidcUser.getAttribute("family_name");
                                String prenom = oidcUser.getAttribute("given_name");
                                referenceService.enregistrerVisite(
                                        a.getIdArticle().intValue(),
                                        nom != null ? nom : "",
                                        prenom != null ? prenom : "",
                                        email != null ? email : ""
                                );
                            }
                        } catch (Exception ex) {
                            System.err.println("Erreur enregistrement visite : " + ex.getMessage());
                        }

                        // ── Vendeur : chargé indépendamment de la connexion ──
                        try {
                            Integer idVendeur = referenceService.findIdVendeur(a.getIdArticle().intValue());
                            personne = idVendeur != null
                                    ? personneService.findById(idVendeur.longValue())
                                    : null;
                        } catch (Exception ex) {
                            personne = null;
                            System.err.println("Erreur chargement vendeur : " + ex.getMessage());
                        }

                        buildView(a);
                    },
                    () -> add(new Paragraph(getTranslation("detail.notfound")))
            );
        } catch (NumberFormatException e) {
            add(new Paragraph(getTranslation("detail.invalidid")));
        }
    }

    // ══════════════════════════════════════════════════════════
    //  Helpers responsive pour les boîtes de dialogue
    // ══════════════════════════════════════════════════════════

    /**
     * Configuration commune des dialogues :
     * - largeur plafonnée à la fenêtre (min(px, 96vw)) au lieu d'une valeur fixe
     * - hauteur max en dvh (tient compte de la barre d'adresse mobile, contrairement à vh)
     * - optionnel : classe CSS "responsive-dialog" => plein écran sur téléphone
     *   (voir la feuille de style fournie avec ce fichier)
     */
    private void configureDialog(Dialog dlg, int maxWidthPx, boolean fullscreenOnMobile) {
        dlg.setWidth("min(" + maxWidthPx + "px, 96vw)");
        dlg.setMaxWidth("100vw");
        dlg.setMaxHeight("92dvh");
        if (fullscreenOnMobile) {
            dlg.addClassName("responsive-dialog");
        }
    }

    // ══════════════════════════════════════════════════════════

    private void buildView(Article article) {
        removeAll();
        setPadding(true);
        setSpacing(true);

        // ── Image ─────────────────────────────────────────────
        Div imgBox = new Div();
        imgBox.getStyle()
                .set("width", "180px").set("height", "120px")
                .set("border", "1px solid #999")
                .set("margin-bottom", "12px");

        String pochette = article.getPochette();
        if (pochette != null && !pochette.isBlank()) {
            Image img = new Image("/images/articles/" + pochette, article.getNom());
            img.getStyle()
                    .set("width", "180px").set("height", "120px")
                    .set("object-fit", "cover");
            imgBox.add(img);
        } else {
            imgBox.getStyle().set("background-color", "#DDD");
        }
        add(imgBox);

        // ── Corps ─────────────────────────────────────────────
        HorizontalLayout body = new HorizontalLayout();
        body.setAlignItems(Alignment.START);
        body.setSpacing(false);
        body.setWidthFull();
        body.getStyle()
                .set("flex-wrap", "wrap")              // colonnes empilées sur mobile
                .set("column-gap", "96px")
                .set("row-gap", "1rem");
        body.add(buildLeftColumn(article));
        body.add(buildRightColumn(article));
        add(body);
        // ── Derniers enchérisseurs ────────────────────────────
        if (article.getEnchere() != null && article.getEnchere() > 0) {
            add(new Hr());
            add(buildEncherisseursSection(article));
        }
        // ── Description ───────────────────────────────────────
        add(new Hr());
        if (article.getLabel() != null && !article.getLabel().isBlank()) {
            Paragraph desc = new Paragraph(article.getLabel());
            desc.getStyle().set("font-size", "0.9rem");
            add(desc);
        }

        String video = article.getLinkYoutube();
        if (video != null && !video.isBlank()) {
            IFrame iframe = new IFrame();
            iframe.setSrc("https://www.youtube-nocookie.com/embed/" + video);
            iframe.setWidth("100%");
            iframe.setMaxWidth("560px");
            iframe.getStyle().set("aspect-ratio", "16 / 9");
            iframe.getElement().setAttribute("frameborder", "0");
            iframe.getElement().setAttribute("allow",
                    "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture");
            iframe.getElement().setAttribute("allowfullscreen", true);
            add(iframe);
        }

        add(new Hr());
        add(buildCommentsSection(article));
    }

    private VerticalLayout buildEncherisseursSection(Article article) {
        final int PAGE_SIZE = 5;
        final int idArticle = article.getIdArticle().intValue();
        int[] currentPage = {0};

        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(false);
        section.setWidthFull();

        H4 titre = new H4(getTranslation("detail.bidders.title"));
        titre.getStyle().set("color", "#0000CC").set("margin-bottom", "8px");
        section.add(titre);

        VerticalLayout listeZone = new VerticalLayout();
        listeZone.setPadding(false);
        listeZone.setSpacing(false);
        listeZone.setWidthFull();

        HorizontalLayout pagination = new HorizontalLayout();
        pagination.setAlignItems(Alignment.CENTER);
        pagination.setJustifyContentMode(
                com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.CENTER);
        pagination.setWidthFull();
        pagination.setMaxWidth("560px");
        pagination.getStyle().set("margin-top", "8px");

        Button prevBtn = new Button("◀");
        Button nextBtn = new Button("▶");
        Span pageInfo = new Span();
        stylePageButton(prevBtn);
        stylePageButton(nextBtn);
        pageInfo.getStyle().set("font-size", "0.8rem");

        Runnable renderPage = () -> {
            listeZone.removeAll();

            int total = referenceService.countEncherisseurs(idArticle);
            if (total == 0) {
                Span aucun = new Span(getTranslation("detail.bidders.none"));
                aucun.getStyle().set("font-size", "0.85rem").set("color", "#999");
                listeZone.add(aucun);
                pagination.setVisible(false);
                return;
            }

            int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
            // si des offres ont disparu entre deux clics, on reste dans les bornes
            currentPage[0] = Math.max(0, Math.min(currentPage[0], totalPages - 1));

            var offres = referenceService.findDerniersEncherisseurs(
                    idArticle, PAGE_SIZE, currentPage[0] * PAGE_SIZE);

            for (int i = 0; i < offres.size(); i++) {
                // seule la toute première offre (page 1, ligne 1) est mise en évidence
                boolean plusRecente = currentPage[0] == 0 && i == 0;
                listeZone.add(buildEncherisseurRow(offres.get(i), plusRecente));
            }

            pageInfo.setText((currentPage[0] + 1) + " / " + totalPages);
            prevBtn.setEnabled(currentPage[0] > 0);
            nextBtn.setEnabled(currentPage[0] < totalPages - 1);
            pagination.setVisible(true);
        };

        prevBtn.addClickListener(e -> { currentPage[0]--; renderPage.run(); });
        nextBtn.addClickListener(e -> { currentPage[0]++; renderPage.run(); });

        pagination.add(prevBtn, pageInfo, nextBtn);
        section.add(listeZone, pagination);
        renderPage.run();

        return section;
    }

    private HorizontalLayout buildEncherisseurRow(String[] o, boolean plusRecente) {
        String[] identite = formatEncherisseur(o[0], o[1]);
        String affichage = identite[0];
        String initiale  = identite[1];
        String nomUtilisateur = o.length > 4 ? o[4] : null;

        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(Alignment.CENTER);
        row.setWidthFull();
        row.setMaxWidth("560px");
        row.getStyle()
                .set("padding", "6px 8px")
                .set("flex-wrap", "wrap")
                .set("border-bottom", "1px solid #F0E6FF");
        if (plusRecente) {
            row.getStyle().set("background-color", "#F7F0FF");
        }

        Div avatar = new Div();
        avatar.setText(initiale);
        avatar.getStyle()
                .set("width", "30px").set("height", "30px")
                .set("border-radius", "50%")
                .set("background-color", "#6100C1")
                .set("color", "white")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("font-size", "0.8rem")
                .set("flex-shrink", "0");

        // ✅ Largeur flexible (avant : 150px fixes) : le nom s'adapte à la place disponible
        Span nomSpan = new Span(affichage.trim());
        nomSpan.getStyle()
                .set("font-size", "0.85rem")
                .set("flex", "1 1 120px")
                .set("min-width", "0")
                .set("overflow-wrap", "anywhere");
        if (nomUtilisateur != null && !nomUtilisateur.isBlank()) {
            nomSpan.getStyle().set("cursor", "pointer").set("color", "#0000CC");
            nomSpan.addClickListener(e -> nomSpan.getUI().ifPresent(ui ->
                    ui.navigate(ProfilView.class,
                            new RouteParameters("username", nomUtilisateur))));
        }

        Span montantSpan = new Span(o[2] + " CHF");
        montantSpan.getStyle()
                .set("font-size", "0.85rem")
                .set("font-weight", "bold")
                .set("color", "#0000CC")
                .set("flex", "0 0 auto")
                .set("white-space", "nowrap");

        Span dateSpan = new Span(formatDateVisite(o.length > 3 ? o[3] : null));
        dateSpan.getStyle()
                .set("font-size", "0.78rem")
                .set("color", "#888")
                .set("white-space", "nowrap");

        row.add(avatar, nomSpan, montantSpan, dateSpan);
        return row;
    }
    /**
     * Retourne [affichage, initiale] pour un enchérisseur.
     * Gère le cas où le nom complet est stocké dans "nom" avec "prenom" vide.
     */
    private String[] formatEncherisseur(String nom, String prenom) {
        String n = nom != null ? nom.trim() : "";
        String p = prenom != null ? prenom.trim() : "";

        // Fonctionne dans les deux cas :
        // - prenom = "Alexandre", nom = "Jaquet"  -> "Alexandre Jaquet"
        // - prenom vide, nom = "Alexandre Jaquet" -> "Alexandre Jaquet"
        String complet = (p + " " + n).trim();

        if (complet.isBlank()) {
            return new String[]{getTranslation("detail.anonymous"), "?"};
        }
        String[] mots = complet.split("\\s+");
        String initiales = mots.length > 1
                ? ("" + mots[0].charAt(0) + mots[mots.length - 1].charAt(0)).toUpperCase()
                : mots[0].substring(0, 1).toUpperCase();

        return new String[]{complet, initiales};
    }

    private VerticalLayout buildCommentsSection(Article article) {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(false);
        section.setWidthFull();

        H4 titre = new H4(getTranslation("detail.comments.title"));
        titre.getStyle().set("color", "#0000CC").set("margin-bottom", "8px");
        section.add(titre);

        var commentaires = referenceService.findCommentaires(article.getIdArticle().intValue());

        if (commentaires.isEmpty()) {
            Span aucun = new Span(getTranslation("detail.comments.none"));
            aucun.getStyle().set("font-size", "0.85rem").set("color", "#999");
            section.add(aucun);
        } else {
            for (String[] c : commentaires) {
                section.add(buildCommentRow(c));
            }
        }

        section.add(new Hr());

        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof OidcUser) {
            section.add(buildQuestionForm(article));
        } else {
            Span loginMsg = new Span(getTranslation("detail.comments.login"));
            loginMsg.getStyle().set("font-size", "0.85rem").set("color", "#999");
            section.add(loginMsg);
        }

        return section;
    }

    private HorizontalLayout buildCommentRow(String[] c) {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(Alignment.START);
        row.setWidthFull();
        row.getStyle()
                .set("padding", "8px 0")
                .set("border-bottom", "1px solid #EEE");

        Div avatar = new Div();
        String initiales = ((c[0] != null && !c[0].isBlank()) ? c[0].substring(0, 1) : "?").toUpperCase()
                + ((c[1] != null && !c[1].isBlank()) ? c[1].substring(0, 1) : "").toUpperCase();
        avatar.setText(initiales);
        avatar.getStyle()
                .set("width", "36px").set("height", "36px")
                .set("border-radius", "50%")
                .set("background-color", "#6100C1")
                .set("color", "white")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("font-size", "0.85rem")
                .set("flex-shrink", "0");

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(false);

        HorizontalLayout meta = new HorizontalLayout();
        meta.setAlignItems(Alignment.BASELINE);

        String nomComplet = (c[0] != null ? c[0] : "") + " " + (c[1] != null ? c[1] : "");
        String slug = nomComplet.trim().toLowerCase().replace(" ", "-");
        Anchor auteurLink = new Anchor("/profil/" + slug, nomComplet.trim());
        auteurLink.getStyle()
                .set("font-size", "0.85rem")
                .set("font-weight", "bold")
                .set("color", "#0000CC")
                .set("text-decoration", "none");

        Span dateSpan = new Span(c[2] != null ? c[2] : "");
        dateSpan.getStyle().set("font-size", "0.75rem").set("color", "#999");

        meta.add(auteurLink, dateSpan);

        Paragraph question = new Paragraph("❓ " + (c[3] != null ? c[3] : ""));
        question.getStyle()
                .set("font-size", "0.85rem")
                .set("font-weight", "bold")
                .set("margin", "6px 0 2px 0")
                .set("color", "#333");

        Paragraph texte = new Paragraph(c[4] != null ? c[4] : "");
        texte.getStyle()
                .set("font-size", "0.85rem")
                .set("margin", "0")
                .set("color", "#555");
        texte.setVisible(c[4] != null && !c[4].isBlank());

        content.add(meta, question, texte);
        row.add(avatar, content);
        return row;
    }

    private VerticalLayout buildQuestionForm(Article article) {
        VerticalLayout form = new VerticalLayout();
        form.setPadding(false);
        form.setSpacing(true);
        form.setMaxWidth("560px");

        H4 titreForm = new H4(getTranslation("detail.ask.title"));
        titreForm.getStyle().set("color", "#0000CC").set("margin", "0");
        form.add(titreForm);

        TextField questionField = new TextField(getTranslation("detail.ask.subject"));
        questionField.setPlaceholder(getTranslation("detail.ask.subject.ph"));
        questionField.setWidthFull();

        TextArea texteField = new TextArea(getTranslation("detail.ask.detail"));
        texteField.setPlaceholder(getTranslation("detail.ask.detail.ph"));
        texteField.setWidthFull();
        texteField.setMinHeight("80px");

        Span errMsg = new Span("");
        errMsg.getStyle().set("color", "#CC0000").set("font-size", "0.8rem");
        errMsg.setVisible(false);

        Button envoyerBtn = styledButton(getTranslation("detail.send"));

        envoyerBtn.addClickListener(e -> {
            String questionVal = questionField.getValue();
            String texteVal    = texteField.getValue();

            if (questionVal == null || questionVal.isBlank()) {
                errMsg.setText(getTranslation("detail.ask.err.subject"));
                errMsg.setVisible(true);
                return;
            }
            try {
                var auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth == null || !(auth.getPrincipal() instanceof OidcUser oidcUser)) {
                    errMsg.setText(getTranslation("detail.err.notlogged"));
                    errMsg.setVisible(true);
                    return;
                }
                String email = oidcUser.getAttribute("email");
                Integer idAuteur = referenceService.findPersonneIdByEmail(email);
                if (idAuteur == null) {
                    errMsg.setText(getTranslation("detail.err.noaccount"));
                    errMsg.setVisible(true);
                    return;
                }

                referenceService.enregistrerCommentaire(
                        article.getIdArticle().intValue(),
                        idAuteur,
                        questionVal.trim(),
                        texteVal != null ? texteVal.trim() : ""
                );

                Notification.show(getTranslation("detail.ask.sent"), 3000,
                        Notification.Position.TOP_CENTER);

                questionField.clear();
                texteField.clear();
                errMsg.setVisible(false);
                buildView(article);

            } catch (Exception ex) {
                errMsg.setText(getTranslation("detail.error", String.valueOf(ex.getMessage())));
                errMsg.setVisible(true);
            }
        });

        form.add(questionField, texteField, errMsg, envoyerBtn);
        return form;
    }

    private VerticalLayout buildLeftColumn(Article article) {
        VerticalLayout col = new VerticalLayout();
        col.setSpacing(false);
        col.setPadding(false);
        col.setWidth("310px");
        col.setMaxWidth("100%");

        String catLibelle    = "";
        String subCatLibelle = "";
        if (article.getRefCategorie() != null) {
            catLibelle = referenceService.findCategorieLibelle(article.getRefCategorie());
        }
        // ✅ CORRECTION : la sous-catégorie se résout avec le COUPLE (catégorie, sous-catégorie).
        // Les ids 1 à 4 existent à la fois en vêtements et en vins : l'id seul est ambigu.
        if (article.getRefCategorie() != null && article.getRefSubcategorie() != null) {
            subCatLibelle = referenceService.findSubCategorieLibelle(
                    article.getRefCategorie(), article.getRefSubcategorie());
        }

        col.add(fieldRow(getTranslation("detail.f.category"), catLibelle));
        if (subCatLibelle != null && !subCatLibelle.isBlank()) {
            col.add(fieldRow(getTranslation("detail.f.subcategory"), subCatLibelle));
        }
        col.add(
                fieldRow(getTranslation("detail.f.name"),       article.getNom()),
                fieldRow(getTranslation("detail.f.maker"), article.getMarque()),
                fieldRow(getTranslation("detail.f.price"), article.getPrix() != null
                        ? String.format("%.2f CHF", article.getPrix())
                        : "—"),
                gap()
        );

        Integer catId    = article.getRefCategorie();
        Integer subcatId = article.getRefSubcategorie();

        if (catId != null) {
            ArticleCategory cat = ArticleCategory.fromId(catId);

            if (cat == ArticleCategory.VOITURE || cat == ArticleCategory.MOTO) {
                col.add(
                        fieldRow(getTranslation("detail.f.hp"),          str(article.getNbCheveaux())),
                        fieldRow(getTranslation("detail.f.cylinders"),             str(article.getNbCylindre())),
                        fieldRow(getTranslation("detail.f.km"),           str(article.getNbKm())),
                        fieldRow(getTranslation("detail.f.yearMade"),  str(article.getAnnee())),
                        fieldRow(getTranslation("detail.f.firstReg"),  str(article.getPremiereImmatriculation()))
                );

                if (article.getEssenceOuDiesel() != null)
                    referenceService.findAllFuelTypes().stream()
                            .filter(f -> f.id() == article.getEssenceOuDiesel())
                            .findFirst()
                            .ifPresent(f -> col.add(fieldRow(getTranslation("detail.f.fuel"), f.nom())));

                if (article.getRefBoiteDeVitesse() != null)
                    referenceService.findAllGearboxTypes().stream()
                            .filter(g -> g.id() == article.getRefBoiteDeVitesse())
                            .findFirst()
                            .ifPresent(g -> col.add(fieldRow(getTranslation("detail.f.gearbox"), g.nom())));

                col.add(fieldRow(getTranslation("detail.f.ac"),
                        article.getClima() != null && article.getClima() == 1 ? getTranslation("detail.yes") : getTranslation("detail.no")));
            }

            else if (cat == ArticleCategory.LIVRES) {
                col.add(
                        fieldRow(getTranslation("detail.f.author"),  article.getAuteur()),
                        fieldRow(getTranslation("detail.f.publisher"), article.getMarque()),
                        fieldRow(getTranslation("detail.f.year"),   str(article.getAnnee()))
                );
            }

            else if (cat == ArticleCategory.IMMOBILIER) {
                col.add(
                        fieldRow(getTranslation("detail.f.rooms"),    str(article.getNbPiece())),
                        fieldRow(getTranslation("detail.f.living"),   str(article.getSurfaceHabitable())),
                        fieldRow(getTranslation("detail.f.land"),     str(article.getSuperficieTerrain())),
                        fieldRow(getTranslation("detail.f.yearBuilt"),  str(article.getAnneeConstruction())),
                        fieldRow(getTranslation("detail.f.address"),             article.getAdresse()),
                        fieldRow(getTranslation("detail.f.city"),               article.getLieu()),
                        fieldRow(getTranslation("detail.f.zip"),         article.getNpa())
                );
            }

            else if (cat == ArticleCategory.FASHION_FEMME
                    || cat == ArticleCategory.FASHION_HOMME
                    || cat == ArticleCategory.LINGERIE) {
                col.add(fieldRow(getTranslation("detail.f.size"), str(article.getTaille())));
            }

            else if (cat == ArticleCategory.VINS) {
                col.add(fieldRow(getTranslation("detail.f.vintage"), article.getTx()));

                if (article.getRefTypeDeVin() != null)
                    referenceService.findAllWineTypes().stream()
                            .filter(w -> w.id() == article.getRefTypeDeVin())
                            .findFirst()
                            .ifPresent(w -> col.add(fieldRow(getTranslation("detail.f.wineType"), w.nom())));

                // ✅ CORRECTION : getRefCepage() (cohérent avec ArticleRepository.setRefCepage)
                if (article.getRefCepage() != null)
                    referenceService.findAllCepages().stream()
                            .filter(w -> w.id() == article.getRefCepage())
                            .findFirst()
                            .ifPresent(w -> col.add(fieldRow(getTranslation("detail.f.grape"), w.nom())));
            }

            else if (cat == ArticleCategory.DVD) {
                col.add(
                        fieldRow(getTranslation("detail.f.actors"),    article.getActeurs()),
                        fieldRow(getTranslation("detail.f.director"),  article.getRealisateur()),
                        fieldRow(getTranslation("detail.f.duration"),  str(article.getDuree())),
                        fieldRow(getTranslation("detail.f.year"),        str(article.getAnnee()))
                );
            }

            else if (cat == ArticleCategory.JEUX) {
                if (article.getRefTypeDeJeux() != null)
                    referenceService.findAllGameTypes().stream()
                            .filter(g -> g.id() == article.getRefTypeDeJeux())
                            .findFirst()
                            .ifPresent(g -> col.add(fieldRow(getTranslation("detail.f.gameType"), g.nom())));
            }

            if (subcatId != null) {
                if (subcatId == ArticleSubCategory.TV_ECRAN_PLAT_ID) {
                    col.add(fieldRow(getTranslation("detail.f.dimension"), str(article.getDimension())));
                    if (article.getRefTypeEcran() != null)
                        referenceService.findAllScreenTypes().stream()
                                .filter(s -> s.id() == article.getRefTypeEcran())
                                .findFirst()
                                .ifPresent(s -> col.add(fieldRow(getTranslation("detail.f.screenType"), s.nom())));
                }
                if (ArticleSubCategory.INFO_PC_SUBCATS.contains(subcatId)) {
                    col.add(
                            fieldRow(getTranslation("detail.f.cpu"), str(article.getProcesseur())),
                            fieldRow(getTranslation("detail.f.ram"),          article.getRam()),
                            fieldRow(getTranslation("detail.f.disk"),   article.getDisqueDur())
                    );
                }
            }
        }

        referenceService.findAllPayements().stream()
                .filter(p -> p.id() == (article.getRefConditionPayement() != null ? article.getRefConditionPayement() : -1))
                .findFirst()
                .ifPresent(p -> col.add(fieldRow(getTranslation("detail.f.payment"), p.nom())));

        referenceService.findAllLivraisons().stream()
                .filter(l -> l.id() == (article.getRefModeDeLivraison() != null ? article.getRefModeDeLivraison() : -1))
                .findFirst()
                .ifPresent(l -> col.add(fieldRow(getTranslation("detail.f.delivery"), l.nom())));

        return col;
    }

    private VerticalLayout buildRightColumn(Article article) {
        VerticalLayout col = new VerticalLayout();
        col.setSpacing(false);
        col.setPadding(false);
        col.setWidth("300px");
        col.setMaxWidth("100%");

        col.add(fieldRow(getTranslation("detail.f.views"),    str(article.getVisites())));
        if (article.getEnchere() != null && article.getEnchere().equals(1)) {
            col.add(fieldRow(getTranslation("detail.f.bidsCount"), str(article.getNbrEnchere())));
        }
        col.add(fieldRow(getTranslation("detail.f.stock"), str(article.getQuantite())));
        col.add(gap());

        // ── Vendeur ────────────────────────────────────────────
        // ✅ CORRECTION : on réutilise le champ "personne" (déjà chargé) et on teste null
        if (personne != null) {
            String lienProfil = personne.getNomUtilisateur() != null ? personne.getNomUtilisateur() : "";
            String nomAffiche = personne.getNom() != null ? personne.getNom() : lienProfil;
            Anchor auteurLink = new Anchor("/profil/" + lienProfil, nomAffiche);

            Span lbl = new Span(getTranslation("detail.seller"));
            lbl.getStyle()
                    .set("font-size", "0.85rem")
                    .set("margin-right", "6px");
            auteurLink.getStyle()
                    .set("font-size", "0.85rem")
                    .set("font-weight", "bold")
                    .set("color", "#0000CC")
                    .set("text-decoration", "none");

            HorizontalLayout vendeurLayout = new HorizontalLayout(lbl, auteurLink);
            vendeurLayout.setAlignItems(Alignment.CENTER);
            col.add(vendeurLayout);
        }

        col.add(gap());

        // ── Bouton visiteurs ───────────────────────────────────
        // ✅ Même style que les autres boutons d'action
        Button listeVisiteursBtn = styledButton(getTranslation("detail.visitors.btn"));
        listeVisiteursBtn.addClickListener(e -> showVisiteursDialog(article));

        // ── Boutons d'action ───────────────────────────────────
        // ✅ Grille CSS : toutes les cellules ont la même largeur, les boutons la remplissent.
        // Pour une pile verticale (1 bouton par ligne), remplacer par "1fr".
        Div buttonsLayout = new Div();
        buttonsLayout.setWidthFull();
        buttonsLayout.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(150px, 1fr))")
                .set("gap", "8px")
                .set("max-width", "100%")
                .set("box-sizing", "border-box");

        Integer catId = article.getRefCategorie();
        boolean immobilier = catId != null && ArticleCategory.fromId(catId) == ArticleCategory.IMMOBILIER;

        if (article.getEnchere() != null && article.getEnchere() > 0) {

            Double derniereOffre = referenceService.findDerniereOffre(
                    article.getIdArticle().intValue()
            );
            String date = article.getEnchereDateFin();

            col.add(fieldRow(getTranslation("detail.f.lastOffer"),
                    derniereOffre != null
                            ? String.format("%.2f CHF", derniereOffre)
                            : getTranslation("detail.noOffer")));

            if (date != null) {
                col.add(fieldRow(getTranslation("detail.f.closeDate"), date));
            }
            Button offreBtn = styledButton(getTranslation("detail.offer.make"));
            offreBtn.addClickListener(e -> showFaireOffreDialog(article, null, derniereOffre));

            if (immobilier) {
                Button visiteBtn = styledButton(getTranslation("detail.visit.request"));
                visiteBtn.addClickListener(e -> showDemandeDeVisite(article));
                Button visiteCalendrier = styledButton(getTranslation("detail.agenda"));
                visiteCalendrier.addClickListener(e -> ouvrirAgenda());

                buttonsLayout.add(listeVisiteursBtn, offreBtn, visiteBtn, visiteCalendrier);
            } else {
                buttonsLayout.add(listeVisiteursBtn, offreBtn);
            }

        } else {

            Button offreBtn = styledButton(getTranslation("detail.buy"));
            offreBtn.addClickListener(e -> showAcheterDialog(article));

            if (immobilier) {
                Button visiteBtn = styledButton(getTranslation("detail.visit.request"));
                visiteBtn.addClickListener(e -> showDemandeDeVisite(article));

                Button visiteCalendrier = styledButton(getTranslation("detail.agenda"));
                visiteCalendrier.addClickListener(e -> ouvrirAgenda());

                buttonsLayout.add(listeVisiteursBtn, offreBtn, visiteBtn, visiteCalendrier);
            } else {
                buttonsLayout.add(listeVisiteursBtn, offreBtn);
            }
        }
        // ✅ Chaque bouton remplit sa cellule de la grille -> tailles identiques
        buttonsLayout.getChildren().forEach(c -> {
            if (c instanceof Button b) b.setWidthFull();
        });
        col.add(buttonsLayout);

        return col;
    }

    /** ✅ Ouvre l'agenda du vendeur, en protégeant le cas "vendeur introuvable". */
    private void ouvrirAgenda() {
        if (personne == null || personne.getEmail() == null || personne.getEmail().isBlank()) {
            Notification.show(getTranslation("detail.agenda.unavailable"), 3000,
                    Notification.Position.TOP_CENTER);
            return;
        }
        buildCalendrierArticle(personne.getEmail());
    }

    // ══════════════════════════════════════════════════════════
    //  Dialogue : demande de visite
    //  ✅ En-tête + pied de page fixes, contenu qui défile, plein écran sur téléphone,
    //     FormLayout responsive, claviers mobiles adaptés.
    // ══════════════════════════════════════════════════════════
    private void showDemandeDeVisite(Article article) {
        Dialog dlg = new Dialog();
        configureDialog(dlg, 480, true);
        dlg.setCloseOnOutsideClick(false); // évite de perdre la saisie d'un tap accidentel
        dlg.setHeaderTitle(getTranslation("detail.visit.request"));

        // ── Email (clavier avec @ sur mobile) ──
        EmailField reponseParEmail = new EmailField(getTranslation("detail.visit.email"));
        reponseParEmail.setPlaceholder(getTranslation("detail.visit.email.ph"));
        reponseParEmail.setClearButtonVisible(true);
        reponseParEmail.setAutocomplete(Autocomplete.EMAIL);
        reponseParEmail.setWidthFull();

        // ── Téléphone ──
        List<PaysPhone> pays = buildPaysList();

        ComboBox<PaysPhone> indicatifBox = new ComboBox<>(getTranslation("detail.visit.dial"));
        indicatifBox.setItems(pays);

        // Texte utilisé pour le filtrage et pour la valeur affichée dans le champ fermé
        indicatifBox.setItemLabelGenerator(p -> p.code() + " " + p.indicatif());

        // ✅ Drapeaux en images (Windows n'affiche pas les emojis de drapeaux) :
        // rendu de chaque ligne de la liste déroulante = image + nom du pays + indicatif
        indicatifBox.setRenderer(new ComponentRenderer<>(p -> {
            HorizontalLayout ligne = new HorizontalLayout();
            ligne.setAlignItems(Alignment.CENTER);
            ligne.setPadding(false);
            ligne.setSpacing(true);

            ligne.add(createFlagImage(p.code()), new Span(p.nom() + " " + p.indicatif()));
            return ligne;
        }));

        // ✅ Drapeau du pays sélectionné, affiché dans le champ fermé (préfixe)
        Image flagPrefix = createFlagImage("CH");
        flagPrefix.setVisible(false);
        indicatifBox.setPrefixComponent(flagPrefix);

        // Le listener doit être ajouté AVANT le setValue initial pour afficher le drapeau au chargement
        indicatifBox.addValueChangeListener(e -> {
            PaysPhone p = e.getValue();
            flagPrefix.setVisible(p != null);
            if (p != null) {
                flagPrefix.setSrc(flagUrl(p.code()));
                flagPrefix.setAlt(p.code());
            }
        });

        pays.stream()
                .filter(p -> p.code().equals("CH"))
                .findFirst()
                .ifPresent(indicatifBox::setValue);

        TextField reponseParTelephone = new TextField(getTranslation("detail.visit.number"));
        reponseParTelephone.setPlaceholder("XX XXX XX XX");
        // ✅ Clavier numérique téléphone + autocomplétion du navigateur
        reponseParTelephone.getElement().setAttribute("inputmode", "tel");
        reponseParTelephone.setAutocomplete(Autocomplete.TEL);

        reponseParTelephone.addValueChangeListener(e -> {
            PaysPhone selected = indicatifBox.getValue();
            if (selected != null) {
                String raw = e.getValue().replaceAll("[^0-9]", "");
                String formatted = formatWithLibPhone(raw, selected.code());
                if (!formatted.equals(e.getValue())) {
                    reponseParTelephone.setValue(formatted);
                }
            }
        });

        // ✅ Au lieu de 45 % / 55 % : les champs passent à la ligne si la place manque (écrans ~360 px)
        indicatifBox.setMinWidth("140px");
        indicatifBox.getStyle().set("flex", "1 1 140px");
        reponseParTelephone.setMinWidth("160px");
        reponseParTelephone.getStyle().set("flex", "2 1 180px");

        HorizontalLayout phoneLayout = new HorizontalLayout(indicatifBox, reponseParTelephone);
        phoneLayout.setAlignItems(Alignment.BASELINE);
        phoneLayout.setWidthFull();
        phoneLayout.setPadding(false);
        phoneLayout.getStyle().set("flex-wrap", "wrap");

        // ── Identité / adresse ──
        TextField nom = new TextField(getTranslation("detail.visit.name"));
        nom.setPlaceholder(getTranslation("detail.visit.name.ph"));
        nom.setAutocomplete(Autocomplete.FAMILY_NAME);

        TextField prenom = new TextField(getTranslation("detail.visit.firstname"));
        prenom.setPlaceholder(getTranslation("detail.visit.firstname.ph"));
        prenom.setAutocomplete(Autocomplete.GIVEN_NAME);

        TextField adresse = new TextField(getTranslation("detail.visit.address"));
        adresse.setPlaceholder(getTranslation("detail.visit.address.ph"));
        adresse.setAutocomplete(Autocomplete.STREET_ADDRESS);

        TextArea commentaire = new TextArea(getTranslation("detail.visit.comment"));
        commentaire.setPlaceholder(getTranslation("detail.visit.comment.ph"));
        commentaire.setMinHeight("80px");

        DatePicker datePicker = new DatePicker(getTranslation("detail.visit.date"));
        datePicker.setValue(LocalDate.now());
        datePicker.setMin(LocalDate.now());
        datePicker.setLocale(getLocale());

        TimePicker timePicker = new TimePicker(getTranslation("detail.visit.time"));
        timePicker.setValue(LocalTime.of(9, 0));
        timePicker.setStep(Duration.ofMinutes(30));
        timePicker.setMin(LocalTime.of(8, 0));
        timePicker.setMax(LocalTime.of(18, 0));

        // ✅ FormLayout : 1 colonne sur mobile, 2 colonnes dès 500 px
        FormLayout form = new FormLayout();
        form.setWidthFull();
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2));
        form.add(reponseParEmail, 2);
        form.add(phoneLayout, 2);
        form.add(nom, prenom);
        form.add(adresse, 2);
        form.add(datePicker, timePicker);
        form.add(commentaire, 2);
        dlg.add(form);

        // ✅ Boutons dans le pied de page : toujours visibles, même avec le clavier ouvert
        Button fermerBtn = secondaryButton(getTranslation("detail.close"));
        fermerBtn.addClickListener(e -> dlg.close());

        Button envoyerBtn = primaryButton(getTranslation("detail.send"));
        envoyerBtn.addClickListener(e -> {
            // ✅ Validation : date et heure obligatoires (LocalDateTime.of plante sur null)
            if (datePicker.getValue() == null || timePicker.getValue() == null) {
                Notification.show(getTranslation("detail.visit.pick"), 3000,
                        Notification.Position.TOP_CENTER);
                return;
            }

            PaysPhone selected = indicatifBox.getValue();
            String numeroComplet = selected != null
                    ? selected.indicatif() + reponseParTelephone.getValue().replaceAll("[^0-9]", "")
                    : reponseParTelephone.getValue();

            LocalDateTime dateVisite = LocalDateTime.of(
                    datePicker.getValue(),
                    timePicker.getValue()
            );

            String emailDemandeur = reponseParEmail.getValue() != null
                    ? reponseParEmail.getValue().trim() : "";

            try {
                Integer idVendeur = referenceService.findIdVendeur(article.getIdArticle().intValue());
                if (idVendeur != null) {
                    referenceService.enregistrerDemandeVisite(
                            article.getIdArticle().intValue(),
                            idVendeur,
                            emailDemandeur,
                            numeroComplet,
                            nom.getValue().trim(),
                            prenom.getValue().trim(),
                            adresse.getValue().trim(),
                            commentaire.getValue().trim(),
                            dateVisite
                    );
                    Personne vendeur = this.personneService.findById(idVendeur.longValue());
                    if (vendeur != null) {
                        envoyerEmailDemandeVisite(
                                vendeur.getEmail(),
                                article.getNom(),
                                nom.getValue() + " " + prenom.getValue(),
                                commentaire.getValue(),
                                emailDemandeur,
                                dateVisite
                        );
                    }
                }
                Notification.show(getTranslation("detail.visit.sent"), 3000,
                        Notification.Position.TOP_CENTER);
                dlg.close();
            } catch (Exception ex) {
                Notification.show(getTranslation("detail.error", String.valueOf(ex.getMessage())), 3000,
                        Notification.Position.TOP_CENTER);
            }
        });

        dlg.getFooter().add(fermerBtn, envoyerBtn);
        dlg.open();
    }

    private void envoyerEmailDemandeVisite(String email, String nom,
                                           String nomPrenom,
                                           String commentaire,
                                           String emailDemandeur,
                                           LocalDateTime dateVisite) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setFrom("robot@avant-garde.ch");
        msg.setSubject("Vous avez une demande de visites !");
        // ✅ CORRECTION : le text block était concaténé avec "Bonjour " + email + """...""",
        // ce qui mélangeait concaténation et .formatted() (le .formatted ne s'appliquait
        // qu'au dernier bloc). On formate maintenant un seul bloc.
        msg.setText("""
                Bonjour,

                Vous avez une nouvelle demande de visite pour l'article %s

                La personne avec le nom suivant %s désire visiter le bien, voici son commentaire : %s

                Voici son email : %s

                Il désire visiter le bien pour la date du %s

                L'équipe Avant-Garde
                """.formatted(nom, nomPrenom, commentaire, emailDemandeur, dateVisite));
        try {
            mailSender.send(msg);
            Notification.show(getTranslation("detail.mail.sent"));
        } catch (Exception e) {
            System.err.println("Erreur envoi email vendeur : " + e.getMessage());
        }
    }

    private String formatWithLibPhone(String raw, String countryCode) {
        try {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber number = phoneUtil.parse(raw, countryCode);
            return phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        } catch (NumberParseException e) {
            return raw;
        }
    }

    private List<PaysPhone> buildPaysList() {
        return Arrays.asList(
                new PaysPhone("CH", "+41",  countryName("CH")),
                new PaysPhone("FR", "+33",  countryName("FR")),
                new PaysPhone("DE", "+49",  countryName("DE")),
                new PaysPhone("IT", "+39",  countryName("IT")),
                new PaysPhone("BE", "+32",  countryName("BE")),
                new PaysPhone("ES", "+34",  countryName("ES")),
                new PaysPhone("PT", "+351", countryName("PT")),
                new PaysPhone("GB", "+44",  countryName("GB")),
                new PaysPhone("US", "+1",   countryName("US")),
                new PaysPhone("CA", "+1",   countryName("CA")),
                new PaysPhone("JP", "+81",  countryName("JP")),
                new PaysPhone("CN", "+86",  countryName("CN")),
                new PaysPhone("BR", "+55",  countryName("BR")),
                new PaysPhone("AU", "+61",  countryName("AU")),
                new PaysPhone("MA", "+212", countryName("MA")),
                new PaysPhone("SN", "+221", countryName("SN")),
                new PaysPhone("TN", "+216", countryName("TN"))
        );
    }

    public String getFullPhoneNumber(ComboBox<PaysPhone> indicatifBox, TextField numeroField) {
        PaysPhone pays = indicatifBox.getValue();
        if (pays == null || numeroField.getValue().isBlank()) return "";
        String raw = numeroField.getValue().replaceAll("[^0-9]", "");
        return pays.indicatif() + raw;
    }

    /** Nom du pays dans la langue de l'interface (fr/en/de), sans table de traduction. */
    private String countryName(String countryCode) {
        return new Locale("", countryCode).getDisplayCountry(getLocale());
    }

    /** URL de l'image du drapeau pour un code pays ISO (ex. "CH" -> .../ch.png). */
    private String flagUrl(String countryCode) {
        return String.format(FLAG_URL_PATTERN, countryCode.toLowerCase(Locale.ROOT));
    }

    /** Image de drapeau de taille fixe (24 x 18 px). */
    private Image createFlagImage(String countryCode) {
        Image flag = new Image(flagUrl(countryCode), countryCode);
        flag.setWidth("24px");
        flag.setHeight("18px");
        flag.getStyle()
                .set("object-fit", "cover")
                .set("border-radius", "2px")
                .set("box-shadow", "0 0 1px rgba(0,0,0,0.4)")
                .set("flex-shrink", "0");
        return flag;
    }

    public record PaysPhone(String code, String indicatif, String nom) {
        @Override
        public String toString() {
            return nom + " (" + indicatif + ")";
        }
    }

    // ══════════════════════════════════════════════════════════
    //  Dialogue : liste des visiteurs
    //  ✅ Colonnes flexibles (plus de largeurs fixes qui débordent), boutons de
    //     pagination tactiles, bouton Fermer dans le pied de page.
    // ══════════════════════════════════════════════════════════
    private void showVisiteursDialog(Article article) {
        Dialog dlg = new Dialog();
        configureDialog(dlg, 560, false);
        dlg.setHeaderTitle(getTranslation("detail.visitors.title"));

        var visiteurs = referenceService.findVisiteurs(article.getIdArticle().intValue());
        final int PAGE_SIZE = 5;
        int[] currentPage = {0};

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(false);
        content.setWidthFull();

        VerticalLayout listeZone = new VerticalLayout();
        listeZone.setPadding(false);
        listeZone.setSpacing(false);
        listeZone.setWidthFull();

        HorizontalLayout pagination = new HorizontalLayout();
        pagination.setAlignItems(Alignment.CENTER);
        pagination.setJustifyContentMode(
                com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.CENTER
        );
        pagination.setWidthFull();
        pagination.getStyle().set("margin-top", "8px");

        Button prevBtn = new Button("◀");
        Button nextBtn = new Button("▶");
        Span pageInfo = new Span();
        stylePageButton(prevBtn);
        stylePageButton(nextBtn);

        Runnable renderPage = () -> {
            listeZone.removeAll();

            if (visiteurs.isEmpty()) {
                Span aucun = new Span(getTranslation("detail.visitors.none"));
                aucun.getStyle().set("color", "#999").set("font-size", "0.85rem");
                listeZone.add(aucun);
                pagination.setVisible(false);
                return;
            }

            pagination.setVisible(true);

            // En-tête de tableau : mêmes flex que les lignes pour rester alignées
            HorizontalLayout tableHeader = new HorizontalLayout();
            tableHeader.setWidthFull();
            tableHeader.addClassName("hide-on-mobile");   // masqué sur petit écran (voir CSS)
            tableHeader.getStyle().set("padding", "6px 8px").set("margin-bottom", "4px");
            Span hSpacer = new Span();                    // place de l'avatar
            hSpacer.getStyle().set("width", "30px").set("flex-shrink", "0");
            Span hNom = new Span(getTranslation("detail.visitors.h.visitor"));
            hNom.getStyle().set("flex", "1 1 120px").set("min-width", "0")
                    .set("font-size", "0.8rem").set("font-weight", "bold");
            Span hEmail = new Span(getTranslation("detail.visitors.h.email"));
            hEmail.getStyle().set("flex", "1 1 100px").set("min-width", "0")
                    .set("font-size", "0.8rem").set("font-weight", "bold");
            Span hDate = new Span(getTranslation("detail.visitors.h.date"));
            hDate.getStyle().set("font-size", "0.8rem").set("font-weight", "bold").set("white-space", "nowrap");
            tableHeader.add(hSpacer, hNom, hEmail, hDate);
            listeZone.add(tableHeader);

            int totalPages = (int) Math.ceil((double) visiteurs.size() / PAGE_SIZE);
            int from = currentPage[0] * PAGE_SIZE;
            int to   = Math.min(from + PAGE_SIZE, visiteurs.size());

            for (int i = from; i < to; i++) {
                String[] v = visiteurs.get(i);
                HorizontalLayout row = new HorizontalLayout();
                row.setAlignItems(Alignment.CENTER);
                row.setWidthFull();
                row.getStyle()
                        .set("padding", "6px 8px")
                        .set("flex-wrap", "wrap")          // la date passe à la ligne sur mobile
                        .set("border-bottom", "1px solid #F0E6FF");

                String nomComplet = (v[0] != null ? v[0] : "") + " " + (v[1] != null ? v[1] : "");
                String email      = v[2] != null ? v[2] : "";
                String username   = email.contains("@")
                        ? email.substring(0, email.indexOf('@'))
                        : nomComplet.trim();

                Div avatar = new Div();
                avatar.setText((!nomComplet.isBlank() ? nomComplet.substring(0, 1) : "?").toUpperCase());
                avatar.getStyle()
                        .set("width", "30px").set("height", "30px")
                        .set("border-radius", "50%")
                        .set("background-color", "#6100C1")
                        .set("color", "white")
                        .set("display", "flex")
                        .set("align-items", "center")
                        .set("justify-content", "center")
                        .set("font-size", "0.8rem")
                        .set("flex-shrink", "0");

                Span nomLink = new Span(nomComplet.trim());
                nomLink.getStyle()
                        .set("color", "#0000CC").set("cursor", "pointer")
                        .set("font-size", "0.85rem")
                        .set("flex", "1 1 120px").set("min-width", "0")
                        .set("overflow-wrap", "anywhere");
                nomLink.addClickListener(e -> {
                    dlg.close();
                    nomLink.getUI().ifPresent(ui -> ui.navigate("profil/" + username));
                });

                // ✅ Masquage de l'email sécurisé : substring(0, 2) plantait si la partie locale < 2 caractères
                Span emailSpan = new Span(masquerEmail(email));
                emailSpan.getStyle()
                        .set("font-size", "0.78rem").set("color", "#888")
                        .set("flex", "1 1 100px").set("min-width", "0")
                        .set("overflow", "hidden").set("text-overflow", "ellipsis");

                String dateBrute = v.length > 3 ? v[3] : null;
                Span dateSpan = new Span(formatDateVisite(dateBrute));
                dateSpan.getStyle().set("font-size", "0.78rem").set("color", "#888").set("white-space", "nowrap");

                row.add(avatar, nomLink, emailSpan, dateSpan);
                listeZone.add(row);
            }

            pageInfo.setText((currentPage[0] + 1) + " / " + totalPages);
            prevBtn.setEnabled(currentPage[0] > 0);
            nextBtn.setEnabled(currentPage[0] < totalPages - 1);
        };

        prevBtn.addClickListener(e -> { currentPage[0]--; renderPage.run(); });
        nextBtn.addClickListener(e -> { currentPage[0]++; renderPage.run(); });

        pagination.add(prevBtn, pageInfo, nextBtn);
        renderPage.run();
        content.add(listeZone, pagination);
        dlg.add(content);

        Button fermerBtn = primaryButton(getTranslation("detail.close"));
        fermerBtn.addClickListener(e -> dlg.close());
        dlg.getFooter().add(fermerBtn);

        dlg.open();
    }

    // ══════════════════════════════════════════════════════════
    //  Dialogue : faire une offre
    // ══════════════════════════════════════════════════════════
    private void showFaireOffreDialog(Article article, TextField offreField, Double derniereOffre) {
        Dialog dlg = new Dialog();
        configureDialog(dlg, 400, false);
        dlg.setCloseOnOutsideClick(false);
        dlg.setHeaderTitle(getTranslation("detail.offer.make"));

        // ✅ Prix de référence : dernière offre si elle existe, sinon prix de départ de l'article
        final Double prixReference = derniereOffre != null ? derniereOffre : article.getPrix();

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(true);

        Span nomArticle = new Span(getTranslation("detail.article", article.getNom() != null ? article.getNom() : "—"));
        nomArticle.getStyle().set("font-size", "0.9rem");
        content.add(nomArticle);

        Span prixActuel = new Span(getTranslation("detail.currentPrice",
                prixReference != null ? String.format("%.2f", prixReference) : "—"));
        prixActuel.getStyle().set("font-size", "0.9rem").set("color", "#555");
        content.add(prixActuel);
        content.add(new Hr());

        TextField montantField = new TextField(getTranslation("detail.offer.amount"));
        montantField.setWidthFull();
        montantField.setPlaceholder(getTranslation("detail.offer.amount.ph"));
        // ✅ Clavier numérique avec séparateur décimal sur mobile
        montantField.getElement().setAttribute("inputmode", "decimal");
        if (offreField != null && !offreField.getValue().isBlank()) {
            montantField.setValue(offreField.getValue());
        }
        content.add(montantField);

        Span errMsg = new Span("");
        errMsg.getStyle().set("color", "#CC0000").set("font-size", "0.8rem");
        errMsg.setVisible(false);
        content.add(errMsg);

        dlg.add(content);

        Button annulerBtn = secondaryButton(getTranslation("detail.cancel"));
        annulerBtn.addClickListener(e -> dlg.close());

        Button confirmerBtn = primaryButton(getTranslation("detail.confirm"));

        confirmerBtn.addClickListener(e -> {
            String valeur = montantField.getValue().replace(",", ".");
            if (valeur.isBlank()) {
                errMsg.setText(getTranslation("detail.offer.empty"));
                errMsg.setVisible(true);
                return;
            }
            try {
                double montant = Double.parseDouble(valeur);
                // ✅ CORRECTION : on compare à la dernière offre (ou au prix de départ s'il n'y en a pas)
                if (prixReference != null && montant <= prixReference) {
                    errMsg.setText(getTranslation("detail.offer.tooLow", String.format("%.2f", prixReference)));
                    errMsg.setVisible(true);
                    return;
                }

                var auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth == null || !(auth.getPrincipal() instanceof OidcUser oidcUser)) {
                    errMsg.setText(getTranslation("detail.offer.needLogin"));
                    errMsg.setVisible(true);
                    return;
                }

                String email = oidcUser.getAttribute("email");
                Integer idAcheteur = referenceService.findPersonneIdByEmail(email);
                Integer idVendeur  = referenceService.findIdVendeur(article.getIdArticle().intValue());

                if (idAcheteur == null) {
                    errMsg.setText(getTranslation("detail.err.noaccount"));
                    errMsg.setVisible(true);
                    return;
                }

                if (!idAcheteur.equals(idVendeur)) {
                    referenceService.enregistrerOffre(
                            article.getIdArticle().intValue(),
                            idAcheteur,
                            montant
                    );
                    Notification.show(
                            getTranslation("detail.offer.saved", String.valueOf(montant)),
                            3000,
                            Notification.Position.TOP_CENTER
                    );
                    dlg.close();
                } else {
                    errMsg.setText(getTranslation("detail.offer.own"));
                    errMsg.setVisible(true);
                }

            } catch (NumberFormatException ex) {
                errMsg.setText(getTranslation("detail.offer.invalid"));
                errMsg.setVisible(true);
            } catch (Exception ex) {
                errMsg.setText(getTranslation("detail.error", String.valueOf(ex.getMessage())));
                errMsg.setVisible(true);
            }
        });

        dlg.getFooter().add(annulerBtn, confirmerBtn);
        dlg.open();
    }

    // ══════════════════════════════════════════════════════════
    //  Dialogue : acheter
    // ══════════════════════════════════════════════════════════
    private void showAcheterDialog(Article article) {
        Dialog dlg = new Dialog();
        configureDialog(dlg, 400, false);
        dlg.setHeaderTitle(getTranslation("detail.buy.title"));

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(true);

        Span nomArticle = new Span(getTranslation("detail.article", article.getNom() != null ? article.getNom() : "—"));
        nomArticle.getStyle().set("font-size", "0.9rem");
        content.add(nomArticle);

        Span prixFixe = new Span(getTranslation("detail.price", article.getPrix() != null ? article.getPrix() + " CHF" : "—"));
        prixFixe.getStyle().set("font-size", "0.9rem").set("color", "#555");
        content.add(prixFixe);
        content.add(new Hr());

        Span confirm = new Span(getTranslation("detail.buy.question"));
        confirm.getStyle().set("font-size", "0.9rem");
        content.add(confirm);

        Span errMsg = new Span("");
        errMsg.getStyle().set("color", "#CC0000").set("font-size", "0.8rem");
        errMsg.setVisible(false);
        content.add(errMsg);

        dlg.add(content);

        Button annulerBtn = secondaryButton(getTranslation("detail.cancel"));
        annulerBtn.addClickListener(e -> dlg.close());

        Button confirmerBtn = primaryButton(getTranslation("detail.buy"));

        confirmerBtn.addClickListener(e -> {
            try {
                var auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth == null || !(auth.getPrincipal() instanceof OidcUser oidcUser)) {
                    errMsg.setText(getTranslation("detail.buy.needLogin"));
                    errMsg.setVisible(true);
                    return;
                }

                String email = oidcUser.getAttribute("email");
                Integer idAcheteur = referenceService.findPersonneIdByEmail(email);
                if (idAcheteur == null) {
                    errMsg.setText(getTranslation("detail.err.noaccount"));
                    errMsg.setVisible(true);
                    return;
                }

                // ✅ CORRECTION : quantité null protégée, et on s'arrête si le stock est épuisé
                // (avant, la notification "plus disponible" était suivie de "Achat confirmé").
                int stock = article.getQuantite() != null ? article.getQuantite() : 0;
                if (stock - 1 < 0) {
                    Notification.show(
                            getTranslation("detail.buy.outOfStock"),
                            3000,
                            Notification.Position.TOP_CENTER
                    );
                    dlg.close();
                    return;
                }

                int conditionPayement = article.getRefConditionPayement() != null
                        ? article.getRefConditionPayement() : 0;
                int conditionLivraison = article.getRefModeDeLivraison() != null
                        ? article.getRefModeDeLivraison() : 0;

                referenceService.enregistrerAchat(
                        article.getIdArticle().intValue(),
                        idAcheteur,
                        article.getPrix(),
                        article.getQuantite(),
                        conditionPayement,
                        conditionLivraison
                );

                Notification.show(
                        getTranslation("detail.buy.done", String.valueOf(article.getPrix())),
                        3000,
                        Notification.Position.TOP_CENTER
                );
                dlg.close();

            } catch (Exception ex) {
                errMsg.setText(getTranslation("detail.error", String.valueOf(ex.getMessage())));
                errMsg.setVisible(true);
            }
        });

        dlg.getFooter().add(annulerBtn, confirmerBtn);
        dlg.open();
    }

    // ══════════════════════════════════════════════════════════
    //  Dialogue : agenda du vendeur
    //  ✅ Hauteur en dvh, plein écran sur téléphone, vue "liste" par défaut sur petit écran.
    // ══════════════════════════════════════════════════════════
    private void buildCalendrierArticle(String email) {
        Dialog dlg = new Dialog();
        configureDialog(dlg, 900, true);
        dlg.setHeaderTitle(getTranslation("detail.agenda"));
        dlg.setHeight("min(85dvh, 700px)");

        FullCalendar calendar = FullCalendarBuilder.create().build();
        calendar.setSizeFull();
        calendar.setLocale(getLocale());
        calendar.changeView(CalendarViewImpl.DAY_GRID_MONTH);

        List<EvenementCalendrier> evenements = calendrierService
                .findArticlesByVendeurAndStatut(email, article.getIdArticle().intValue());

        evenements.forEach(evt -> {
            Entry entry = new Entry();
            entry.setTitle(evt.getTitre());
            entry.setStart(evt.getDebut());
            entry.setEnd(evt.getFin());
            entry.setColor(evt.getCouleur());
            calendar.getEntryProvider().asInMemory().addEntry(entry);
        });

        calendar.addEntryClickedListener(event -> {
            Entry clicked = event.getEntry();
            Notification.show(
                    getTranslation("detail.visit.title", String.valueOf(clicked.getTitle())),
                    3000,
                    Notification.Position.BOTTOM_CENTER
            );
        });

        Button prev  = new Button(VaadinIcon.ANGLE_LEFT.create(),  e -> calendar.previous());
        Button next  = new Button(VaadinIcon.ANGLE_RIGHT.create(), e -> calendar.next());
        Button today = new Button(getTranslation("detail.today"), e -> calendar.today());

        Select<CalendarView> viewSelect = new Select<>();
        viewSelect.setItems(
                CalendarViewImpl.DAY_GRID_MONTH,
                CalendarViewImpl.TIME_GRID_WEEK,
                CalendarViewImpl.LIST_WEEK
        );
        viewSelect.setValue(CalendarViewImpl.DAY_GRID_MONTH);
        viewSelect.addValueChangeListener(e -> calendar.changeView(e.getValue()));

        // ✅ Sur petit écran (< 600 px), la vue semaine en grille horaire est illisible :
        // on démarre sur la vue liste.
        UI.getCurrent().getPage().executeJs("return window.innerWidth;")
                .then(Integer.class, w -> {
                    if (w != null && w < 600) {
                        viewSelect.setValue(CalendarViewImpl.LIST_WEEK);
                    }
                });

        HorizontalLayout toolbar = new HorizontalLayout(prev, today, next, viewSelect);
        toolbar.setAlignItems(Alignment.CENTER);
        toolbar.getStyle().set("flex-wrap", "wrap");

        VerticalLayout content = new VerticalLayout(toolbar, calendar);
        content.setPadding(false);
        content.setSizeFull();
        content.expand(calendar);
        dlg.add(content);

        Button fermerBtn = primaryButton(getTranslation("detail.close"));
        fermerBtn.addClickListener(e -> dlg.close());
        dlg.getFooter().add(fermerBtn);

        dlg.open();
    }

    // ── Helpers ───────────────────────────────────────────────

    private HorizontalLayout fieldRow(String label, String value) {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(Alignment.BASELINE);
        row.setWidthFull();
        row.getStyle().set("margin-bottom", "3px");

        // Libellé = moitié de la ligne, champ = le reste
        Span lbl = new Span(label);
        lbl.getStyle()
                .set("flex", "0 0 50%")
                .set("min-width", "0")
                .set("font-size", "0.85rem")
                .set("color", "#0000CC");

        TextField val = new TextField();
        val.setValue(value != null ? value : "");
        val.setMinWidth("0");
        val.getStyle().set("flex", "1 1 0");
        val.setReadOnly(true);
        val.getStyle().set("font-size", "0.8rem");

        row.add(lbl, val);
        return row;
    }

    private Div gap() {
        Div d = new Div();
        d.getStyle().set("height", "8px");
        return d;
    }

    private String str(Object o) {
        return o != null ? o.toString() : "—";
    }

    /** Masque la partie locale d'un email : "ab***@domaine.ch" (sans risque si < 2 caractères). */
    private String masquerEmail(String email) {
        if (email == null || !email.contains("@")) return "—";
        int at = email.indexOf('@');
        String local = email.substring(0, at);
        String visible = local.length() >= 2 ? local.substring(0, 2) : local;
        return visible + "***" + email.substring(at);
    }

    // Formate la date de visite renvoyée par ReferenceService.findVisiteurs
    // (chaîne issue de LocalDateTime.toString()). En cas de format inattendu ou de
    // valeur nulle, on affiche "—" plutôt que de laisser planter l'interface.
    private static final DateTimeFormatter DATE_VISITE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private String formatDateVisite(String brute) {
        if (brute == null || brute.isBlank()) {
            return "—";
        }
        try {
            return java.time.LocalDateTime.parse(brute).format(DATE_VISITE_FORMAT);
        } catch (DateTimeParseException e) {
            return brute; // valeur déjà lisible ou format non standard
        }
    }

    /** Bouton "contour" violet utilisé pour les actions de la page. */
    private Button styledButton(String label) {
        Button btn = new Button(label);

        btn.getStyle()
                .set("background", "white")
                .set("border", "1px solid #6100C1")
                .set("color", "#6100C1")
                .set("border-radius", "20px")
                .set("font-size", "0.85rem")
                .set("cursor", "pointer")
                .set("padding", "8px 14px")
                .set("margin", "0")
                .set("white-space", "normal")
                .set("min-height", "40px")
                .set("box-sizing", "border-box");

        return btn;
    }

    /** ✅ Bouton principal des dialogues (remplace les styles #0000CC répétés). */
    private Button primaryButton(String label) {
        Button btn = new Button(label);
        btn.getStyle()
                .set("background-color", "#0000CC")
                .set("color", "white")
                .set("border", "none")
                .set("min-height", "40px")
                .set("cursor", "pointer");
        return btn;
    }

    /** ✅ Bouton secondaire des dialogues (Annuler / Fermer). */
    private Button secondaryButton(String label) {
        Button btn = new Button(label);
        btn.getStyle()
                .set("background-color", "#E0E0E0")
                .set("border", "1px solid #999")
                .set("min-height", "40px")
                .set("cursor", "pointer");
        return btn;
    }

    /** ✅ Boutons ◀ ▶ de pagination : zone tactile d'au moins 44 x 44 px. */
    private void stylePageButton(Button btn) {
        btn.getStyle()
                .set("background", "none")
                .set("border", "1px solid #6100C1")
                .set("color", "#6100C1")
                .set("border-radius", "20px")
                .set("cursor", "pointer")
                .set("padding", "2px 10px")
                .set("min-width", "44px")
                .set("min-height", "44px");
    }
}
