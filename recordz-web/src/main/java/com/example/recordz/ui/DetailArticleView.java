package com.example.recordz.ui;


import com.example.recordz.model.domain.*;
import com.example.recordz.model.domain.Article;
import com.example.recordz.service.CalendrierService;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.select.Select;
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
import java.util.stream.Collectors;
import com.vaadin.flow.component.timepicker.TimePicker;
import org.vaadin.stefan.fullcalendar.*;

import java.time.LocalTime;
import java.time.Duration;


@Route(value = "detail/:articleId", layout = MainLayout.class)
@PageTitle("Avant-Garde - Détail article")
@PermitAll
public class DetailArticleView extends VerticalLayout implements BeforeEnterObserver {

    private final ArticleService articleService;
    private final ReferenceService referenceService;
    private final PersonneService personneService;
    private final JavaMailSender mailSender;
    private final CalendrierService calendrierService;

    private Article article;
    private Personne personne;

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
    public void beforeEnter(BeforeEnterEvent event) {
        String idStr = event.getRouteParameters().get("articleId").orElse("0");
        try {
            Long id = Long.valueOf(idStr); // CORRECTION 1 : suppression du 's' parasite et extraction de l'id
            article = articleService.findById(Long.valueOf(idStr)).orElseThrow();
            articleService.findByIdAndTrackVisit(id).ifPresentOrElse(
                    article -> {
                        try {
                            var auth = SecurityContextHolder.getContext().getAuthentication();
                            if (auth != null && auth.getPrincipal() instanceof OidcUser oidcUser) {
                                String email = oidcUser.getAttribute("email");
                                String nom = oidcUser.getAttribute("family_name");
                                String prenom = oidcUser.getAttribute("given_name");
                                referenceService.enregistrerVisite(
                                        article.getIdArticle().intValue(),
                                        nom != null ? nom : "",
                                        prenom != null ? prenom : "",
                                        email != null ? email : ""
                                );
                                // CORRECTION 2 : fermeture correcte du bloc if
                                Integer idVendeur = referenceService.findIdVendeur(article.getIdArticle().intValue());
                                personne = personneService.findById(idVendeur.longValue());
                            }
                        } catch (Exception ex) {
                            System.err.println("Erreur enregistrement visite : " + ex.getMessage());
                        }
                        buildView(article);
                    },
                    () -> add(new Paragraph("Article introuvable."))
            );
        } catch (NumberFormatException e) {
            add(new Paragraph("Identifiant invalide."));
        }
    }

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
        body.setSpacing(true);
        body.add(buildLeftColumn(article));
        body.add(buildRightColumn(article));
        add(body);

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
            iframe.setWidth("560px");
            iframe.getElement().setAttribute("frameborder", "0");
            iframe.getElement().setAttribute("allow",
                    "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture");
            iframe.getElement().setAttribute("allowfullscreen", true);
            add(iframe);
        }

        add(new Hr());
        add(buildCommentsSection(article));
    }

    private VerticalLayout buildCommentsSection(Article article) {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(false);
        section.setWidthFull();

        H4 titre = new H4("Questions & Commentaires");
        titre.getStyle().set("color", "#0000CC").set("margin-bottom", "8px");
        section.add(titre);

        var commentaires = referenceService.findCommentaires(article.getIdArticle().intValue());

        if (commentaires.isEmpty()) {
            Span aucun = new Span("Aucun commentaire pour le moment.");
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
            Span loginMsg = new Span("Connectez-vous pour poser une question.");
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

        H4 titreForm = new H4("Poser une question");
        titreForm.getStyle().set("color", "#0000CC").set("margin", "0");
        form.add(titreForm);

        TextField questionField = new TextField("Sujet de la question");
        questionField.setPlaceholder("Ex: Livraison possible ?");
        questionField.setWidthFull();

        TextArea texteField = new TextArea("Détail");
        texteField.setPlaceholder("Décrivez votre question en détail…");
        texteField.setWidthFull();
        texteField.setMinHeight("80px");

        Span errMsg = new Span("");
        errMsg.getStyle().set("color", "#CC0000").set("font-size", "0.8rem");
        errMsg.setVisible(false);

        Button envoyerBtn = styledButton("Envoyer");

        envoyerBtn.addClickListener(e -> {
            String questionVal = questionField.getValue();
            String texteVal    = texteField.getValue();

            if (questionVal == null || questionVal.isBlank()) {
                errMsg.setText("Veuillez saisir un sujet.");
                errMsg.setVisible(true);
                return;
            }
            try {
                var auth = SecurityContextHolder.getContext().getAuthentication();
                if (!(auth.getPrincipal() instanceof OidcUser oidcUser)) {
                    errMsg.setText("Vous devez être connecté.");
                    errMsg.setVisible(true);
                    return;
                }
                String email = oidcUser.getAttribute("email");
                Integer idAuteur = referenceService.findPersonneIdByEmail(email);
                if (idAuteur == null) {
                    errMsg.setText("Votre compte n'est pas enregistré.");
                    errMsg.setVisible(true);
                    return;
                }

                referenceService.enregistrerCommentaire(
                        article.getIdArticle().intValue(),
                        idAuteur,
                        questionVal.trim(),
                        texteVal != null ? texteVal.trim() : ""
                );

                Notification.show("Votre question a été envoyée !", 3000,
                        Notification.Position.TOP_CENTER);

                questionField.clear();
                texteField.clear();
                errMsg.setVisible(false);
                buildView(article);

            } catch (Exception ex) {
                errMsg.setText("Erreur : " + ex.getMessage());
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

        String catLibelle    = "";
        String subCatLibelle = "";
        if (article.getRefCategorie() != null) {
            catLibelle = referenceService.findCategorieLibelle(article.getRefCategorie());
        }
        if (article.getRefSubcategorie() != null) {
            subCatLibelle = referenceService.findSubCategorieLibelle(article.getRefSubcategorie());
        }

        col.add(fieldRow("Catégorie", catLibelle));
        if (subCatLibelle != null && !subCatLibelle.isBlank()) {
            col.add(fieldRow("Sous-catégorie", subCatLibelle));
        }
        col.add(
                fieldRow("Nom",       article.getNom()),
                fieldRow("Fabricant", article.getMarque()),
                fieldRow("Prix", article.getPrix() != null
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
                        fieldRow("Chevaux (CV)",          str(article.getNbCheveaux())),
                        fieldRow("Cylindres",             str(article.getNbCylindre())),
                        fieldRow("Kilométrage",           str(article.getNbKm())),
                        fieldRow("Année de fabrication",  str(article.getAnnee())),
                        fieldRow("1ère immatriculation",  str(article.getPremiereImmatriculation()))
                );

                if (article.getEssenceOuDiesel() != null)
                    referenceService.findAllFuelTypes().stream()
                            .filter(f -> f.id() == article.getEssenceOuDiesel())
                            .findFirst()
                            .ifPresent(f -> col.add(fieldRow("Carburant", f.nom())));

                if (article.getRefBoiteDeVitesse() != null)
                    referenceService.findAllGearboxTypes().stream()
                            .filter(g -> g.id() == article.getRefBoiteDeVitesse())
                            .findFirst()
                            .ifPresent(g -> col.add(fieldRow("Boîte de vitesse", g.nom())));

                col.add(fieldRow("Climatisation",
                        article.getClima() != null && article.getClima() == 1 ? "Oui" : "Non"));
            }

            else if (cat == ArticleCategory.LIVRES) {
                col.add(
                        fieldRow("Auteur",  article.getAuteur()),
                        fieldRow("Éditeur", article.getMarque()),
                        fieldRow("Année",   str(article.getAnnee()))
                );
            }

            else if (cat == ArticleCategory.IMMOBILIER) {
                col.add(
                        fieldRow("Nombre de pièces",    str(article.getNbPiece())),
                        fieldRow("Surface habitable",   str(article.getSurfaceHabitable())),
                        fieldRow("Surface terrain",     str(article.getSuperficieTerrain())),
                        fieldRow("Année construction",  str(article.getAnneeConstruction())),
                        fieldRow("Adresse",             article.getAdresse()),
                        fieldRow("Ville",               article.getLieu()),
                        fieldRow("Code postal",         article.getNpa())
                );
            }

            else if (cat == ArticleCategory.VINS) {
                col.add(fieldRow("Millésime", article.getMillesime()));

                if (article.getRefTypeDeVin() != null)
                    referenceService.findAllWineTypes().stream()
                            .filter(w -> w.id() == article.getRefTypeDeVin())
                            .findFirst()
                            .ifPresent(w -> col.add(fieldRow("Type de vin", w.nom())));

                if (article.getCepage() != null)
                    referenceService.findAllCepages().stream()
                            .filter(w -> w.id() == article.getCepage())
                            .findFirst()
                            .ifPresent(w -> col.add(fieldRow("Cépage", w.nom())));
            }

            else if (cat == ArticleCategory.DVD) {
                col.add(
                        fieldRow("Acteur(s)",    article.getActeurs()),
                        fieldRow("Réalisateur",  article.getRealisateur()),
                        fieldRow("Durée (min)",  str(article.getDuree())),
                        fieldRow("Année",        str(article.getAnnee()))
                );
            }

            else if (cat == ArticleCategory.JEUX) {
                if (article.getRefTypeDeJeux() != null)
                    referenceService.findAllGameTypes().stream()
                            .filter(g -> g.id() == article.getRefTypeDeJeux())
                            .findFirst()
                            .ifPresent(g -> col.add(fieldRow("Type de jeu", g.nom())));
            }

            if (subcatId != null) {
                if (subcatId == ArticleSubCategory.TV_ECRAN_PLAT_ID) {
                    col.add(fieldRow("Dimension (pouces)", str(article.getDimension())));
                    if (article.getRefTypeEcran() != null)
                        referenceService.findAllScreenTypes().stream()
                                .filter(s -> s.id() == article.getRefTypeEcran())
                                .findFirst()
                                .ifPresent(s -> col.add(fieldRow("Type d'écran", s.nom())));
                }
                if (ArticleSubCategory.INFO_PC_SUBCATS.contains(subcatId)) {
                    col.add(
                            fieldRow("Processeur (GHz)", str(article.getProcesseur())),
                            fieldRow("RAM (Go)",          article.getRam()),
                            fieldRow("Disque dur (Go)",   article.getDisqueDur())
                    );
                }
                if (ArticleSubCategory.WEAR_SIZE_SUBCATS.contains(subcatId)) {
                    col.add(fieldRow("Taille", article.getTaille()));
                }
            }
        }

        referenceService.findAllPayements().stream()
                .filter(p -> p.id() == (article.getRefConditionPayement() != null ? article.getRefConditionPayement() : -1))
                .findFirst()
                .ifPresent(p -> col.add(fieldRow("Condition de paiement", p.nom())));

        referenceService.findAllLivraisons().stream()
                .filter(l -> l.id() == (article.getRefModeDeLivraison() != null ? article.getRefModeDeLivraison() : -1))
                .findFirst()
                .ifPresent(l -> col.add(fieldRow("Condition de livraison", l.nom())));

        return col;
    }

    private VerticalLayout buildRightColumn(Article article) {
        VerticalLayout col = new VerticalLayout();
        col.setSpacing(false);
        col.setPadding(false);
        col.setWidth("300px");
        col.getStyle().set("margin-left", "80px");

        col.add(fieldRow("Nombre de vues",    str(article.getVisites())));
        col.add(fieldRow("Nombre d'enchères", str(article.getNbrEnchere())));
        col.add(fieldRow("Quantité en stock", str(article.getQuantite())));
        col.add(gap());

        // ── Vendeur ────────────────────────────────────────────
        Integer idVendeur = referenceService.findIdVendeur(article.getIdArticle().intValue());
        if (idVendeur != null) {
            Personne person = personneService.findById(idVendeur.longValue());
            Anchor auteurLink = new Anchor("/profil/" + person.getNomUtilisateur(), person.getNom());

            Span lbl = new Span("Vendeur : ");
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
        Button listeVisiteursBtn = new Button("👥 Voir les visiteurs");
        listeVisiteursBtn.getStyle()
                .set("background", "none")
                .set("border", "1px solid #6100C1")
                .set("color", "#6100C1")
                .set("border-radius", "20px")
                .set("font-size", "0.8rem")
                .set("cursor", "pointer")
                .set("padding", "4px 12px")
                .set("margin-top", "4px")
                .set("transition", "all 0.2s");
        listeVisiteursBtn.addClickListener(e -> showVisiteursDialog(article));

        // ── Boutons d'action ───────────────────────────────────
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setAlignItems(Alignment.CENTER);
        buttonsLayout.getStyle().set("margin-top", "4px");

        if (article.getEnchere() != null && article.getEnchere() > 0) {

            Double derniereOffre = referenceService.findDerniereOffre(
                    article.getIdArticle().intValue()
            );
            String date = article.getEnchereDateFin();

            col.add(fieldRow("Dernière offre",
                    derniereOffre != null
                            ? String.format("%.2f CHF", derniereOffre)
                            : "Aucune offre"));

            if (date != null) {
                System.out.println("Date fermeture : " + article.getDateFermetureEnchere());
                col.add(fieldRow("Date fermeture enchère", date));
            }
            Button offreBtn = styledButton("Faire une offre");
            offreBtn.addClickListener(e -> showFaireOffreDialog(article, null, derniereOffre));

            Integer catId = article.getRefCategorie();
            if (catId != null && ArticleCategory.fromId(catId) == ArticleCategory.IMMOBILIER) {
                Button visiteBtn = styledButton("Demande de visites");
                visiteBtn.addClickListener(e -> showDemandeDeVisite(article));
                Button visiteCalendrier = styledButton("L'agenda des visites");
                // CORRECTION 3 : parenthèse fermante manquante sur le listener
                visiteCalendrier.addClickListener(e ->
                        buildCalendrierArticle(personne.getEmail()));

                buttonsLayout.add(listeVisiteursBtn, offreBtn, visiteBtn, visiteCalendrier);

            } else {
                buttonsLayout.add(listeVisiteursBtn, offreBtn);
            }

        } else {

            Button offreBtn = styledButton("Acheter");
            offreBtn.addClickListener(e -> showAcheterDialog(article));

            Integer catId = article.getRefCategorie();
            if (catId != null && ArticleCategory.fromId(catId) == ArticleCategory.IMMOBILIER) {
                Button visiteBtn = styledButton("Demande de visites");
                visiteBtn.addClickListener(e -> showDemandeDeVisite(article));

                // ✅ Ajout du bouton calendrier
                Button visiteCalendrier = styledButton("L'agenda des visites");
                visiteCalendrier.addClickListener(e ->
                        buildCalendrierArticle(personne.getEmail()));

                buttonsLayout.add(listeVisiteursBtn, offreBtn, visiteBtn, visiteCalendrier);
            } else {
                buttonsLayout.add(listeVisiteursBtn, offreBtn);
            }
        }        col.add(buttonsLayout);

        return col;
    }

    private void showDemandeDeVisite(Article article) {
        Dialog dlg = new Dialog();
        dlg.setWidth("420px");

        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(false);

        TextField reponseParEmail = new TextField("Réponse par email");
        reponseParEmail.setPlaceholder("Votre email");
        content.add(reponseParEmail);

        List<PaysPhone> pays = buildPaysList();

        ComboBox<PaysPhone> indicatifBox = new ComboBox<>("Indicatif");
        indicatifBox.setItems(pays);
        indicatifBox.setItemLabelGenerator(p -> p.drapeau() + " " + p.indicatif());
        indicatifBox.setWidth("180px");

        pays.stream()
                .filter(p -> p.code().equals("CH"))
                .findFirst()
                .ifPresent(indicatifBox::setValue);

        TextField reponseParTelephone = new TextField("Numéro");
        reponseParTelephone.setPlaceholder("XX XXX XX XX");
        reponseParTelephone.setWidth("200px");

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

        HorizontalLayout phoneLayout = new HorizontalLayout(indicatifBox, reponseParTelephone);
        phoneLayout.setAlignItems(Alignment.BASELINE);
        phoneLayout.setWidthFull();
        content.add(phoneLayout);

        TextField nom = new TextField("Nom");
        nom.setPlaceholder("Votre nom");
        content.add(nom);

        TextField prenom = new TextField("Prénom");
        prenom.setPlaceholder("Votre prénom");
        content.add(prenom);

        TextField adresse = new TextField("Adresse");
        adresse.setPlaceholder("Votre adresse");
        content.add(adresse);

        TextArea commentaire = new TextArea("Commentaire");
        commentaire.setPlaceholder("Votre commentaire");
        content.add(commentaire);

        DatePicker datePicker = new DatePicker("Date de rendez-vous");
        datePicker.setValue(LocalDate.now());
        datePicker.setMin(LocalDate.now());
        datePicker.setLocale(new java.util.Locale("fr", "CH"));
        datePicker.addValueChangeListener(e -> {
            LocalDate selected = e.getValue();
            System.out.println("Date choisie : " + selected);
        });
        content.add(datePicker);

        TimePicker timePicker = new TimePicker("Heure du rendez-vous");
        timePicker.setValue(LocalTime.of(9, 0));
        timePicker.setStep(Duration.ofMinutes(30));
        timePicker.setMin(LocalTime.of(8, 0));
        timePicker.setMax(LocalTime.of(18, 0));
        timePicker.addValueChangeListener(e -> {
            LocalTime selected = e.getValue();
            System.out.println("Heure choisie : " + selected);
        });
        content.add(timePicker);

        Button fermerBtn = new Button("Fermer");
        fermerBtn.getStyle()
                .set("background-color", "#0000CC")
                .set("color", "white")
                .set("width", "90px")
                .set("border", "none");
        fermerBtn.addClickListener(e -> dlg.close());

        Button envoyerBtn = new Button("Envoyer");
        envoyerBtn.getStyle()
                .set("background-color", "#0000CC")
                .set("color", "white")
                .set("width", "90px")
                .set("border", "none");
        envoyerBtn.addClickListener(e -> {
            PaysPhone selected = indicatifBox.getValue();
            String numeroComplet = selected != null
                    ? selected.indicatif() + reponseParTelephone.getValue().replaceAll("[^0-9]", "")
                    : reponseParTelephone.getValue();

            LocalDateTime dateVisite = LocalDateTime.of(
                    datePicker.getValue(),
                    timePicker.getValue()
            );

            try {
                Integer idVendeur = referenceService.findIdVendeur(article.getIdArticle().intValue());
                if (idVendeur != null) {
                    referenceService.enregistrerDemandeVisite(
                            article.getIdArticle().intValue(),
                            idVendeur,
                            reponseParEmail.getValue().trim(),
                            numeroComplet,
                            nom.getValue().trim(),
                            prenom.getValue().trim(),
                            adresse.getValue().trim(),
                            commentaire.getValue().trim(),
                            dateVisite
                    );
                    Personne personne = this.personneService.findById(idVendeur.longValue());
                    if (personne != null) {
                        envoyerEmailDemandeVisite(
                                personne.getEmail(),
                                article.getNom(),
                                nom.getValue() + " " + prenom.getValue(),
                                commentaire.getValue(),
                                reponseParEmail.getValue().trim(),
                                dateVisite
                        );
                    }
                }
                Notification.show("Demande de visite envoyée !", 3000,
                        Notification.Position.TOP_CENTER);
                dlg.close();
            } catch (Exception ex) {
                Notification.show("Erreur : " + ex.getMessage(), 3000,
                        Notification.Position.TOP_CENTER);
            }
        });

        HorizontalLayout buttonsLayout = new HorizontalLayout(envoyerBtn, fermerBtn);
        content.add(buttonsLayout);
        dlg.add(content);
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
        msg.setText("""
                Bonjour """ + email + """
                
                Vous avez une nouvelle demande de visite pour l'article %s
                
                La personne avec le nom suivant %s désire visiter le bien voici son commentaire %s
                
                Voici son email : %s
                
                Il désire visiter le bien pour la date du %s
                
                L'équipe Avant-Garde
                """.formatted(nom, nomPrenom, commentaire, emailDemandeur, dateVisite));
        try {
            mailSender.send(msg);
            Notification.show("Email envoyé");
        } catch (Exception e) {
            System.err.println("Erreur envoi email acheteur : " + e.getMessage());
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
                new PaysPhone("CH", "+41",  "Suisse",        countryCodeToFlag("CH")),
                new PaysPhone("FR", "+33",  "France",        countryCodeToFlag("FR")),
                new PaysPhone("DE", "+49",  "Allemagne",     countryCodeToFlag("DE")),
                new PaysPhone("IT", "+39",  "Italie",        countryCodeToFlag("IT")),
                new PaysPhone("BE", "+32",  "Belgique",      countryCodeToFlag("BE")),
                new PaysPhone("ES", "+34",  "Espagne",       countryCodeToFlag("ES")),
                new PaysPhone("PT", "+351", "Portugal",      countryCodeToFlag("PT")),
                new PaysPhone("GB", "+44",  "Royaume-Uni",   countryCodeToFlag("GB")),
                new PaysPhone("US", "+1",   "États-Unis",    countryCodeToFlag("US")),
                new PaysPhone("CA", "+1",   "Canada",        countryCodeToFlag("CA")),
                new PaysPhone("JP", "+81",  "Japon",         countryCodeToFlag("JP")),
                new PaysPhone("CN", "+86",  "Chine",         countryCodeToFlag("CN")),
                new PaysPhone("BR", "+55",  "Brésil",        countryCodeToFlag("BR")),
                new PaysPhone("AU", "+61",  "Australie",     countryCodeToFlag("AU")),
                new PaysPhone("MA", "+212", "Maroc",         countryCodeToFlag("MA")),
                new PaysPhone("SN", "+221", "Sénégal",       countryCodeToFlag("SN")),
                new PaysPhone("TN", "+216", "Tunisie",       countryCodeToFlag("TN"))
        );
    }

    public String getFullPhoneNumber(ComboBox<PaysPhone> indicatifBox, TextField numeroField) {
        PaysPhone pays = indicatifBox.getValue();
        if (pays == null || numeroField.getValue().isBlank()) return "";
        String raw = numeroField.getValue().replaceAll("[^0-9]", "");
        return pays.indicatif() + raw;
    }

    private String countryCodeToFlag(String countryCode) {
        return countryCode.chars()
                .mapToObj(c -> String.valueOf(Character.toChars(c - 'A' + 0x1F1E6)))
                .collect(Collectors.joining());
    }

    public record PaysPhone(String code, String indicatif, String nom, String drapeau) {
        @Override
        public String toString() {
            return drapeau + " " + nom + " (" + indicatif + ")";
        }
    }

    private void showVisiteursDialog(Article article) {
        Dialog dlg = new Dialog();
        dlg.setWidth("420px");

        var visiteurs = referenceService.findVisiteurs(article.getIdArticle().intValue());
        final int PAGE_SIZE = 5;
        int[] currentPage = {0};

        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(false);

        H4 titre = new H4("👥 Visiteurs de l'article");
        titre.getStyle().set("color", "#0000CC").set("margin", "0").set("font-size", "1rem");
        content.add(titre);

        VerticalLayout listeZone = new VerticalLayout();
        listeZone.setPadding(false);
        listeZone.setSpacing(false);

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

        for (Button btn : new Button[]{prevBtn, nextBtn}) {
            btn.getStyle()
                    .set("background", "none")
                    .set("border", "1px solid #6100C1")
                    .set("color", "#6100C1")
                    .set("border-radius", "20px")
                    .set("cursor", "pointer")
                    .set("padding", "2px 10px");
        }

        Runnable renderPage = () -> {
            listeZone.removeAll();

            if (visiteurs.isEmpty()) {
                Span aucun = new Span("Aucun visiteur enregistré.");
                aucun.getStyle().set("color", "#999").set("font-size", "0.85rem");
                listeZone.add(aucun);
                pagination.setVisible(false);
                return;
            }

            HorizontalLayout tableHeader = new HorizontalLayout();
            tableHeader.setWidthFull();
            tableHeader.getStyle().set("padding", "6px 8px").set("margin-bottom", "4px");
            Span hNom = new Span("Visiteur");
            hNom.getStyle().set("width", "180px").set("font-size", "0.8rem").set("font-weight", "bold");
            Span hEmail = new Span("Email");
            hEmail.getStyle().set("font-size", "0.8rem").set("font-weight", "bold");
            tableHeader.add(hNom, hEmail);
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
                        .set("font-size", "0.85rem").set("width", "150px");
                nomLink.addClickListener(e -> {
                    dlg.close();
                    nomLink.getUI().ifPresent(ui -> ui.navigate("profil/" + username));
                });

                Span emailSpan = new Span(email.contains("@")
                        ? email.substring(0, 2) + "***" + email.substring(email.indexOf('@'))
                        : "—");
                emailSpan.getStyle().set("font-size", "0.78rem").set("color", "#888");

                row.add(avatar, nomLink, emailSpan);
                listeZone.add(row);
            }

            pageInfo.setText((currentPage[0] + 1) + " / " + totalPages);
            prevBtn.setEnabled(currentPage[0] > 0);
            nextBtn.setEnabled(currentPage[0] < totalPages - 1);
            pagination.setVisible(totalPages > 1);
        };

        prevBtn.addClickListener(e -> { currentPage[0]--; renderPage.run(); });
        nextBtn.addClickListener(e -> { currentPage[0]++; renderPage.run(); });

        pagination.add(prevBtn, pageInfo, nextBtn);
        renderPage.run();
        content.add(listeZone, pagination, new Hr());

        Button fermerBtn = new Button("Fermer");
        fermerBtn.setWidthFull();
        fermerBtn.getStyle()
                .set("background-color", "#0000CC")
                .set("color", "white")
                .set("border", "none");
        fermerBtn.addClickListener(e -> dlg.close());
        content.add(fermerBtn);

        dlg.add(content);
        dlg.open();
    }

    private void showFaireOffreDialog(Article article, TextField offreField, Double derniereOffre) {
        Dialog dlg = new Dialog();
        dlg.setWidth("380px");

        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);

        H4 titre = new H4("Faire une offre");
        titre.getStyle().set("color", "#0000CC").set("margin", "0");
        content.add(titre);
        content.add(new Hr());

        Span nomArticle = new Span("Article : " + (article.getNom() != null ? article.getNom() : "—"));
        nomArticle.getStyle().set("font-size", "0.9rem");
        content.add(nomArticle);

        Span prixActuel = new Span("Prix actuel : " + derniereOffre + " CHF");
        prixActuel.getStyle().set("font-size", "0.9rem").set("color", "#555");
        content.add(prixActuel);
        content.add(new Hr());

        TextField montantField = new TextField("Votre offre (CHF)");
        montantField.setWidth("100%");
        montantField.setPlaceholder("Ex: 150.00");
        if (offreField != null && !offreField.getValue().isBlank()) {
            montantField.setValue(offreField.getValue());
        }
        content.add(montantField);

        Span errMsg = new Span("");
        errMsg.getStyle().set("color", "#CC0000").set("font-size", "0.8rem");
        errMsg.setVisible(false);
        content.add(errMsg);

        content.add(new Hr());
        HorizontalLayout btns = new HorizontalLayout();
        btns.setWidthFull();
        btns.setJustifyContentMode(
                com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.END
        );

        Button annulerBtn = new Button("Annuler");
        annulerBtn.getStyle()
                .set("background-color", "#E0E0E0")
                .set("border", "1px solid #999");
        annulerBtn.addClickListener(e -> dlg.close());

        Button confirmerBtn = new Button("Confirmer");
        confirmerBtn.getStyle()
                .set("background-color", "#0000CC")
                .set("color", "white")
                .set("border", "none");

        confirmerBtn.addClickListener(e -> {
            String valeur = montantField.getValue().replace(",", ".");
            if (valeur.isBlank()) {
                errMsg.setText("Veuillez saisir un montant.");
                errMsg.setVisible(true);
                return;
            }
            try {
                double montant = Double.parseDouble(valeur);
                if (derniereOffre != null && montant <= article.getPrix()) {
                    errMsg.setText("Votre offre doit être supérieure au prix actuel ("
                            + derniereOffre + " CHF).");
                    errMsg.setVisible(true);
                    return;
                }

                var auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth == null || !(auth.getPrincipal() instanceof OidcUser oidcUser)) {
                    errMsg.setText("Vous devez être connecté pour faire une offre.");
                    errMsg.setVisible(true);
                    return;
                }

                String email = oidcUser.getAttribute("email");
                Integer idAcheteur = referenceService.findPersonneIdByEmail(email);
                Integer idVendeur  = referenceService.findIdVendeur(article.getIdArticle().intValue());

                if (idAcheteur == null) {
                    errMsg.setText("Votre compte n'est pas enregistré.");
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
                            "Offre de " + montant + " CHF enregistrée !",
                            3000,
                            Notification.Position.TOP_CENTER
                    );
                    dlg.close();
                } else {
                    errMsg.setText("Impossible d'enchérir sur un article vous appartenant.");
                    errMsg.setVisible(true);
                }

            } catch (NumberFormatException ex) {
                errMsg.setText("Montant invalide.");
                errMsg.setVisible(true);
            } catch (Exception ex) {
                errMsg.setText("Erreur : " + ex.getMessage());
                errMsg.setVisible(true);
            }
        });

        btns.add(annulerBtn, confirmerBtn);
        content.add(btns);
        dlg.add(content);
        dlg.open();
    }

    private void showAcheterDialog(Article article) {
        Dialog dlg = new Dialog();
        dlg.setWidth("380px");

        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);

        H4 titre = new H4("Confirmer l'achat");
        titre.getStyle().set("color", "#0000CC").set("margin", "0");
        content.add(titre);
        content.add(new Hr());

        Span nomArticle = new Span("Article : " + (article.getNom() != null ? article.getNom() : "—"));
        nomArticle.getStyle().set("font-size", "0.9rem");
        content.add(nomArticle);

        Span prixFixe = new Span("Prix : " + (article.getPrix() != null ? article.getPrix() + " CHF" : "—"));
        prixFixe.getStyle().set("font-size", "0.9rem").set("color", "#555");
        content.add(prixFixe);
        content.add(new Hr());

        Span confirm = new Span("Souhaitez-vous acheter cet article au prix affiché ?");
        confirm.getStyle().set("font-size", "0.9rem");
        content.add(confirm);

        Span errMsg = new Span("");
        errMsg.getStyle().set("color", "#CC0000").set("font-size", "0.8rem");
        errMsg.setVisible(false);
        content.add(errMsg);

        content.add(new Hr());
        HorizontalLayout btns = new HorizontalLayout();
        btns.setWidthFull();
        btns.setJustifyContentMode(
                com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.END
        );

        Button annulerBtn = new Button("Annuler");
        annulerBtn.getStyle()
                .set("background-color", "#E0E0E0")
                .set("border", "1px solid #999");
        annulerBtn.addClickListener(e -> dlg.close());

        Button confirmerBtn = new Button("Acheter");
        confirmerBtn.getStyle()
                .set("background-color", "#0000CC")
                .set("color", "white")
                .set("border", "none");

        confirmerBtn.addClickListener(e -> {
            try {
                var auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth == null || !(auth.getPrincipal() instanceof OidcUser oidcUser)) {
                    errMsg.setText("Vous devez être connecté pour acheter.");
                    errMsg.setVisible(true);
                    return;
                }

                String email = oidcUser.getAttribute("email");
                Integer idAcheteur = referenceService.findPersonneIdByEmail(email);
                if (idAcheteur == null) {
                    errMsg.setText("Votre compte n'est pas enregistré.");
                    errMsg.setVisible(true);
                    return;
                }

                if (article.getQuantite() - 1 >= 0) {
                    int[] conditionPayement = {article.getRefConditionPayement() != null
                            ? article.getRefConditionPayement() : 0};
                    int[] conditionLivraison = {article.getRefModeDeLivraison() != null
                            ? article.getRefModeDeLivraison() : 0};

                    referenceService.enregistrerAchat(
                            article.getIdArticle().intValue(),
                            idAcheteur,
                            article.getPrix(),
                            article.getQuantite(),
                            conditionPayement[0],
                            conditionLivraison[0]
                    );

                } else {
                    Notification.show(
                            "L'article n'est plus disponible en stock",
                            3000,
                            Notification.Position.TOP_CENTER
                    );
                }
                Notification.show(
                        "Achat confirmé pour " + article.getPrix() + " CHF !",
                        3000,
                        Notification.Position.TOP_CENTER
                );
                dlg.close();

            } catch (Exception ex) {
                errMsg.setText("Erreur : " + ex.getMessage());
                errMsg.setVisible(true);
            }
        });

        btns.add(annulerBtn, confirmerBtn);
        content.add(btns);
        dlg.add(content);
        dlg.open();
    }

    // CORRECTION 4 : buildCalendrierArticle ouvre une Dialog au lieu d'injecter dans la vue
    private void buildCalendrierArticle(String email) {
        Dialog dlg = new Dialog();
        dlg.setWidth("900px");
        dlg.setHeight("600px");

        FullCalendar calendar = FullCalendarBuilder.create().build();
        calendar.setSizeFull();
        calendar.setLocale(Locale.FRENCH);
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
                    "Visite : " + clicked.getTitle(),
                    3000,
                    Notification.Position.BOTTOM_CENTER
            );
        });

        Button prev  = new Button(VaadinIcon.ANGLE_LEFT.create(),  e -> calendar.previous());
        Button next  = new Button(VaadinIcon.ANGLE_RIGHT.create(), e -> calendar.next());
        Button today = new Button("Aujourd'hui", e -> calendar.today());

        Select<CalendarView> viewSelect = new Select<>();
        viewSelect.setItems(
                CalendarViewImpl.DAY_GRID_MONTH,
                CalendarViewImpl.TIME_GRID_WEEK,
                CalendarViewImpl.LIST_WEEK
        );
        viewSelect.setValue(CalendarViewImpl.DAY_GRID_MONTH);
        viewSelect.addValueChangeListener(e -> calendar.changeView(e.getValue()));

        Button fermerBtn = new Button("Fermer", e -> dlg.close());
        fermerBtn.getStyle()
                .set("background-color", "#0000CC")
                .set("color", "white")
                .set("border", "none");

        HorizontalLayout toolbar = new HorizontalLayout(prev, today, next, viewSelect);
        toolbar.setAlignItems(Alignment.CENTER);

        VerticalLayout content = new VerticalLayout(toolbar, calendar, fermerBtn);
        content.setSizeFull();
        content.expand(calendar);

        dlg.add(content);
        dlg.open();
    }

    // ── Helpers ───────────────────────────────────────────────

    private HorizontalLayout fieldRow(String label, String value) {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(Alignment.BASELINE);
        row.getStyle().set("margin-bottom", "3px");

        Span lbl = new Span(label);
        lbl.getStyle()
                .set("width", "185px")
                .set("font-size", "0.85rem")
                .set("color", "#0000CC");

        TextField val = new TextField();
        val.setValue(value != null ? value : "");
        val.setWidth("150px");
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

    private Button styledButton(String label) {
        Button btn = new Button(label);
        btn.getStyle()
                .set("background", "none")
                .set("border", "1px solid #6100C1")
                .set("color", "#6100C1")
                .set("border-radius", "20px")
                .set("font-size", "0.8rem")
                .set("cursor", "pointer")
                .set("padding", "4px 12px")
                .set("margin-top", "4px");
        return btn;
    }
}
