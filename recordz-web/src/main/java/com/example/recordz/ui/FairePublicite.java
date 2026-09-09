package com.example.recordz.ui;
import com.example.recordz.model.domain.Article;
import com.example.recordz.security.AuthenticatedUser;
import com.example.recordz.service.ArticleService;
import com.example.recordz.service.EnchereService;
import com.example.recordz.service.ReferenceService;
import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;

@Route(value = "publicite", layout = MainLayout.class)
@PageTitle("Recordz — Faire de la publicité")
@PermitAll

public class FairePublicite extends VerticalLayout implements HasUrlParameter<Integer>  {

    private final ArticleService    articleService;
    private final EnchereService    enchereService;
    private final AuthenticatedUser authenticatedUser;
    private final ReferenceService  referenceService;
    private final Grid<Article> grid = new Grid<>(Article.class, false);
    private static final int PAGE_SIZE = 10;

    public FairePublicite(ArticleService articleService,
                        EnchereService enchereService,
                        AuthenticatedUser authenticatedUser,ReferenceService  referenceService) {
        add(new H2("🔨 Faire de la publicite"));
        this.articleService    = articleService;
        this.enchereService    = enchereService;
        this.authenticatedUser = authenticatedUser;
        this.referenceService  = referenceService;

        setPadding(true);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer page) {
        int currentPage = (page != null && page > 0) ? page : 1;

        // Récupérer l'email de l'utilisateur connecté
        OidcUser user = (OidcUser) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        String email = user.getAttribute("email");

        int total = articleService.countActiveArticlesByVendeur(email);
        int totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        removeAll();
        setPadding(true);
        setSpacing(false);

        add(buildSecondaryNav());
        add(buildPagination(currentPage, totalPages));

        List<Article> articles = articleService.findActiveArticlesByVendeurSansPub(
                email, currentPage, PAGE_SIZE
        );
        grid.setItems(articles);
        add(buildGrid());
        add(buildPagination(currentPage, totalPages));
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
                .set("width", "100%")
                .set("border-bottom", "2px solid #6600CC");  // ← ligne séparatrice
        header.add(
                cell("Image",                 "80px"),   // ← doit matcher imgBox (60px + marge)
                cell("Nom de l'article",      "210px"),
                cell("Marque",                "165px"),
                cell("Prix CHF", "130px")
        );
        return header;
    }
    private HorizontalLayout buildArticleRow(Article art) {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(Alignment.CENTER);
        row.getStyle()
                .set("padding", "6px 0")
                .set("border-bottom", "1px solid #EEE")
                .set("cursor", "pointer");

        // ✅ Image depuis DB
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
            // Placeholder gris si pas d'image
            imgBox.getStyle()
                    .set("background-color", "#DDD")
                    .set("border", "1px solid #CCC");
        }

        // Nom – lien vers détail
        String nom = art.getNom() != null ? art.getNom() : "(sans nom)";
        Anchor nomLink = new Anchor("/detail/" + art.getIdArticle(), nom);
        nomLink.getStyle()
                .set("color", "#0000CC")
                .set("width", "210px")
                .set("font-size", "0.9rem")
                .set("overflow", "hidden")
                .set("white-space", "nowrap")
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

        row.add(imgBox, nomLink, marqueSpan, prixSpan);

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

    private HorizontalLayout buildPagination(int currentPage, int totalPages) {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(FlexComponent.Alignment.CENTER);
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
    private HorizontalLayout buildSecondaryNav() {
        HorizontalLayout nav = new HorizontalLayout();
        nav.getStyle()
                .set("color", "#0000CC")
                .set("font-size", "0.85rem");
        nav.add(
                navLink("Informations",       "/mon-compte/informations"),
                navLink("Mes encheres",       "/encheres"),
                navLink("Ajouter un article", "/ajouter-article"),
                navLink("Faire de la publicité", "#"),
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

    // ── Grille ────────────────────────────────────────────────

    private Grid<Article> buildGrid() {
        grid.getStyle()
                .set("border-radius", "20px")
                .set("border", "1px solid #6100C1")
                .set("overflow", "hidden")
                .set("width", "800px")
                .set("padding-left", "8px");
        ;
        grid.addColumn(Article::getNom)
                .setHeader("Article").setSortable(true).setFlexGrow(2);

        grid.addColumn(Article::getMarque)
                .setHeader("Marque").setSortable(true);

        grid.addColumn(a -> a.getPrix() != null ? a.getPrix() + " CHF" : "—")
                .setHeader("Prix de départ");

// ✅ CORRECTION
        grid.addColumn(a -> {
                    Double derniereOffre = referenceService.findDerniereOffre(a.getIdArticle().intValue());
                    return derniereOffre != null ? derniereOffre + " CHF" : "—";
                })
                .setHeader("Meilleure offre");
        grid.addColumn(Article::getEnchereDateFin)
                .setHeader("Fin le");
        grid.addComponentColumn(article -> {
            Button pub = styledButton("Faire de la pub");
            pub.addClickListener(e -> {
                // accès à l'article de la ligne
                faireUnePub(article);
            });
            return pub;
        }).setHeader("Publicité");return grid;
    }

    private void faireUnePub(Article article) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Publicité — " + article.getNom());

        Text info = new Text("Mettre en avant cet article pour attirer plus d'enchérisseurs.");

        Button confirmer = styledButton("Activer la pub");
        Button annuler = new Button("Annuler", e -> dialog.close());

        confirmer.addClickListener(e -> {
            // ta logique métier ici
            // ex: pubService.activerPub(article.getIdArticle());
            this.articleService.updateFaitDeLapublicite(article.getIdArticle().intValue());
            Notification.show("Publicité activée pour : " + article.getNom());
            dialog.close();
        });

        dialog.add(info);
        dialog.getFooter().add(annuler, confirmer);
        dialog.open();
        buildGrid();
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



    // ── Helpers ───────────────────────────────────────────────

    private void refreshGrid(int page) {
        List<Article> articles = articleService.findAll(page, PAGE_SIZE);
        grid.setItems(articles);
    }

    private void showSuccess(String message) {
        Notification n = Notification.show(message, 3000, Notification.Position.TOP_END);
        n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void showError(String message) {
        Notification n = Notification.show(message, 4000, Notification.Position.TOP_END);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

}



