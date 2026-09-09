package com.example.recordz.ui;

import com.example.recordz.model.domain.Article;
import com.example.recordz.model.domain.Personne;
import com.example.recordz.security.AuthenticatedUser;
import com.example.recordz.service.ArticleService;
import com.example.recordz.service.PersonneService;
import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;

public class OtherViews {

    // ══════════════════════════════════════════════════════════
    // MesAnnoncesView
    // ══════════════════════════════════════════════════════════

    @Route(value = "mes-annonces", layout = MainLayout.class)
    @PageTitle("Recordz — Mes annonces")
    @PermitAll
    public static class MesAnnoncesView extends VerticalLayout {

        private final ArticleService    articleService;
        private final AuthenticatedUser authenticatedUser;
        private final Grid<Article>     grid = new Grid<>(Article.class, false);

        public MesAnnoncesView(ArticleService articleService,
                               AuthenticatedUser authenticatedUser) {
            this.articleService    = articleService;
            this.authenticatedUser = authenticatedUser;

            setSizeFull();
            setPadding(true);
            add(new H2("Mes annonces"));
            add(buildGrid());
            loadData();
        }

        private Grid<Article> buildGrid() {
            grid.setSizeFull();
            grid.addColumn(Article::getNom).setHeader("Nom").setFlexGrow(2);
            grid.addColumn(Article::getMarque).setHeader("Marque");
            grid.addColumn(a -> a.getPrix() != null ? a.getPrix() + " CHF" : "—").setHeader("Prix");
            grid.addColumn(a -> a.getVendu() != null && a.getVendu() == 1
                    ? "Vendu" : "Disponible").setHeader("Statut");
            grid.addColumn(Article::getDate).setHeader("Date");
            return grid;
        }

        private void loadData() {
            authenticatedUser.get().ifPresentOrElse(
                    p -> grid.setItems(articleService.findByVendeur(p.getIdPersonne())),
                    () -> grid.setItems(List.of())
            );
        }
    }

    // ══════════════════════════════════════════════════════════
    // MesAchatsView
    // ══════════════════════════════════════════════════════════

    @Route(value = "mes-achats", layout = MainLayout.class)
    @PageTitle("Recordz — Mes achats")
    @PermitAll
    public static class MesAchatsView extends VerticalLayout {

        private final AuthenticatedUser authenticatedUser;

        public MesAchatsView(AuthenticatedUser authenticatedUser) {
            this.authenticatedUser = authenticatedUser;
            setSizeFull();
            setPadding(true);
            add(new H2("Mes achats"));

            authenticatedUser.get().ifPresentOrElse(
                    p -> add(new Paragraph("Historique des achats de " + p.getNomUtilisateur())),
                    () -> add(new Paragraph("Connectez-vous pour voir vos achats."))
            );

            // TODO : implémenter CommandeRepository.findByAcheteur()
            add(new Paragraph("Fonctionnalité à venir."));
        }
    }

    // ══════════════════════════════════════════════════════════
    // WishlistView
    // ══════════════════════════════════════════════════════════

    @Route(value = "wishlist", layout = MainLayout.class)
    @PageTitle("Recordz — Wishlist")
    @PermitAll
    public static class WishlistView extends VerticalLayout {

        private final AuthenticatedUser authenticatedUser;

        public WishlistView(AuthenticatedUser authenticatedUser) {
            this.authenticatedUser = authenticatedUser;
            setSizeFull();
            setPadding(true);
            add(new H2("❤️ Ma Wishlist"));

            authenticatedUser.get().ifPresentOrElse(
                    p -> add(new Paragraph("Wishlist de " + p.getNomUtilisateur())),
                    () -> add(new Paragraph("Connectez-vous pour voir votre wishlist."))
            );

            // TODO : implémenter WishRepository
            add(new Paragraph("Fonctionnalité à venir."));
        }
    }

    // ══════════════════════════════════════════════════════════
    // BoutiqueView
    // ══════════════════════════════════════════════════════════

    @Route(value = "boutique", layout = MainLayout.class)
    @PageTitle("Recordz — Ma boutique")
    @PermitAll
    public static class BoutiqueView extends VerticalLayout {

        private final AuthenticatedUser authenticatedUser;

        public BoutiqueView(AuthenticatedUser authenticatedUser) {
            this.authenticatedUser = authenticatedUser;
            setSizeFull();
            setPadding(true);
            add(new H2("🏪 Ma boutique"));

            authenticatedUser.get().ifPresentOrElse(
                    p -> add(new Paragraph("Boutique de " + p.getNomUtilisateur())),
                    () -> add(new Paragraph("Connectez-vous pour gérer votre boutique."))
            );

            // TODO : implémenter BoutiqueRepository
            add(new Paragraph("Fonctionnalité à venir."));
        }
    }

    // ══════════════════════════════════════════════════════════
    // ProfilView
    // ══════════════════════════════════════════════════════════

}
