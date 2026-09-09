package com.example.recordz.ui;

import com.example.recordz.model.domain.Article;
import com.example.recordz.service.ArticleService;
import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;

/**
 * Vue catalogue avec recherche et affichage en grille.
 */
@Route(value = "catalogue", layout = MainLayout.class)
@PageTitle("Recordz — Catalogue")
@PermitAll
public class CatalogueView extends VerticalLayout {

    private final ArticleService articleService;
    private final Grid<Article> grid = new Grid<>(Article.class, false);

    public CatalogueView(ArticleService articleService) {
        this.articleService = articleService;
            setSizeFull();
        setPadding(true);

        add(new H2("Catalogue des annonces"));
        add(buildSearchBar());
        add(buildGrid());

        // ✅ Ne charge rien au démarrage — attend une recherche
        // OU charge les 50 derniers articles toutes catégories
        refreshGrid(articleService.findAll(1, 50));
    }

    // ── Barre de recherche ────────────────────────────────────

    private HorizontalLayout buildSearchBar() {
        TextField searchField = new TextField();
        searchField.setPlaceholder("Rechercher un article…");
        searchField.setWidth("400px");

        Button searchBtn = new Button("Rechercher");
        searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchBtn.addClickListener(e ->
                refreshGrid(articleService.search(searchField.getValue()))
        );

        searchField.addValueChangeListener(e -> {
            if (e.getValue().isBlank()) {
                refreshGrid(articleService.findByCategorie(0, 0, 50));
            }
        });

        HorizontalLayout bar = new HorizontalLayout(searchField, searchBtn);
        bar.setAlignItems(Alignment.BASELINE);
        return bar;
    }

    // ── Grille ────────────────────────────────────────────────

    private Grid<Article> buildGrid() {
        // ✅ Ne pas faire add(grid) ici — le add() est dans le constructeur
        grid.setSizeFull();
        grid.getStyle().set("text-decoration", "none");
        grid.addColumn(Article::getNom).setHeader("Nom").setSortable(true).setFlexGrow(2);
        grid.addColumn(Article::getMarque).setHeader("Marque").setSortable(true);
        grid.addColumn(a -> a.getPrix() != null ? a.getPrix() + " CHF" : "—").setHeader("Prix");
        grid.addColumn(Article::getEtat).setHeader("État");
        grid.addColumn(Article::getLang).setHeader("Langue");
        grid.addColumn(a -> a.getVendu() != null && a.getVendu() == 1 ? "Vendu" : "Disponible").setHeader("Statut");
        grid.addColumn(a -> a.getVisites() != null ? a.getVisites() : 0).setHeader("Visites").setSortable(true);
        return grid;
    }

    private void refreshGrid(List<Article> articles) {
        grid.setItems(articles);
    }
}
