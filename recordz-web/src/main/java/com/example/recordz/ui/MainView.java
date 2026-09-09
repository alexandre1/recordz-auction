package com.example.recordz.ui;

import com.example.recordz.model.domain.Article;
import com.example.recordz.service.ArticleService;
import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Avant-Garde")
@PermitAll

public class MainView extends VerticalLayout implements HasUrlParameter<Integer> {

    private static final int PAGE_SIZE = 10;

    private final ArticleService articleService;

    public MainView(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer page) {
        int currentPage = (page != null && page > 0) ? page : 1;

        List<Article> articles = articleService.findAllTries(currentPage, PAGE_SIZE);
        int total = articleService.countAll();
        int totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        removeAll();
        setPadding(true);
        setSpacing(false);
        add(buildPagination(currentPage, totalPages));
        add(buildArticlesTable(articles));
        add(buildPagination(currentPage, totalPages));
    }

    private HorizontalLayout buildPagination(int currentPage, int totalPages) {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(Alignment.CENTER);
        row.getStyle().set("padding", "12px 0").set("gap", "6px");

        row.add(paginationBtn("«", "/1",                        currentPage == 1));
        row.add(paginationBtn("‹", "/" + (currentPage - 1),    currentPage == 1));

        int start = Math.max(1, currentPage - 2);
        int end   = Math.min(totalPages, currentPage + 2);

        if (start > 1) {
            row.add(paginationBtn("1", "/1", false));
            if (start > 2) {
                Span dots = new Span("…");
                dots.getStyle().set("color", "#6100C1").set("font-size", "0.85rem");
                row.add(dots);
            }
        }

        for (int i = start; i <= end; i++) {
            boolean isActive = (i == currentPage);
            Div pageBtn = new Div();
            pageBtn.setText(String.valueOf(i));
            pageBtn.getStyle()
                    .set("width", "32px").set("height", "32px")
                    .set("display", "flex")
                    .set("align-items", "center")
                    .set("justify-content", "center")
                    .set("border-radius", "50%")
                    .set("font-size", "0.85rem")
                    .set("cursor", isActive ? "default" : "pointer")
                    .set("font-weight", isActive ? "bold" : "normal")
                    .set("background-color", isActive ? "#6100C1" : "transparent")
                    .set("color", isActive ? "white" : "#6100C1")
                    .set("border", isActive ? "none" : "1px solid #6100C1");

            if (!isActive) {
                int p = i;
                pageBtn.getElement().addEventListener("click", e ->
                        // ✅ Format correct pour HasUrlParameter<Integer>
                        pageBtn.getUI().ifPresent(ui -> ui.navigate("/" + p))
                );
            }
            row.add(pageBtn);
        }

        if (end < totalPages) {
            if (end < totalPages - 1) {
                Span dots = new Span("…");
                dots.getStyle().set("color", "#6100C1").set("font-size", "0.85rem");
                row.add(dots);
            }
            row.add(paginationBtn(String.valueOf(totalPages), "/" + totalPages, false));
        }

        row.add(paginationBtn("›", "/" + (currentPage + 1), currentPage == totalPages));
        row.add(paginationBtn("»", "/" + totalPages,        currentPage == totalPages));

        Span info = new Span("Page " + currentPage + " / " + totalPages);
        info.getStyle()
                .set("font-size", "0.78rem").set("color", "#6100C1")
                .set("margin-left", "8px").set("align-self", "center");
        row.add(info);

        return row;
    }
    // ── Helper bouton pagination ──────────────────────────────
    private Anchor paginationBtn(String label, String href, boolean disabled) {
        Anchor btn = new Anchor(disabled ? "#" : href, label);
        btn.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("width", "32px")
                .set("height", "32px")
                .set("border-radius", "50%")
                .set("border", "1px solid " + (disabled ? "#DDD" : "#6100C1"))
                .set("color", disabled ? "#CCC" : "#6100C1")
                .set("font-size", "1rem")
                .set("text-decoration", "none")
                .set("pointer-events", disabled ? "none" : "auto")
                .set("font-weight", "bold");
        return btn;
    }
    private Div buildArticlesTable(List<Article> articles) {
        Div table = new Div();
        //   border-radius: 25px;border: 2px solid #6100C1;padding: 20px
        table.add(buildTableHeader());
        table.getStyle()
                .set("border-radius", "20px")
                .set("border", "1px solid #6100C1")
                .set("overflow", "hidden")
                .set("padding-left", "8px");
        ;
        if (articles.isEmpty()) {
            Paragraph empty = new Paragraph("Aucun article disponible.");
            empty.getStyle().set("color", "#999").set("font-size", "0.9rem");
            table.add(empty);
        } else {
            for (Article art : articles) {
                table.add(buildArticleRow(art));
            }
        }
        return table;
    }

