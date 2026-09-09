package com.avantgarde.views;

import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Map;

/**
 * Vue profil d'un vendeur.
 *
 * URL   : /vendeur/{username}[?option=xxx]
 * Ancienne URL CGI : detail_dealer.pl?page=profil_vendeur&username=encore
 *
 * Options supportées via query param ?option= :
 *   (vide)              => fiche + rien
 *   list                => fiche + derniers visiteurs
 *   showbuyedarticle    => fiche + articles achetés   (image7)
 *   evalbuy             => fiche + évaluations acheteur (image8)
 *   viewcomments        => fiche + commentaires        (image9)
 *
 * Données : Map username -> String[] {
 *   enchOuv, enchFerm, ventesOuv, ventesFerm,
 *   evalVentes, evalAchats, nbArticlesAchetes, nbCommentaires
 * }
 */
@Route(value = "vendeur/:username", layout = MainLayout.class)
@PageTitle("Avant-Garde - Profil vendeur")
@PermitAll

public class ProfilVendeurView extends VerticalLayout implements BeforeEnterObserver {

    // -------------------------------------------------------------------------
    // Mock data
    // -------------------------------------------------------------------------

    private static final Map<String, String[]> STATS = Map.of(
        "encore",    new String[]{"0",  "0", "0", "0", "0", "0",  "1",  "1"},
        "alexandre", new String[]{"21", "0", "14","0", "0", "10", "20", "12"}
    );

    // Articles achetés : { vendeur, prix }
    private static final List<String[]> ARTICLES_ACHETES = List.of(
        new String[]{"alexandre", "200"}, new String[]{"alexandre", "200"},
        new String[]{"alexandre", "450"}, new String[]{"alexandre", "200"},
        new String[]{"alexandre", "450"}, new String[]{"alexandre", "400"},
        new String[]{"alexandre", "400"}, new String[]{"alexandre", "900"}
    );

    // Évaluations : { username, note }
    private static final List<String[]> EVALUATIONS_POS = List.of(
        new String[]{"test", "10"}, new String[]{"test", "10"},
        new String[]{"test", "10"}, new String[]{"test", "10"},
        new String[]{"pr0grammer", "10"}
    );

    // Commentaires : { auteur, sujet, texte, date }
    private static final List<String[]> COMMENTAIRES = List.of(
        new String[]{"alexandre", "test",
            "Bonjour,\n\nMon neveu Michael aimerait savoir si la fille est disponible ce soir",
            "2017-11-25 22:09:13"},
        new String[]{"alexandre", "test question", "coucou :)", ""}
    );

    // Derniers visiteurs
    private static final List<String> VISITEURS = List.of(
        "pr0grammer", "pr0grammer", "pr0grammer", "pr0grammer"
    );

    // -------------------------------------------------------------------------

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String username = event.getRouteParameters().get("username").orElse("unknown");
        String option = event.getLocation().getQueryParameters()
            .getParameters().getOrDefault("option", List.of("")).get(0);

        removeAll();
        setPadding(true);
        setSpacing(true);

        String[] stats = STATS.getOrDefault(username,
            new String[]{"0","0","0","0","0","0","0","0"});

        add(buildFicheVendeur(username, stats));

