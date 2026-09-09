package com.example.recordz.ui;
import com.example.recordz.model.domain.Article;
import com.example.recordz.model.domain.Personne;
import com.example.recordz.security.AuthenticatedUser;
import com.example.recordz.service.ArticleService;
import com.example.recordz.service.PersonneService;
import com.example.recordz.service.ReferenceService;
import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;

@Route(value = "profil/:username", layout = MainLayout.class)
@PageTitle("Recordz — Profil")
@PermitAll
public class ProfilView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;
    private final PersonneService personneService;
    private final ReferenceService referenceService;
    private final ArticleService  articleService;
    // Zone dynamique sous la fiche vendeur
    private final Div detailZone = new Div();

    public ProfilView(AuthenticatedUser authenticatedUser,
                      PersonneService personneService,
                      ReferenceService referenceService,
                      ArticleService articleService) {
        this.authenticatedUser = authenticatedUser;
        this.personneService   = personneService;
        this.referenceService  = referenceService;
        this.articleService    = articleService;
        setSizeFull();
        setPadding(true);
        setSpacing(false);

    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String username = event.getRouteParameters().get("username").orElse(null);
        if (username == null) {
            return;
        }

        // Charger la personne depuis l'URL, pas l'utilisateur connecté
        Personne personne = personneService.findByNomUtilisateur(username);
        if (personne == null) {
            add(new Paragraph("Profil introuvable."));
            return;
        }

        // Vider ce qui a été ajouté dans le constructeur
        removeAll();
        H2 titre = new H2("👤 Profil de " + personne.getNomUtilisateur());
        titre.getStyle().set("color", "#6100C1").set("margin-bottom", "16px");
        add(titre);

        buildForm(personne);
    }



    private void buildForm(Personne personne) {
        // ── Table infos personnelles ───────────────────────────
        Div table = new Div();
        table.getStyle()
                .set("border-radius", "20px")
                .set("border", "1px solid #6100C1")
                .set("overflow", "hidden")
                .set("width", "800px")
                .set("margin-bottom", "24px");


        // ── Fiche vendeur ──────────────────────────────────────
        ReferenceService.ProfilStats stats = referenceService.findProfilStats(personne.getEmail(), personne.getNomUtilisateur());
        add(buildFicheVendeur(personne.getEmail(), stats, personne));

        // ── Zone dynamique ─────────────────────────────────────
        detailZone.getStyle().set("margin-top", "24px").set("width", "800px");
        add(detailZone);
    }

    // ── Fiche vendeur ─────────────────────────────────────────
    private Div buildFicheVendeur(String email, ReferenceService.ProfilStats s, Personne personne) {
        Div table = new Div();
        table.getStyle()
                .set("border-radius", "20px")
                .set("border", "1px solid #6100C1")
                .set("overflow", "visible")  // ✅
                .set("width", "800px");

        table.add(buildTableHeader());

        Integer idPersonne = referenceService.findPersonneIdByUsername(email);

        table.add(rowClickable("Nombre d'encheres ouvertes",        str(s.enchOuv()),     () -> showEncheres(idPersonne)));
//        table.add(rowClickable   ("Nombre d'enchères ouvertes",        str(s.enchOuv()))),  () -> showEncheres(idPersonnee)));
        table.add(rowField   ("Nombre d'enchères fermées",         str(s.enchFerm())));
        table.add(rowClickable("Nombre de ventes directes ouvertes",  str(s.ventesOuv()),   () -> showVentes(idPersonne, false)));
        table.add(rowClickable("Nombre de ventes directes fermées",   str(s.ventesFerm()),  () -> showVentes(idPersonne, true)));
        table.add(rowField   ("Nombre d'évaluations des ventes",   str(s.evalVentes())));
        table.add(rowClickable("Nombre d'évaluations des achats",  str(s.evalAchats()),     () -> showEvalAchats(idPersonne)));
        table.add(rowClickable("Articles achetés par l'utilisateur", str(s.nbArticlesAchetes()), () -> showArticlesAchetes(idPersonne)));
        table.add(rowClickable("Commentaires faits par l'utilisateur", str(s.nbCommentaires()), () -> showCommentaires(idPersonne)));
        return table;
    }

    private void showEvaluationnVentes(Integer idPersonne, boolean vendues) {
        detailZone.removeAll();
        detailZone.add(sectionTitle(vendues ? "Evaluation des ventes" : "Evaluation des ventes"));

        var articles = referenceService.findVentesDirectes(idPersonne, vendues);
        if (articles.isEmpty()) {
            detailZone.add(emptyMsg("Aucune vente."));
            return;
        }

        detailZone.add(buildArticlesTable(articles));
    }


    private HorizontalLayout rowField(String label, String value) {
        HorizontalLayout row = baseRow();  // ✅ même base que rowClickable

        Span lbl = new Span(label);
        lbl.getStyle()
                .set("color", "#0000CC")
                .set("font-size", "0.85rem")
                .set("width", "340px");    // ✅ même largeur que le Anchor dans rowClickable

        com.vaadin.flow.component.textfield.TextField f =
                new com.vaadin.flow.component.textfield.TextField();
        f.setValue(value != null ? value : "");
        f.setWidth("80px");
        f.setReadOnly(true);
        f.getStyle().set("font-size", "0.8rem");

        row.add(lbl, f);   // ✅ label et valeur bien ajoutés
        return row;
    }    // ── Affichages dynamiques ─────────────────────────────────

    private void showVisiteurs(String email) {
        detailZone.removeAll();
        detailZone.add(sectionTitle("Derniers visiteurs"));

        var visiteurs = referenceService.findVisiteursVendeur(email);
        if (visiteurs.isEmpty()) {
            detailZone.add(emptyMsg("Aucun visiteur enregistré."));
            return;
        }

        Div t = newTable();
        t.add(tableHeader("Nom", "Prénom"));
        for (String[] v : visiteurs) {
            t.add(tableRow(v[0], v[1]));
        }
        detailZone.add(t);
    }

    private void showEncheres( Integer idPersonne) {
        detailZone.removeAll();
        detailZone.add(sectionTitle("Encheres ouvertes"));

        var articles = referenceService.findVentesEncheres(idPersonne);
        if (articles.isEmpty()) {
            detailZone.add(emptyMsg("Aucune vente."));
            return;
        }
        detailZone.add(buildArticlesTable(articles));
    }
    private void showVentes(Integer idPersonne, boolean vendues) {
        detailZone.removeAll();
        detailZone.add(sectionTitle(vendues ? "Ventes directes fermées" : "Ventes directes ouvertes"));

        var articles = referenceService.findVentesDirectes(idPersonne, vendues);
        if (articles.isEmpty()) {
            detailZone.add(emptyMsg("Aucune vente."));
            return;
        }
        detailZone.add(buildArticlesTable(articles));
    }

    private void showEvalAchats(Integer idPersonne) {
        detailZone.removeAll();
        detailZone.add(sectionTitle("Évaluations des achats"));

        var evals = referenceService.findVEvalAAchatForProfil(idPersonne);
        if (evals.isEmpty()) {
            detailZone.add(emptyMsg("Aucune évaluation."));
            return;
        }
        detailZone.add(buildArticlesEvaluation(evals));
    }

    private void showArticlesAchetes(Integer idPersonne) {
        detailZone.removeAll();
        detailZone.add(sectionTitle("Articles achetés"));

        var articles = referenceService.findArticlesAchetes(idPersonne);
        if (articles.isEmpty()) {
            detailZone.add(emptyMsg("Aucun article acheté."));
            return;
        }
        detailZone.add(buildArticlesTable(articles));
    }

    private void showCommentaires(Integer idPersonne) {
        detailZone.removeAll();
        detailZone.add(sectionTitle("Commentaires faits"));

        var comments = referenceService.findCommentairesByPersonneId(idPersonne);
        if (comments.isEmpty()) {
            detailZone.add(emptyMsg("Aucun commentaire."));
            return;
        }
        Div t = newTable();
        t.add(tableHeader("Date", "Question", "Texte", "Article"));
        for (String[] c : comments) {
            HorizontalLayout row = new HorizontalLayout();
            row.getStyle().set("padding", "6px 8px").set("border-bottom", "1px solid #EEE");

            // Colonnes texte
            for (int i = 0; i < 3; i++) {
                Span s = new Span(c[i] != null ? c[i] : "—");
                s.getStyle().set("width", "200px").set("font-size", "0.85rem");
                row.add(s);
            }

            // Lien vers l'article
            Anchor lien = new Anchor("/detail/" + c[3], "Voir l'article");
            lien.getStyle()
                    .set("color", "#0000CC")
                    .set("font-size", "0.85rem")
                    .set("text-decoration", "none");
            row.add(lien);
            t.add(row);
        }
        detailZone.add(t);
    }
    // ── Builder table articles (image, nom, prix) ─────────────
    private Div buildArticlesTable(java.util.List<String[]> articles) {
        Div t = newTable();
        t.add(tableHeader("Image", "Nom de l'article", "Prix CHF"));

        for (String[] art : articles) {
            HorizontalLayout row = new HorizontalLayout();
            row.setAlignItems(Alignment.CENTER);
            row.getStyle().set("padding", "6px 8px");

            // Image — 200px comme le header
            Div imgBox = new Div();
            imgBox.getStyle()
                    .set("width", "200px")      // ← même que header
                    .set("min-width", "200px")
                    .set("height", "60px");
            if (art.length > 3 && art[3] != null && !art[3].isBlank()) {
                Image img = new Image("/images/articles/" + art[3], art[1]);
                img.getStyle().set("width", "60px").set("height", "60px").set("object-fit", "cover");
                imgBox.add(img);
            } else {
                imgBox.getStyle().set("background-color", "#DDD");
            }

            // Nom — 200px comme le header
            Anchor nom = new Anchor("/detail/" + art[0], art[1] != null ? art[1] : "—");
            nom.getStyle()
                    .set("width", "200px")      // ← même que header
                    .set("min-width", "200px")
                    .set("color", "#0000CC")
                    .set("text-decoration", "none")
                    .set("font-size", "0.85rem");

            // Prix — 200px comme le header
            Span prix = new Span(art[2] != null ? art[2] + " CHF" : "—");
            prix.getStyle()
                    .set("width", "200px")      // ← même que header
                    .set("min-width", "200px")
                    .set("font-size", "0.85rem");
// Nom — 200px comme le header
            row.add(imgBox, nom, prix);
            t.add(row);
        }
        return t;
    }
    // ── Helpers UI ────────────────────────────────────────────



    private HorizontalLayout rowClickable(String label, String value, Runnable onClick) {
        HorizontalLayout row = baseRow();

        // ✅ Span cliquable au lieu d'un Anchor
        Span lbl = new Span(label);
        lbl.getStyle()
                .set("color", "#0000CC")
                .set("font-size", "0.85rem")
                .set("cursor", "pointer")
                .set("width", "340px")
                .set("text-decoration", "none");
        // ✅ Simple addClickListener, pas de Anchor ni preventDefault
        lbl.addClickListener(e -> onClick.run());

        com.vaadin.flow.component.textfield.TextField f =
                new com.vaadin.flow.component.textfield.TextField();
        f.setValue(value != null ? value : "");
        f.setWidth("80px");
        f.setReadOnly(true);
        f.getStyle().set("font-size", "0.8rem");

        row.add(lbl, f);
        return row;
    }
    private HorizontalLayout baseRow() {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(Alignment.CENTER);
        row.getStyle()
                .set("padding", "6px 8px")
                //.set("border-bottom", "1px solid #EEE")
                .set("width", "100%")
                .set("min-height", "40px")       // ✅ hauteur minimale garantie
                .set("justify-content", "space-between");
        return row;
    }
    private HorizontalLayout buildTableHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.getStyle()
                .set("font-weight", "bold").set("color", "#0000CC")
                .set("padding", "4px 8px").set("width", "100%");
                //.set("border-bottom", "2px solid #6600CC");
        header.add(cell("Statistique", "340px"), cell("Valeur", "120px"));
        return header;
    }

    private Div newTable() {
        Div t = new Div();
        t.getStyle()
                .set("border-radius", "20px")
                .set("border", "1px solid #6100C1")
                .set("overflow", "visible")  // ✅
                .set("width", "800px");
        return t;
    }

    private HorizontalLayout tableHeader(String... cols) {
        HorizontalLayout h = new HorizontalLayout();
        h.getStyle()
                .set("font-weight", "bold").set("color", "#0000CC")
                .set("padding", "4px 8px");
                //.set("border-bottom", "2px solid #6600CC");
        for (String col : cols) h.add(cell(col, "200px"));
        return h;
    }

    private HorizontalLayout tableRow(String... vals) {
        HorizontalLayout row = new HorizontalLayout();
        row.getStyle().set("padding", "6px 8px").set("border-bottom", "1px solid #EEE");
        for (String val : vals) {
            Span s = new Span(val != null ? val : "—");
            s.getStyle().set("width", "200px").set("font-size", "0.85rem");
            row.add(s);
        }
        return row;
    }

    private H4 sectionTitle(String text) {
        H4 h = new H4(text);
        h.getStyle().set("color", "#6100C1").set("margin", "16px 0 8px 0");
        return h;
    }

    private Span emptyMsg(String text) {
        Span s = new Span(text);
        s.getStyle().set("font-size", "0.85rem").set("color", "#999");
        return s;
    }

    private Span cell(String text, String width) {
        Span s = new Span(text);
        s.getStyle().set("width", width).set("font-size", "0.9rem");
        return s;
    }

    private Span labelSpan(String text) {
        Span s = new Span(text);
        s.getStyle().set("min-width", "320px").set("width", "320px")
                .set("font-size", "0.85rem").set("color", "#0000CC");
        return s;
    }

    private Span valueSpan(String text) {
        Span s = new Span(text != null ? text : "");
        s.getStyle().set("font-size", "0.85rem");
        return s;
    }

    private String str(int i) { return String.valueOf(i); }

    private HorizontalLayout infoRow(String label, String value) {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(Alignment.CENTER);
        row.getStyle().set("padding", "6px 8px").set("border-bottom", "1px solid #EEE")
                .set("width", "100%").set("background-color", "white");
        Span lbl = new Span(label);
        lbl.getStyle().set("width", "200px").set("min-width", "200px")
                .set("font-size", "0.85rem").set("color", "#0000CC");
        com.vaadin.flow.component.textfield.TextField f =
                new com.vaadin.flow.component.textfield.TextField();
        f.setValue(value != null ? value : "");
        f.setWidth("300px");
        row.add(lbl, f);
        return row;
    }

    private void showVisiteursDialog(Personne p) {
        Dialog dlg = new Dialog();
        dlg.setWidth("420px");

        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(false);

        // ── En-tête coloré ────────────────────────────────────
        Div header = new Div();
        H4 titre = new H4("👥 Derniers visiteurs");
        titre.getStyle()
                .set("color", "white")
                .set("margin", "0")
                .set("font-size", "1rem");
        header.add(titre);
        content.add(header);
// ── Liste des visiteurs ───────────────────────────────
        var visiteurs = referenceService.findVisiteursVendeur(p.getEmail());

        if (visiteurs.isEmpty()) {
            Div emptyBox = new Div();
            emptyBox.getStyle()
                    .set("text-align", "center")
                    .set("padding", "20px")
                    .set("color", "#999")
                    .set("font-size", "0.85rem");
            emptyBox.setText("Aucun visiteur enregistré.");
            content.add(emptyBox);
        } else {
            // ── En-tête tableau ───────────────────────────────
            HorizontalLayout tableHeader = new HorizontalLayout();
            tableHeader.setWidthFull();
            tableHeader.getStyle()
                    //.set("background-color", "#F3E8FF")
                    .set("border-radius", "6px")
                    .set("padding", "6px 8px")
                    .set("margin-bottom", "4px");

            Span hNom = new Span("Visiteur");
            hNom.getStyle()
                    .set("width", "180px")
                    .set("font-size", "0.8rem")
                    .set("font-weight", "bold");
            //.set("color", "#6100C1");

            Span hEmail = new Span("Email");
            hEmail.getStyle()
                    .set("font-size", "0.8rem")
                    .set("font-weight", "bold");
            //.set("color", "#6100C1");

            tableHeader.add(hNom, hEmail);
            content.add(tableHeader);

            // ── Lignes ────────────────────────────────────────
            for (String[] v : visiteurs) {
                HorizontalLayout row = new HorizontalLayout();
                row.setAlignItems(Alignment.CENTER);
                row.setWidthFull();
                row.getStyle()
                        .set("padding", "6px 8px")
                        .set("border-bottom", "1px solid #F0E6FF")
                        .set("border-radius", "4px");

                String nomComplet = (v[0] != null ? v[0] : "") + " " + (v[1] != null ? v[1] : "");
                String email = v[2] != null ? v[2] : "";
                String username = email.contains("@")
                        ? email.substring(0, email.indexOf('@'))
                        : nomComplet.trim();

                // Avatar initiales
                Div avatar = new Div();
                String initiales = (!nomComplet.isBlank() ? nomComplet.substring(0, 1) : "?").toUpperCase();
                avatar.setText(initiales);
                avatar.getStyle()
                        .set("background-color", "white")
                        .set("color", "white")
                        .set("border", "none");


                // Nom cliquable
                Span nomLink = new Span(nomComplet.trim());
                nomLink.getStyle()
                        .set("background-color", "white")
                        .set("color", "black")
                        .set("border", "none");


                nomLink.addClickListener(e -> {
                    dlg.close();
                    nomLink.getUI().ifPresent(ui -> ui.navigate("profil/" + username));
                });

                // Email masqué
                Span emailSpan = new Span(email.contains("@")
                        ? email.substring(0, 2) + "***" + email.substring(email.indexOf('@'))
                        : "—");
                emailSpan.getStyle()
                        .set("font-size", "0.78rem")
                        .set("color", "#888");

                row.add(avatar, nomLink, emailSpan);
                content.add(row);
            }
        }

        // ── Bouton fermer ─────────────────────────────────────
        content.add(new Hr());
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

    private Div buildArticlesEvaluation(java.util.List<String[]> articles) {
        Div t = newTable();

        // Header — mêmes largeurs que les cellules de données
        HorizontalLayout header = new HorizontalLayout();
        header.setSpacing(false);
        header.getStyle()
                .set("font-weight", "bold").set("color", "#0000CC")
                .set("padding", "4px 8px");
        header.add(cell("Image",            "100px"));
        header.add(cell("Nom de l'article", "300px"));
        header.add(cell("Prix CHF",         "150px"));
        header.add(cell("Note",             "80px"));
        t.add(header);

        for (String[] art : articles) {
            HorizontalLayout row = new HorizontalLayout();
            row.setSpacing(false);                          // ← clé de l'alignement
            row.setAlignItems(Alignment.CENTER);
            row.getStyle()
                    .set("padding", "6px 8px")
                    .set("border-top", "1px solid #EEE");

            // Image — 100px
            Div imgBox = new Div();
            imgBox.getStyle()
                    .set("width", "100px").set("min-width", "100px")
                    .set("height", "60px").set("flex-shrink", "0");
            if (art.length > 3 && art[3] != null && !art[3].isBlank()) {
                Image img = new Image("/images/articles/" + art[3], art[1]);
                img.getStyle().set("width", "60px").set("height", "60px").set("object-fit", "cover");
                imgBox.add(img);
            } else {
                imgBox.getStyle().set("background-color", "#DDD");
            }

            // Nom — 300px
            Anchor nom = new Anchor("/detail/" + art[0], art[1] != null ? art[1] : "—");
            nom.getStyle()
                    .set("width", "300px").set("min-width", "300px")
                    .set("color", "#0000CC").set("font-size", "0.85rem")
                    .set("overflow", "hidden").set("white-space", "nowrap")
                    .set("text-decoration", "none")

                    .set("text-overflow", "ellipsis");

            // Prix — 150px
            Span prix = new Span(art[2] != null ? art[2] + " CHF" : "—");
            prix.getStyle()
                    .set("width", "150px").set("min-width", "150px")
                    .set("font-size", "0.85rem");

            // Note — 80px
            Integer note = null;
            try {
                note = referenceService.findNoteForArticle(Integer.valueOf(art[0]));
            } catch (Exception ignored) {}
            Span noteSpan = new Span(note != null ? note.toString() : "—");
            noteSpan.getStyle()
                    .set("width", "80px").set("min-width", "80px")
                    .set("font-size", "0.85rem");

            row.add(imgBox, nom, prix, noteSpan);
            t.add(row);
        }
        return t;
    }
}