    private HorizontalLayout buildTableHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("articles-header");
        header.getStyle()
                .set("font-weight", "bold")
                .set("color", "#0000CC")
                .set("padding", "4px 8px")       // ← padding horizontal
                .set("width", "100%");
                //.set("border-bottom", "2px solid #6600CC");  // ← ligne séparatrice
        header.add(
                cell("Image",                 "80px"),   // ← doit matcher imgBox (60px + marge)
                cell("Nom de l'article",      "210px"),
                cell("Marque",                "165px"),
                cell("Prix CHF", "130px"),
                cell("Statut",           "130px")
        );
        return header;
    }
    private HorizontalLayout buildArticleRow(Article art) {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(Alignment.CENTER);
        row.getStyle()
                .set("color", "#0000CC")
                .set("font-size", "0.85rem")
                .set("text-decoration", "none");

        // Image
        Div imgBox = new Div();
        imgBox.getStyle()
                .set("width", "60px")
                .set("height", "60px")
                .set("flex-shrink", "0");

        String pochette = art.getPochette();
        if (pochette != null && !pochette.isBlank()) {
            Image img = new Image("/images/articles/" + pochette, art.getNom());
            img.getStyle()
                    .set("width", "60px")
                    .set("height", "60px")
                    .set("object-fit", "cover");
            imgBox.add(img);
        } else {
            imgBox.getStyle()
                    .set("background-color", "#DDD")
                    .set("border", "1px solid #CCC");
        }

        // Nom
        String nom = art.getNom() != null ? art.getNom() : "(sans nom)";
        Anchor nomLink = new Anchor("/detail/" + art.getIdArticle(), nom);
        nomLink.getStyle()
                .set("color", "#0000CC")
                .set("width", "210px")
                .set("font-size", "0.9rem")
                .set("overflow", "hidden")
                .set("white-space", "nowrap")
                .set("text-decoration", "none")
                .set("text-overflow", "ellipsis");

        // Marque
        String marque = art.getMarque() != null ? art.getMarque() : "—";
        Span marqueSpan = new Span(marque);
        marqueSpan.getStyle()
                .set("color", "#0000CC")
                .set("width", "165px")
                .set("font-size", "0.85rem");

        // Prix
        String prix = art.getPrix() != null ? art.getPrix() + " CHF" : "—";
        Span prixSpan = new Span(prix);
        prixSpan.getStyle()
                .set("width", "130px")
                .set("font-size", "0.85rem");

        // ✅ Badge statut corrigé
        Span statutSpan = new Span();
        statutSpan.getStyle().set("width", "130px");

        if (Integer.valueOf(1).equals(art.getPub())) {
            Span badge = new Span("⭐ Article promu");
            badge.getStyle()
                    .set("background-color", "#6100C1")
                    .set("color", "white")
                    .set("padding", "3px 8px")
                    .set("border-radius", "12px")
                    .set("font-size", "0.75rem")
                    .set("font-weight", "bold")
                    .set("white-space", "nowrap");
            statutSpan.add(badge);
        }

        // ✅ Plus de 'a' parasite
        row.add(imgBox, nomLink, marqueSpan, prixSpan, statutSpan);

        Long id = art.getIdArticle();
        row.getElement().addEventListener("click", e ->
                row.getUI().ifPresent(ui -> ui.navigate("detail/" + id))
        );

        return row;
    }
    private Span cell(String text, String width) {
        Span s = new Span(text);
        s.getStyle().set("width", width).set("font-size", "0.9rem");
        return s;
    }
}