        switch (option) {
            case "list"             -> add(buildDerniersVisiteurs());
            case "showbuyedarticle" -> add(buildArticlesAchetes());
            case "evalbuy"          -> add(buildEvaluations());
            case "viewcomments"     -> add(buildCommentaires());
        }
    }

    // -------------------------------------------------------------------------
    // Fiche principale (image5 + image6)
    // -------------------------------------------------------------------------

    private Div buildFicheVendeur(String username, String[] s) {
        Div card = new Div();
        card.getStyle()
            .set("border", "2px solid #6600CC")
            .set("border-radius", "4px")
            .set("padding", "12px")
            .set("max-width", "520px");

        String base = "/vendeur/" + username;
        card.add(statRowPlain  ("Vendeur",                                       username));
        card.add(statRowLink   ("Liste des derniers visiteurs",                  "Clickez",     base + "?option=list"));
        card.add(statRowField  ("Nombre d'encheres ouvertes",                    s[0]));
        card.add(statRowField  ("Nombre d'encheres fermées",                     s[1]));
        card.add(statRowField  ("Nombre de ventes directes ouvertes",            s[2]));
        card.add(statRowField  ("Nombre de ventes directes fermées",             s[3]));
        card.add(statRowField  ("Nombre d'évaluation des ventes",                s[4]));
        card.add(statRowLink   ("Nombre d'évaluation des achats",                s[5],          base + "?option=evalbuy"));
        card.add(statRowLink   ("Montrer les articles achetés par l'utilisateur",s[6],          base + "?option=showbuyedarticle"));
        card.add(statRowLink   ("Voir les commentaires faits par l'utilisateur", s[7],          base + "?option=viewcomments"));

        return card;
    }

    private HorizontalLayout statRowPlain(String label, String value) {
        HorizontalLayout row = new HorizontalLayout();
        row.getStyle().set("margin-bottom", "4px");

        Span lbl = labelSpan(label);
        Span val = new Span(value);
        val.getStyle().set("font-size", "0.85rem");
        row.add(lbl, val);
        return row;
    }

    private HorizontalLayout statRowField(String label, String value) {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(Alignment.BASELINE);
        row.getStyle().set("margin-bottom", "4px");

        TextField f = new TextField();
        f.setValue(value);
        f.setWidth("100px");
        f.setReadOnly(true);
        f.getStyle().set("font-size", "0.8rem");

        row.add(labelSpan(label), f);
        return row;
    }

    private HorizontalLayout statRowLink(String label, String value, String href) {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(Alignment.BASELINE);
        row.getStyle().set("margin-bottom", "4px");

        Anchor lbl = new Anchor(href, label);
        lbl.getStyle()
            .set("width", "290px").set("font-size", "0.85rem").set("color", "#0000CC");

        TextField f = new TextField();
        f.setValue(value);
        f.setWidth("100px");
        f.setReadOnly(true);
        f.getStyle().set("font-size", "0.8rem");

        row.add(lbl, f);
        return row;
    }

    private Span labelSpan(String text) {
        Span s = new Span(text);
        s.getStyle()
            .set("width", "290px")
            .set("font-size", "0.85rem")
            .set("color", "#0000CC");
        return s;
    }

    // -------------------------------------------------------------------------
    // Sous-vue : derniers visiteurs (image6)
    // -------------------------------------------------------------------------

    private VerticalLayout buildDerniersVisiteurs() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        for (String v : VISITEURS) {
            Span s = new Span(v);
            s.getStyle().set("font-size", "0.85rem").set("display", "block");
            layout.add(s);
        }
        return layout;
    }

    // -------------------------------------------------------------------------
    // Sous-vue : articles achetés (image7)
    // -------------------------------------------------------------------------

    private VerticalLayout buildArticlesAchetes() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);

        // Pagination
        HorizontalLayout pag = new HorizontalLayout();
        pag.add(navLink("First page", "#"), navLink("<-\"Next\"->", "#"));
        layout.add(pag);

        Div table = new Div();
        table.getStyle()
            .set("border", "2px solid #6600CC")
            .set("border-radius", "4px")
            .set("padding", "8px")
            .set("max-width", "500px");

        for (String[] art : ARTICLES_ACHETES) {
            HorizontalLayout row = new HorizontalLayout();
            row.setAlignItems(Alignment.CENTER);
            row.getStyle().set("padding", "4px 0").set("border-bottom", "1px solid #EEE");

            Div img = new Div();
            img.getStyle()
                .set("width", "50px").set("height", "50px")
                .set("background-color", "#DDD").set("flex-shrink", "0");

            Span vendeur = new Span(art[0]);
            vendeur.getStyle().set("width", "120px").set("color", "#0000CC").set("font-size", "0.85rem");

            Span prix = new Span(art[1]);
            prix.getStyle().set("width", "80px").set("font-size", "0.85rem");

            Span nb = new Span("1");
            nb.getStyle().set("width", "40px").set("font-size", "0.85rem");

            row.add(img, vendeur, prix, nb);
            table.add(row);
        }

        layout.add(table);
        return layout;
    }

    // -------------------------------------------------------------------------
    // Sous-vue : évaluations (image8)
    // -------------------------------------------------------------------------

    private VerticalLayout buildEvaluations() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setPadding(false);

        Tab tabPos = new Tab("Evaluations positives");
        Tab tabNeg = new Tab("Evaluations negatives");
        Tabs tabs = new Tabs(tabPos, tabNeg);

        Div content = new Div();
        content.getStyle()
            .set("border", "1px solid #DDD")
            .set("padding", "8px")
            .set("max-width", "500px");

        // Affichage initial : positif
        renderEvaluations(content, EVALUATIONS_POS);

        tabs.addSelectedChangeListener(e -> {
            content.removeAll();
            if (e.getSelectedTab() == tabPos) {
                renderEvaluations(content, EVALUATIONS_POS);
            } else {
                Span msg = new Span("Aucune évaluation négative");
                msg.getStyle().set("font-size", "0.85rem").set("color", "#666");
                content.add(msg);
            }
        });

        layout.add(tabs, content);
        return layout;
    }

    private void renderEvaluations(Div container, List<String[]> evals) {
        container.removeAll();
        Span pag = new Span("<-0->");
        pag.getStyle().set("font-size", "0.85rem");
        container.add(pag);

        for (String[] ev : evals) {
            HorizontalLayout row = new HorizontalLayout();
            Span nom = new Span(ev[0]);
            nom.getStyle().set("width", "200px").set("font-size", "0.85rem");
            Span note = new Span(ev[1]);
            note.getStyle().set("font-size", "0.85rem");
            row.add(nom, note);
            container.add(row);
        }
    }

    // -------------------------------------------------------------------------
    // Sous-vue : commentaires (image9)
    // -------------------------------------------------------------------------

    private VerticalLayout buildCommentaires() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setPadding(false);

        Span pag = new Span("<-00->");
        pag.getStyle().set("font-size", "0.85rem");
        layout.add(pag);

        for (String[] c : COMMENTAIRES) {
            Div card = new Div();
            card.getStyle()
                .set("border", "1px solid #6600CC")
                .set("border-radius", "4px")
                .set("padding", "10px")
                .set("max-width", "420px");

            Span auteur = new Span(c[0]);
            auteur.getStyle().set("font-weight", "bold").set("font-size", "0.85rem").set("display", "block");
            card.add(auteur);

            Span sujet = new Span(c[1]);
            sujet.getStyle().set("font-size", "0.85rem").set("display", "block");
            card.add(sujet);

            Paragraph texte = new Paragraph(c[2]);
            texte.getStyle().set("font-size", "0.85rem").set("color", "#0000CC");
            card.add(texte);

            if (!c[3].isEmpty()) {
                Span date = new Span(c[3]);
                date.getStyle().set("font-size", "0.8rem").set("color", "#555").set("display", "block");
                card.add(date);
            }

            layout.add(card);
        }
        return layout;
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private Anchor navLink(String text, String href) {
        Anchor a = new Anchor(href, text);
        a.getStyle().set("color", "#0000CC").set("font-size", "0.85rem");
        return a;
    }
}
