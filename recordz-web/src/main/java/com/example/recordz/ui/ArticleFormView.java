package com.example.recordz.ui;

import com.example.recordz.model.domain.dto.ArticleFormData;
import com.example.recordz.service.ArticleDynamicDataService;
import com.example.recordz.service.ArticleSubmitService;
import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "ajouter-article", layout = MainLayout.class)
@PageTitle("Ajouter un article")
public class ArticleFormView extends VerticalLayout {

    public ArticleFormView(ArticleDynamicDataService dataService,
                           ArticleSubmitService submitService) {

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
            // 1. Validationa
            if (!form.isValid()) {
                Notification.show("Veuillez corriger les erreurs avant de publier.",
                    3000, Notification.Position.TOP_CENTER);
                return;
            }

            // 2. Collecte + persistance
            try {
                ArticleFormData data = form.collectValues();
                int newId = submitService.save(data);

                Notification notif = Notification.show(
                    "Article #" + newId + " publié avec succès !",
                    4000, Notification.Position.TOP_CENTER);
                notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                // Redirection vers la fiche de l'article
                saveButton.getUI().ifPresent(ui -> ui.navigate("detail/" + newId));

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
