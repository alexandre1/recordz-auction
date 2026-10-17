package com.example.recordz.ui;

import com.example.recordz.model.domain.Article;
import com.example.recordz.security.AuthenticatedUser;
import com.example.recordz.service.ArticleService;
import com.example.recordz.service.EnchereService;
import com.example.recordz.service.ReferenceService;
import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;

@Route(value = EncheresView.ROUTE, layout = MainLayout.class)
@PermitAll
public class EncheresView extends VerticalLayout implements HasUrlParameter<Integer>, HasDynamicTitle {

    static final String ROUTE = "encheres";
    private static final int PAGE_SIZE = 10;

    private final ArticleService    articleService;
    private final EnchereService    enchereService;
    private final AuthenticatedUser authenticatedUser;
    private final ReferenceService  referenceService;
    private final Grid<Article> grid = new Grid<>(Article.class, false);

    public EncheresView(ArticleService articleService,
                        EnchereService enchereService,
                        AuthenticatedUser authenticatedUser,
                        ReferenceService referenceService) {
        this.articleService    = articleService;
        this.enchereService    = enchereService;
        this.authenticatedUser = authenticatedUser;
        this.referenceService  = referenceService;

        setPadding(true);
        configureGrid();   // colonnes créées une seule fois (la vue est réutilisée quand seule la page change)
    }

    @Override
    public String getPageTitle() {
        return getTranslation("enc.page.title");
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

        // Le titre est ajouté ici : un removeAll() le supprimerait s'il était posé dans le constructeur
        add(new H2("🔨 " + getTranslation("enc.title")));
        add(buildSecondaryNav());
        add(buildPagination(currentPage, totalPages));

        List<Article> articles = articleService.findActiveArticlesByVendeur(
                email, currentPage, PAGE_SIZE
        );
        grid.setItems(articles);
        add(grid);
        add(buildPagination(currentPage, totalPages));
    }

    // ── Pagination ────────────────────────────────────────────

    private HorizontalLayout buildPagination(int currentPage, int totalPages) {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(FlexComponent.Alignment.CENTER);
        row.getStyle().set("padding", "12px 0").set("gap", "6px");

        row.add(paginationBtn("«", pageHref(1),                  currentPage == 1));
        row.add(paginationBtn("‹", pageHref(currentPage - 1),    currentPage == 1));

        int start = Math.max(1, currentPage - 2);
        int end   = Math.min(totalPages, currentPage + 2);

        if (start > 1) {
            row.add(paginationBtn("1", pageHref(1), false));
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
                        pageBtn.getUI().ifPresent(ui -> ui.navigate(ROUTE + "/" + p))
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
            row.add(paginationBtn(String.valueOf(totalPages), pageHref(totalPages), false));
        }

        row.add(paginationBtn("›", pageHref(currentPage + 1), currentPage == totalPages));
        row.add(paginationBtn("»", pageHref(totalPages),      currentPage == totalPages));

        Span info = new Span(getTranslation("main.page.info",
                String.valueOf(currentPage), String.valueOf(totalPages)));
        info.getStyle()
                .set("font-size", "0.78rem").set("color", "#6100C1")
                .set("margin-left", "8px").set("align-self", "center");
        row.add(info);

        return row;
    }

    /** Les liens de pagination restent sur la page « enchères » (et non sur l'accueil). */
    private static String pageHref(int page) {
        return "/" + ROUTE + "/" + Math.max(1, page);
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

    // ── Navigation secondaire ─────────────────────────────────

    private HorizontalLayout buildSecondaryNav() {
        HorizontalLayout nav = new HorizontalLayout();
        nav.getStyle()
                .set("color", "#0000CC")
                .set("font-size", "0.85rem");
        nav.add(
                navLink(getTranslation("account.nav.info"),     "/mon-compte/informations"),
                navLink(getTranslation("account.nav.bids"),     "/encheres"),
                navLink(getTranslation("account.nav.add"),      "/ajouter-article"),
                navLink(getTranslation("account.nav.ads"),      "/publicite"),
                navLink(getTranslation("account.nav.calendar"), "/calendrier")
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

    private void configureGrid() {
        grid.getStyle()
                .set("border-radius", "20px")
                .set("border", "1px solid #6100C1")
                .set("overflow", "hidden")
                .set("padding-left", "8px");
        grid.setWidthFull();
        grid.setMaxWidth("800px");

        grid.addColumn(Article::getNom)
                .setHeader(getTranslation("account.col.article")).setSortable(true).setFlexGrow(2);

        grid.addColumn(Article::getMarque)
                .setHeader(getTranslation("account.col.brand")).setSortable(true);

        grid.addColumn(a -> a.getPrix() != null ? a.getPrix() + " CHF" : "—")
                .setHeader(getTranslation("enc.col.startprice"));

        grid.addColumn(a -> {
                    Double derniereOffre = referenceService.findDerniereOffre(a.getIdArticle().intValue());
                    return derniereOffre != null ? derniereOffre + " CHF" : "—";
                })
                .setHeader(getTranslation("enc.col.bestoffer"));

        grid.addColumn(Article::getEnchereDateFin)
                .setHeader(getTranslation("enc.col.enddate"));
    }
}
