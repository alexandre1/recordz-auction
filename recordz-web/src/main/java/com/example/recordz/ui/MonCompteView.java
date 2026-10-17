package com.example.recordz.ui;

import com.example.recordz.model.domain.Article;
import com.example.recordz.model.domain.Personne;
import com.example.recordz.service.ArticleService;
import com.example.recordz.service.PersonneService;
import com.example.recordz.service.ReferenceService;
import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Route(value = "mon-compte", layout = MainLayout.class)
@PermitAll
public class MonCompteView extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle {

    private static final Number[] STATS_PARIS_HILTON = {0, 4, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1000};
    /** Libellés des 12 mois, traduits selon la locale de l'UI. */
    private String[] months() {
        String[] m = new String[12];
        for (int i = 0; i < 12; i++) {
            m[i] = getTranslation("account.month." + (i + 1));
        }
        return m;
    }

    @Override
    public String getPageTitle() {
        return getTranslation("account.page.title");
    }

    private final ReferenceService referenceService;
    private final ArticleService articleService;
    private final VerticalLayout contentArea = new VerticalLayout();
    private final PersonneService personneService;

    private final Grid<Article> grid = new Grid<>(Article.class, false);
    private Grid<Article> localGridPayment = null;


    private static final int PAGE_SIZE = 80;
    private String email;
    private int total;
    private int totalPages;
    private int currentPage;
    private String activeView = "";

    private final JavaMailSender mailSender;

    public MonCompteView(ArticleService articleService, ReferenceService referenceService,
                         PersonneService personneService,JavaMailSender mailSender) {
        this.articleService = articleService;
        this.referenceService = referenceService;
        this.personneService = personneService;
        this.mailSender = mailSender;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        getStyle()
                .set("border-radius", "20px")
                .set("overflow", "hidden")
                .set("width", "100%")
                .set("max-width", "640px")
                .set("box-sizing", "border-box")
                .set("margin-bottom", "24px");

        OidcUser user = (OidcUser) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        email = user.getAttribute("email");

        removeAll();
        setPadding(true);
        setSpacing(false);

        add(buildSecondaryNav());
        add(buildMenuPrincipal());
        add(new Hr());

        contentArea.setPadding(false);
        contentArea.setSpacing(false);
        add(contentArea);
    }

    // -------------------------------------------------------------------------
    // Menu principal
    // -------------------------------------------------------------------------
    private VerticalLayout buildMenuPrincipal() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);

        String[][] items = {
                {getTranslation("account.menu.myarticlepayes"),                "myarticlepayes"},
                {getTranslation("account.menu.mywaitdelivering"), "mywaitdelivering"},
                {getTranslation("account.menu.mywaitdeliveringbypost"), "mywaitdeliveringbypost"},
                {getTranslation("account.menu.mywaitpayement"),            "mywaitpayement"},
                {getTranslation("account.menu.myobjectstodeliver"),      "myobjectstodeliver"},
                {getTranslation("account.menu.myobjectstodeliverByPost"),                 "myobjectstodeliverByPost"},
                {getTranslation("account.menu.mywaitpickup"),         "mywaitpickup"},
                {getTranslation("account.menu.myarticletopickup"),       "myarticletopickup"},
                {getTranslation("account.menu.myunsold"),                        "myunsold"},
                {getTranslation("account.menu.myselling"),                 "myselling"},
                {getTranslation("account.menu.mybought"),                "mybought"},
                {getTranslation("account.menu.mysold"),                 "mysold"},
                {getTranslation("account.menu.statdealarticlebrand"),   "statdealarticlebrand"}


        };

        for (String[] item : items) {
            String libelle = item[0];
            String option  = item[1];

            Anchor link = new Anchor("#", libelle);
            link.getStyle()
                    .set("color", "#0000CC")
                    .set("font-size", "0.85rem")
                    .set("display", "block")
                    .set("margin-bottom", "4px")
                    .set("margin-top", "10px")
                    .set("text-decoration", "none");

            link.getElement().addEventListener("click", e -> showContent(option))
                    .addEventData("event.preventDefault()");

            layout.add(link);
        }
        return layout;
    }

    // -------------------------------------------------------------------------
    // Dispatch
    // -------------------------------------------------------------------------
    private void showContent(String option) {
        currentPage = 0;
        activeView  = option;
        getStyle()
                .set("width", "100%")
                .set("min-width", "0")
                .set("max-width", "100%")   // annule le max-width: 640px posé dans beforeEnter
                .set("box-sizing", "border-box");
        contentArea.removeAll();
        switch (option) {
            case "myarticlepayes"       -> contentArea.add(buildStatsAPaye());
            case "mywaitdelivering"     -> contentArea.add(buildAttenteLivraisonADomicilePourMoi());
            case "mywaitdeliveringbypost"     -> contentArea.add(buildAttenteLivraisonByPoste());
            case "statdealarticlebrand" -> contentArea.add(buildStatsBrand());
            case "mywaitpayement"             -> contentArea.add(buildStatsMesAttentesDePement());
            case "myselling"                  -> contentArea.add(buildStatsMesObjetsEnVente());
            case "myobjectstodeliver"         -> contentArea.add(buildStatsMesArtilesAlivrerADomicile());
            case "myobjectstodeliverByPost"   -> contentArea.add(buildStatsMesArtilesAlivrerByPost());
            case "mybought"             -> contentArea.add(buildStatsMesAchats());
            case "myunsold"             -> contentArea.add(buildStatsInvendu());
            case "myarticletopickup"    -> contentArea.add(buildAllerChercher());
            case "mywaitpickup"          -> contentArea.add(buildAttenteSoulevement());
            case "mysold"               -> contentArea.add(buildStatsMesVendus());
            default                     -> contentArea.add(buildSousVueGenerique(option));
        }
    }

    private VerticalLayout buildSousVueGenerique(String option) {
        VerticalLayout layout = new VerticalLayout();
        Span msg = new Span(getTranslation("account.coming.soon", option));
        msg.getStyle().set("font-style", "italic").set("color", "#888");
        layout.add(msg);
        return layout;
    }

    // -------------------------------------------------------------------------
    // Sous-vue : Mes articles payés (acheteur)
    // -------------------------------------------------------------------------
    /*
    private VerticalLayout buildStatsAPaye() {
        getStyle().set("width", "100%");
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(new Span("← Mes articles payés →"));

        localGridPayment = new Grid<>(Article.class, false);
        styleGrid(localGridPayment);
        // ✅ Pas nécessaire ici car c'est un nouveau Grid, mais rafraîchir après
        // le dialog doit recréer la vue entière
        addArticlesAPayer(localGridPayment);
        total      = articleService.countAPayerByUsername(email);
        totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        VerticalLayout gridContainer = new VerticalLayout();
        gridContainer.setPadding(false);
        gridContainer.setSpacing(false);
        refreshGridAPaye(gridContainer, layout);
        layout.add(gridContainer);
        return layout;
    }
*/

    private VerticalLayout buildStatsAPaye() {
        getStyle().set("width", "100%");
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(new Span("← " + getTranslation("account.menu.myarticlepayes") + " →"));

        localGridPayment = new Grid<>(Article.class, false);
        styleGrid(localGridPayment);
        addArticlesAPayer(localGridPayment);

        total      = articleService.countAPayerByUsername(email);
        totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        VerticalLayout gridContainer = new VerticalLayout();
        gridContainer.setPadding(false);
        gridContainer.setSpacing(false);
        refreshGridAPaye(gridContainer, layout);
        layout.add(gridContainer);
        return layout;
    }

    // -------------------------------------------------------------------------
    // Sous-vue : Mes attentes de livraison (acheteur)
    // -------------------------------------------------------------------------
    private VerticalLayout buildAttenteLivraisonADomicilePourMoi() {
        getStyle().set("width", "100%");
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(new Span("← " + getTranslation("account.menu.mywaitdelivering") + " →"));

        localGridPayment = new Grid<>(Article.class, false);
        styleGrid(localGridPayment);
        addArticleMesArtclesAttenteLivraisonADomicile(localGridPayment);

        total      = articleService.countALivrerByUsername(email);
        totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        VerticalLayout gridContainer = new VerticalLayout();
        gridContainer.setPadding(false);
        gridContainer.setSpacing(false);
        refreshGridMesArticlesALivrerADomicilePourMoi(gridContainer, layout);
        layout.add(gridContainer);
        return layout;
    }

    private VerticalLayout buildAttenteLivraisonByPoste() {
        getStyle().set("width", "100%");
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(new Span("← " + getTranslation("account.menu.mywaitdeliveringbypost") + " →"));

        localGridPayment = new Grid<>(Article.class, false);
        styleGrid(localGridPayment);
        addArticleMesArtclesAttenteByPoste(localGridPayment);

        total      = articleService.countALivrerByUsername(email);
        totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        VerticalLayout gridContainer = new VerticalLayout();
        gridContainer.setPadding(false);
        gridContainer.setSpacing(false);
        refreshGridMesArticlesAttenteLivraisonByPoste(gridContainer, layout);
        layout.add(gridContainer);
        return layout;
    }

    private VerticalLayout buildAllerChercher() {
        getStyle().set("width", "100%");
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(new Span("← " + getTranslation("account.menu.myarticletopickup") + " →"));

        localGridPayment = new Grid<>(Article.class, false);
        styleGrid(localGridPayment);
        addArticleMesArtclesAllerChercher(localGridPayment);

        total      = articleService.countAllerChercherByUsername(email);
        totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        VerticalLayout gridContainer = new VerticalLayout();
        gridContainer.setPadding(false);
        gridContainer.setSpacing(false);
        refreshGridAllerChercher(gridContainer, layout);
        layout.add(gridContainer);
        return layout;
    }

    private VerticalLayout buildAttenteSoulevement() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(new Span("← " + getTranslation("account.menu.mywaitpickup") + " →"));

        localGridPayment = new Grid<>(Article.class, false);

        styleGrid(localGridPayment);
        addArticleAttenteSoulevement(localGridPayment);

        //total      = articleService.countAllerChercherByUsername(email);
        total      = articleService.countAttenteSoulevementByUsername(email);
        totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        VerticalLayout gridContainer = new VerticalLayout();
        gridContainer.setPadding(false);
        gridContainer.setSpacing(false);
        //refreshGridAllerChercher(gridContainer, layout);
        refreshGridAttenteSoulevement(gridContainer, layout);
        layout.add(gridContainer);
        return layout;
    }

    // -------------------------------------------------------------------------
    // Sous-vue : Mes attentes de payement (vendeur)
    // -------------------------------------------------------------------------
    private VerticalLayout buildStatsMesAttentesDePement() {
        getStyle().set("width", "100%");
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(new Span("← " + getTranslation("account.menu.mywaitpayement") + " →"));

        localGridPayment = new Grid<>(Article.class, false);
        styleGrid(localGridPayment);
        addArticleColumnsPayment(localGridPayment);

        total      = articleService.countAttentePaymentByUsername(email);
        totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        VerticalLayout gridContainer = new VerticalLayout();
        gridContainer.setPadding(false);
        gridContainer.setSpacing(false);
        refreshGrAttenteDePayemnt(gridContainer, layout);
        layout.add(gridContainer);
        return layout;
    }

    // -------------------------------------------------------------------------
    // Sous-vue : Mes articles à livrer (vendeur)
    // -------------------------------------------------------------------------
    private VerticalLayout buildStatsMesArtilesAlivrerADomicile() {
        getStyle().set("width", "100%");
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(new Span("← " + getTranslation("account.menu.myobjectstodeliver") + " →"));

        localGridPayment = new Grid<>(Article.class, false);
        styleGrid(localGridPayment);
        addArticleMesArtclesALivrever(localGridPayment);

        total      = articleService.countALivrerByUsername(email);
        totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        VerticalLayout gridContainer = new VerticalLayout();
        gridContainer.setPadding(false);
        gridContainer.setSpacing(false);
        refreshGridMesArticlesALivrerADomicile(gridContainer, layout);
        layout.add(gridContainer);
        return layout;
    }


    private VerticalLayout buildStatsMesArtilesAlivrerByPost() {
        getStyle().set("width", "100%");
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(new Span("← " + getTranslation("account.menu.myobjectstodeliverByPost") + " →"));

        localGridPayment = new Grid<>(Article.class, false);
        styleGrid(localGridPayment);
        addArticleMesArtclesALivreverByPost(localGridPayment);

        total      = articleService.countALivrerByUsername(email);
        totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        VerticalLayout gridContainer = new VerticalLayout();
        gridContainer.setPadding(false);
        gridContainer.setSpacing(false);
        refreshGridMesArticlesALivrerParPoste(gridContainer, layout);
        layout.add(gridContainer);
        return layout;
    }

    // -------------------------------------------------------------------------
    // Sous-vue : Mes invendus
    // -------------------------------------------------------------------------
    private VerticalLayout buildStatsInvendu() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(new Span("← " + getTranslation("account.menu.myunsold") + " →"));

        grid.removeAllColumns();
        addArticleColumns();

        total      = articleService.countActiveArticlesByVendeur(email);
        totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        VerticalLayout gridContainer = new VerticalLayout();
        gridContainer.setPadding(false);
        gridContainer.setSpacing(false);
        refreshGrid(gridContainer, layout);
        layout.add(gridContainer);
        return layout;
    }

    // -------------------------------------------------------------------------
    // Sous-vue : Mes objets en vente
    // -------------------------------------------------------------------------
    private VerticalLayout buildStatsMesObjetsEnVente() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(new Span("← " + getTranslation("account.menu.myselling") + " →"));

        grid.removeAllColumns();
        addArticleColumns();

        total      = articleService.countActiveArticlesByVendeur(email);
        totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        VerticalLayout gridContainer = new VerticalLayout();
        gridContainer.setPadding(false);
        gridContainer.setSpacing(false);
        refreshGridMesObjetsEnVente(gridContainer, layout);
        layout.add(gridContainer);
        return layout;
    }

    // -------------------------------------------------------------------------
    // Sous-vue : Mes achats
    // -------------------------------------------------------------------------
    private VerticalLayout buildStatsMesAchats() {
        getStyle().set("width", "100%");
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(new Span("← " + getTranslation("account.menu.mybought") + " →"));

        localGridPayment = new Grid<>(Article.class, false);
        styleGrid(localGridPayment);
        grid.removeAllColumns();
        addArticleNoters(localGridPayment);

        total      = articleService.countMesAchatsByAcheteur(email);
        totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        VerticalLayout gridContainer = new VerticalLayout();
        gridContainer.setPadding(false);
        gridContainer.setSpacing(false);
        refreshGridMesAchats(gridContainer, layout);
        layout.add(gridContainer);
        return layout;
    }


    private VerticalLayout buildStatsMesVendus () {
        getStyle().set("width", "100%");
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(false);
        layout.setPadding(false);
        layout.add(new Span("← " + getTranslation("account.menu.mysold") + " →"));

        localGridPayment = new Grid<>(Article.class, false);
        styleGrid(localGridPayment);
        grid.removeAllColumns();
        addArticleVendus(localGridPayment);

        total      = this.articleService.countMesArticlesVendus(email);
        totalPages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        VerticalLayout gridContainer = new VerticalLayout();
        gridContainer.setPadding(false);
        gridContainer.setSpacing(false);
        refreshGridMesVendus(gridContainer, layout);
        layout.add(gridContainer);
        return layout;
    }

    // -------------------------------------------------------------------------
    // Helper : style commun des grids
    // -------------------------------------------------------------------------

    private void styleGrid(Grid<Article> g) {
        g.getStyle()
                .set("border-radius", "20px")
                .set("border", "1px solid #6100C1")
                .set("padding-left", "8px");   // retire "overflow: visible"

        g.setWidth("100%");
        // Plus de min-width : le grid défile horizontalement dans son propre cadre
        // si les colonnes dépassent la largeur de l'écran.
        g.setAllRowsVisible(true);             // OK : pagination à PAGE_SIZE lignes
        // Sur mobile : on masque les colonnes secondaires
        ResponsiveHelper.hideColumnsOnMobile(g, getTranslation("account.col.id"), getTranslation("account.col.brand"));
    }
    // -------------------------------------------------------------------------
    // Colonnes : grid standard
    // -------------------------------------------------------------------------
    private void addArticleColumns() {
        grid.addComponentColumn(this::buildImageBox).setHeader(getTranslation("account.col.image")).setWidth("80px").setFlexGrow(0);
        grid.addComponentColumn(this::buildNomLink).setHeader(getTranslation("account.col.article")).setFlexGrow(2);
        grid.addColumn(art -> art.getMarque() != null ? art.getMarque() : "—").setHeader(getTranslation("account.col.brand")).setWidth("150px").setFlexGrow(0);
        grid.addColumn(art -> art.getPrix() != null ? art.getPrix() + " CHF" : "—").setHeader(getTranslation("account.col.price")).setWidth("120px").setFlexGrow(0);
        ResponsiveHelper.hideColumnsOnMobile(grid, getTranslation("account.col.id"), getTranslation("account.col.brand"));
    }

    // -------------------------------------------------------------------------
    // Colonnes : Mes articles payés (acheteur)
    // -------------------------------------------------------------------------
    private void addArticlesAPayer(Grid<Article> g) {
        g.addColumn(art -> art.getIdApaye() != null ? art.getIdApaye() : "—").setHeader(getTranslation("account.col.id")).setWidth("30px").setFlexGrow(0);
        g.addComponentColumn(this::buildImageBox).setHeader(getTranslation("account.col.image")).setWidth("80px").setFlexGrow(0);
        g.addComponentColumn(this::buildNomLink).setHeader(getTranslation("account.col.article")).setFlexGrow(2);
        g.addColumn(art -> art.getMarque() != null ? art.getMarque() : "—").setHeader(getTranslation("account.col.brand")).setWidth("150px").setFlexGrow(0);
        g.addColumn(art -> art.getPrix() != null ? art.getPrix() + " CHF" : "—").setHeader(getTranslation("account.col.price")).setWidth("120px").setFlexGrow(0);

        g.addComponentColumn(art -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setWidthFull();
            actions.setJustifyContentMode(JustifyContentMode.BETWEEN);
            actions.setAlignItems(Alignment.CENTER);

            if (art.getRefVendeur() != null) {
                Personne vendeur = this.personneService.findById(art.getRefVendeur().longValue());
                if (vendeur != null) {
                    Anchor profilLink = new Anchor("/profil/" + vendeur.getNomUtilisateur(), getTranslation("account.link.seller.profile"));
                    styleAnchorBtn(profilLink);
                    actions.add(profilLink);
                } else {
                    actions.add(new Span("—"));
                }
            } else {
                actions.add(new Span("—"));
            }

            Button btn = styledButton(getTranslation("account.btn.confirm.payment"));
            btn.addClickListener(e -> showConfirmerPayement(art));
            actions.add(btn);
            return actions;
        }).setHeader(getTranslation("account.col.actions")).setWidth("300px").setFlexGrow(1);
    }

    // -------------------------------------------------------------------------
    // Colonnes : Mes attentes de payement (vendeur)
    // -------------------------------------------------------------------------
    private void addArticleColumnsPayment(Grid<Article> g) {
        g.addColumn(art -> art.getIdApaye() != null ? art.getIdApaye() : "—").setHeader(getTranslation("account.col.id")).setWidth("30px").setFlexGrow(0);
        g.addComponentColumn(this::buildImageBox).setHeader(getTranslation("account.col.image")).setWidth("80px").setFlexGrow(0);
        g.addComponentColumn(this::buildNomLink).setHeader(getTranslation("account.col.article")).setFlexGrow(2);
        g.addColumn(art -> art.getMarque() != null ? art.getMarque() : "—").setHeader(getTranslation("account.col.brand")).setWidth("150px").setFlexGrow(0);
        g.addColumn(art -> art.getPrix() != null ? art.getPrix() + " CHF" : "—").setHeader(getTranslation("account.col.price")).setWidth("120px").setFlexGrow(0);

        g.addComponentColumn(art -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setWidthFull();
            actions.setJustifyContentMode(JustifyContentMode.BETWEEN);
            actions.setAlignItems(Alignment.CENTER);

            if (art.getRefAcheteur() != null) {
                Personne personne = this.personneService.findById(art.getRefAcheteur());
                if (personne != null) {
                    Anchor profilLink = new Anchor("/profil/" + personne.getNomUtilisateur(), getTranslation("account.link.buyer.profile"));
                    styleAnchorBtn(profilLink);
                    actions.add(profilLink);
                } else {
                    actions.add(new Span("—"));
                }
            } else {
                actions.add(new Span("—"));
            }

            Button btn = styledButton(getTranslation("account.btn.confirm.payment"));
            btn.addClickListener(e -> showFaireOffreDialog(art));
            actions.add(btn);
            return actions;
        }).setHeader(getTranslation("account.col.actions")).setWidth("300px").setFlexGrow(1);
    }

    // -------------------------------------------------------------------------
    // Colonnes : Mes articles à livrer (vendeur)
    // -------------------------------------------------------------------------
    private void addArticleMesArtclesALivrever(Grid<Article> g) {
        g.addColumn(art -> art.getIdAlivre() != null ? art.getIdAlivre() : "—").setHeader(getTranslation("account.col.id")).setWidth("30px").setFlexGrow(0);
        g.addComponentColumn(this::buildImageBox).setHeader(getTranslation("account.col.image")).setWidth("80px").setFlexGrow(0);
        g.addComponentColumn(this::buildNomLink).setHeader(getTranslation("account.col.article")).setFlexGrow(2);
        g.addColumn(art -> art.getMarque() != null ? art.getMarque() : "—").setHeader(getTranslation("account.col.brand")).setWidth("150px").setFlexGrow(0);
        g.addColumn(art -> art.getPrix() != null ? art.getPrix() + " CHF" : "—").setHeader(getTranslation("account.col.price")).setWidth("120px").setFlexGrow(0);

        g.addComponentColumn(art -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setWidthFull();
            actions.setJustifyContentMode(JustifyContentMode.BETWEEN);
            actions.setAlignItems(Alignment.CENTER);

            if (art.getRefAcheteur() != null) {
                Button profilBtn = styledButton(getTranslation("account.btn.buyer.profile"));
                profilBtn.addClickListener(e -> showAcheteurDetail(art));
                actions.add(profilBtn);
            } else {
                actions.add(new Span("—"));
            }

            Button offreBtn = styledButton(getTranslation("account.btn.confirm.delivery"));
            offreBtn.addClickListener(e -> showConfirmerLivraison(art));
            actions.add(offreBtn);
            return actions;
        }).setHeader(getTranslation("account.col.actions")).setWidth("300px").setFlexGrow(1);
    }


    private void addArticleMesArtclesALivreverByPost(Grid<Article> g) {
        g.addColumn(art -> art.getIdAlivre() != null ? art.getIdAlivre() : "—").setHeader(getTranslation("account.col.id")).setWidth("30px").setFlexGrow(0);
        g.addComponentColumn(this::buildImageBox).setHeader(getTranslation("account.col.image")).setWidth("80px").setFlexGrow(0);
        g.addComponentColumn(this::buildNomLink).setHeader(getTranslation("account.col.article")).setFlexGrow(2);
        g.addColumn(art -> art.getMarque() != null ? art.getMarque() : "—").setHeader(getTranslation("account.col.brand")).setWidth("150px").setFlexGrow(0);
        g.addColumn(art -> art.getPrix() != null ? art.getPrix() + " CHF" : "—").setHeader(getTranslation("account.col.price")).setWidth("120px").setFlexGrow(0);

        g.addComponentColumn(art -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setWidthFull();
            actions.setJustifyContentMode(JustifyContentMode.BETWEEN);
            actions.setAlignItems(Alignment.CENTER);

            if (art.getRefAcheteur() != null) {
                Button profilBtn = styledButton(getTranslation("account.btn.buyer.profile"));
                profilBtn.addClickListener(e -> showAcheteurDetail(art));
                actions.add(profilBtn);
            } else {
                actions.add(new Span("—"));
            }

            Button offreBtn = styledButton(getTranslation("account.btn.confirm.delivery"));
            offreBtn.addClickListener(e -> showConfirmerLivraisonByPost(art));
            actions.add(offreBtn);
            return actions;
        }).setHeader(getTranslation("account.col.actions")).setWidth("300px").setFlexGrow(1);
    }

    // -------------------------------------------------------------------------
    // Colonnes : Mes attentes de livraison (acheteur)
    // -------------------------------------------------------------------------
    private void addArticleMesArtclesAttenteLivraisonADomicile(Grid<Article> g) {
        g.addColumn(art -> art.getIdAlivre() != null ? art.getIdAlivre() : "—").setHeader(getTranslation("account.col.id")).setWidth("30px").setFlexGrow(0);
        g.addComponentColumn(this::buildImageBox).setHeader(getTranslation("account.col.image")).setWidth("80px").setFlexGrow(0);
        g.addComponentColumn(this::buildNomLink).setHeader(getTranslation("account.col.article")).setFlexGrow(2);
        g.addColumn(art -> art.getMarque() != null ? art.getMarque() : "—").setHeader(getTranslation("account.col.brand")).setWidth("150px").setFlexGrow(0);
        g.addColumn(art -> art.getPrix() != null ? art.getPrix() + " CHF" : "—").setHeader(getTranslation("account.col.price")).setWidth("120px").setFlexGrow(0);

        g.addComponentColumn(art -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setWidthFull();
            actions.setJustifyContentMode(JustifyContentMode.BETWEEN);
            actions.setAlignItems(Alignment.CENTER);

            // a_livre.ref_vendeur est déjà l'ID personne du vendeur
            Integer vendeurId = art.getRefVendeur() != null
                    ? art.getRefVendeur().intValue()
                    : null;

            if (vendeurId != null) {
                Personne vendeur = this.personneService.findById(vendeurId.longValue());
                if (vendeur != null) {
                    Anchor profilLink = new Anchor(
                            "/profil/" + vendeur.getNomUtilisateur(),
                            getTranslation("account.link.seller.profile")
                    );
                    styleAnchorBtn(profilLink);
                    actions.add(profilLink);
                } else {
                    actions.add(new Span("—"));
                }
            } else {
                actions.add(new Span("—"));
            }

            Button btn = styledButton(getTranslation("account.btn.confirm.delivery"));
            btn.addClickListener(e -> showConfirmerReceptionADomicileDialog(art));
            actions.add(btn);
            return actions;
        }).setHeader(getTranslation("account.col.actions")).setWidth("300px").setFlexGrow(1);
    }


    private void addArticleMesArtclesAttenteByPoste(Grid<Article> g) {

        g.addColumn(art -> art.getIdAlivre() != null ? art.getIdAlivre() : "—").setHeader(getTranslation("account.col.id")).setWidth("30px").setFlexGrow(0);
        g.addComponentColumn(this::buildImageBox).setHeader(getTranslation("account.col.image")).setWidth("80px").setFlexGrow(0);
        g.addComponentColumn(this::buildNomLink).setHeader(getTranslation("account.col.article")).setFlexGrow(2);
        g.addColumn(art -> art.getMarque() != null ? art.getMarque() : "—").setHeader(getTranslation("account.col.brand")).setWidth("150px").setFlexGrow(0);
        g.addColumn(art -> art.getPrix() != null ? art.getPrix() + " CHF" : "—").setHeader(getTranslation("account.col.price")).setWidth("120px").setFlexGrow(0);

        g.addComponentColumn(art -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setWidthFull();
            actions.setJustifyContentMode(JustifyContentMode.BETWEEN);
            actions.setAlignItems(Alignment.CENTER);
            Integer personneId = art.getRefVendeur().intValue();
            //Integer personneId = this.referenceService.findPersonneId(art.getIdArticle().intValue());
            if (personneId != null) {
                Personne vendeur = this.personneService.findById(personneId.longValue());
                if (vendeur != null && art.getRefAcheteur() != null) {
                    Anchor profilLink = new Anchor("/profil/" + vendeur.getNomUtilisateur(), getTranslation("account.link.seller.profile"));
                    styleAnchorBtn(profilLink);
                    actions.add(profilLink);
                } else {
                    actions.add(new Span("—"));
                }
            } else {
                actions.add(new Span("—"));
            }

            Button btn = styledButton(getTranslation("account.btn.confirm.delivery"));
            btn.addClickListener(e -> showConfirmerReceptionADomicileDialog(art));
            actions.add(btn);
            return actions;
        }).setHeader(getTranslation("account.col.actions")).setWidth("300px").setFlexGrow(1);
    }

    private void addArticleMesArtclesAllerChercher(Grid<Article> g) {
        g.addColumn(art -> art.getIdAlivre() != null ? art.getIdAlivre() : "—").setHeader(getTranslation("account.col.id")).setWidth("30px").setFlexGrow(0);
        g.addComponentColumn(this::buildImageBox).setHeader(getTranslation("account.col.image")).setWidth("80px").setFlexGrow(0);
        g.addComponentColumn(this::buildNomLink).setHeader(getTranslation("account.col.article")).setFlexGrow(2);
        g.addColumn(art -> art.getMarque() != null ? art.getMarque() : "—").setHeader(getTranslation("account.col.brand")).setWidth("150px").setFlexGrow(0);
        g.addColumn(art -> art.getPrix() != null ? art.getPrix() + " CHF" : "—").setHeader(getTranslation("account.col.price")).setWidth("120px").setFlexGrow(0);

        g.addComponentColumn(art -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setWidthFull();
            actions.setJustifyContentMode(JustifyContentMode.BETWEEN);
            actions.setAlignItems(Alignment.CENTER);

            Integer personneId = this.referenceService.findPersonneId(art.getIdArticle().intValue());
            if (personneId != null) {
                Personne vendeur = this.personneService.findById(personneId.longValue());
                if (vendeur != null && art.getRefAcheteur() != null) {

                    Button profilBtn = styledButton(getTranslation("account.btn.seller.profile"));
                    profilBtn.addClickListener(e -> showVendeurDetails(art));

                    actions.add(profilBtn);
                } else {
                    actions.add(new Span("—"));
                }
            } else {
                actions.add(new Span("—"));
            }

            Button btn = styledButton(getTranslation("account.btn.confirm.reception"));
            btn.addClickListener(e -> showConfirmerReceptionDialog(art));
            actions.add(btn);
            return actions;
        }).setHeader(getTranslation("account.col.actions")).setWidth("300px").setFlexGrow(1);
    }


    private void addArticleAttenteSoulevement(Grid<Article> g) {
        g.addColumn(art -> art.getIdAlivre() != null
                        ? art.getIdAlivre() : "—")
                .setHeader(getTranslation("account.col.id"))
                .setWidth("30px")
                .setFlexGrow(0);

        g.addComponentColumn(this::buildImageBox)
                .setHeader(getTranslation("account.col.image"))
                .setWidth("80px")
                .setFlexGrow(0);

        g.addComponentColumn(this::buildNomLink)
                .setHeader(getTranslation("account.col.article"))
                .setFlexGrow(1);

        g.addColumn(art -> art.getMarque() != null
                        ? art.getMarque() : "—")
                .setHeader(getTranslation("account.col.brand"))
                .setWidth("120px")
                .setFlexGrow(0);

        g.addColumn(art -> art.getPrix() != null
                        ? art.getPrix() + " CHF" : "—")
                .setHeader(getTranslation("account.col.price"))
                .setWidth("100px")
                .setFlexGrow(0);

        g.addComponentColumn(art -> {
                    HorizontalLayout actions = new HorizontalLayout();
                    actions.setWidthFull();
                    actions.setSpacing(true);
                    actions.setAlignItems(Alignment.CENTER);

                    // Bouton pour consulter le profil de l'acheteur
                    Integer personneId =
                            referenceService.findPersonneId(
                                    art.getIdArticle().intValue());

                    if (personneId != null) {
                        Personne personne =
                                personneService.findById(personneId.longValue());

                        if (personne != null && art.getRefAcheteur() != null) {
                            Button profilBtn =
                                    styledButton(getTranslation("account.btn.buyer.profile"));

                            profilBtn.addClickListener(
                                    e -> showAcheteurDetails(art));

                            actions.add(profilBtn);
                        }
                    }

                    // Bouton de confirmation : toujours créé pour chaque ligne
                    Button receptionBtn =
                            styledButton(getTranslation("account.btn.confirm.pickup.reception"));

                    receptionBtn.getStyle()
                            .set("white-space", "normal")
                            .set("min-width", "220px");

                    receptionBtn.addClickListener(
                            e -> showConfirmerReceptionSoulevementDialog(art));

                    actions.add(receptionBtn);

                    return actions;
                })
                .setHeader(getTranslation("account.col.actions"))
                .setWidth("520px")
                .setFlexGrow(0);    }


    private void addArticleVendus(Grid<Article> g) {
        g.addComponentColumn(this::buildImageBox).setHeader(getTranslation("account.col.image")).setWidth("80px").setFlexGrow(0);
        g.addComponentColumn(this::buildNomLink).setHeader(getTranslation("account.col.article")).setFlexGrow(2);
        g.addColumn(art -> art.getMarque() != null ? art.getMarque() : "—").setHeader(getTranslation("account.col.brand")).setWidth("150px").setFlexGrow(0);
        g.addColumn(art -> art.getPrix() != null ? art.getPrix() + " CHF" : "—").setHeader(getTranslation("account.col.price")).setWidth("120px").setFlexGrow(0);

    }

    // -------------------------------------------------------------------------
    // Colonnes : Mes achats (noter l'article)
    // -------------------------------------------------------------------------
    private void addArticleNoters(Grid<Article> g) {
        g.addComponentColumn(this::buildImageBox).setHeader(getTranslation("account.col.image")).setWidth("80px").setFlexGrow(0);
        g.addComponentColumn(this::buildNomLink).setHeader(getTranslation("account.col.article")).setFlexGrow(2);
        g.addColumn(art -> art.getMarque() != null ? art.getMarque() : "—").setHeader(getTranslation("account.col.brand")).setWidth("150px").setFlexGrow(0);
        g.addColumn(art -> art.getPrix() != null ? art.getPrix() + " CHF" : "—").setHeader(getTranslation("account.col.price")).setWidth("120px").setFlexGrow(0);

        g.addComponentColumn(art -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setWidthFull();
            actions.setJustifyContentMode(JustifyContentMode.BETWEEN);
            actions.setAlignItems(Alignment.CENTER);
            Button btn = styledButton(getTranslation("account.btn.rate"));
            btn.addClickListener(e -> showNoterDIalogue(art));
            actions.add(btn);
            return actions;
        }).setHeader(getTranslation("account.col.actions")).setWidth("300px").setFlexGrow(1);
    }

    // -------------------------------------------------------------------------
    // Helpers colonnes
    // -------------------------------------------------------------------------
    private Div buildImageBox(Article art) {
        Div imgBox = new Div();
        imgBox.getStyle().set("width", "60px").set("height", "60px").set("flex-shrink", "0");
        String pochette = art.getPochette();
        if (pochette != null && !pochette.isBlank()) {
            Image img = new Image("/images/articles/" + pochette, art.getNom());
            img.getStyle().set("width", "60px").set("height", "60px").set("object-fit", "cover");
            imgBox.add(img);
        } else {
            imgBox.getStyle().set("background-color", "#DDD").set("border", "1px solid #CCC");
        }
        return imgBox;
    }

    private Anchor buildNomLink(Article art) {
        String nom = art.getNom() != null ? art.getNom() : "(sans nom)";
        Anchor nomLink = new Anchor("/detail/" + art.getIdArticle(), nom);
        nomLink.getStyle()
                .set("color", "#0000CC").set("font-size", "0.9rem")
                .set("overflow", "hidden").set("white-space", "nowrap")
                .set("text-decoration", "none").set("text-overflow", "ellipsis");
        return nomLink;
    }

    private void styleAnchorBtn(Anchor a) {
        a.getStyle()
                .set("background", "none")
                .set("border", "1px solid #6100C1")
                .set("color", "#6100C1")
                .set("border-radius", "20px")
                .set("font-size", "0.8rem")
                .set("cursor", "pointer")
                .set("padding", "4px 12px")
                .set("text-decoration", "none")
                .set("margin-top", "4px");
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

    // -------------------------------------------------------------------------
    // Méthodes de rafraîchissement
    // -------------------------------------------------------------------------
    private void refreshGrid(VerticalLayout gridContainer, VerticalLayout parentLayout) {
        gridContainer.removeAll();
        List<Article> articles = articleService.findArticlesByVendeurAndStatut(
                email, PAGE_SIZE, currentPage * PAGE_SIZE, 1);
        grid.setItems(articles);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
        gridContainer.add(grid);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
    }

    private void refreshGridMesObjetsEnVente(VerticalLayout gridContainer, VerticalLayout parentLayout) {
        gridContainer.removeAll();
        List<Article> articles = articleService.findArticlesByVendeurAndStatut(
                email, PAGE_SIZE, currentPage * PAGE_SIZE, 1);
        grid.setItems(articles);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
        gridContainer.add(grid);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
    }

    private void refreshGridMesAchats(VerticalLayout gridContainer, VerticalLayout parentLayout) {
        gridContainer.removeAll();
        List<Article> articles = articleService.findArticlesAchetes(email, PAGE_SIZE, currentPage * PAGE_SIZE);
        localGridPayment.setItems(articles);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
        gridContainer.add(localGridPayment);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
    }

    private void refreshGridMesVendus(VerticalLayout gridContainer, VerticalLayout parentLayout) {
        gridContainer.removeAll();
        List<Article> articles = articleService.findMesVendus(email, PAGE_SIZE, currentPage * PAGE_SIZE);
        localGridPayment.setItems(articles);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
        gridContainer.add(localGridPayment);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
    }

    private void refreshGridAlivrer(VerticalLayout gridContainer, VerticalLayout parentLayout) {
        gridContainer.removeAll();
        List<Article> articles = articleService.findArticlesALivrerEnAttente(email, currentPage + 1, PAGE_SIZE);
        localGridPayment.setItems(articles);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
        gridContainer.add(localGridPayment);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
    }

    private void refreshGridAllerChercher(VerticalLayout gridContainer, VerticalLayout parentLayout) {
        gridContainer.removeAll();
        List<Article> articles = articleService.findArticlesAllerChercher(email, currentPage + 1, PAGE_SIZE);
        localGridPayment.setItems(articles);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
        gridContainer.add(localGridPayment);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
    }

    private void refreshGridAttenteSoulevement(VerticalLayout gridContainer, VerticalLayout parentLayout) {
        gridContainer.removeAll();
        List<Article> articles = articleService.findArticlesAttenteSoulevement(email, currentPage + 1, PAGE_SIZE);
        localGridPayment.setItems(articles);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
        gridContainer.add(localGridPayment);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
    }
    private void refreshGridAPaye(VerticalLayout gridContainer, VerticalLayout parentLayout) {
        gridContainer.removeAll();
        List<Article> articles = articleService.findArticlesAPaye(email, currentPage + 1, PAGE_SIZE);
        localGridPayment.setItems(articles);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
        gridContainer.add(localGridPayment);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
    }

    private void refreshGridMesArticlesALivrerADomicilePourMoi(VerticalLayout gridContainer, VerticalLayout parentLayout) {

        gridContainer.removeAll();
        List<Article> articles = articleService.findArticlesALivrerADomicilePourMoi(email, currentPage + 1, PAGE_SIZE);
        localGridPayment.setItems(articles);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
        gridContainer.add(localGridPayment);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));


    }

    private void refreshGridMesArticlesAttenteLivraisonByPoste(VerticalLayout gridContainer, VerticalLayout parentLayout) {

        gridContainer.removeAll();
        List<Article> articles = articleService.findArticlesALivrerPourMoiParPoste(email, currentPage + 1, PAGE_SIZE);
        localGridPayment.setItems(articles);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
        gridContainer.add(localGridPayment);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));


    }

    private void refreshGridMesArticlesALivrerParPoste(VerticalLayout gridContainer, VerticalLayout parentLayout) {

        gridContainer.removeAll();
        List<Article> articles = articleService.findArticlesALivrerParPoste(email, currentPage + 1, PAGE_SIZE);
        localGridPayment.setItems(articles);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
        gridContainer.add(localGridPayment);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));


    }
    private void refreshGridMesArticlesALivrerADomicile(VerticalLayout gridContainer, VerticalLayout parentLayout) {

        gridContainer.removeAll();
        List<Article> articles = articleService.findArticlesALivrerADomicile(email, currentPage + 1, PAGE_SIZE);
        localGridPayment.setItems(articles);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
        gridContainer.add(localGridPayment);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));


    }

    private void refreshGrAttenteDePayemnt(VerticalLayout gridContainer, VerticalLayout parentLayout) {
        gridContainer.removeAll();
        List<Article> articles = articleService.findArticleAttenteDePayement(email, currentPage + 1, PAGE_SIZE);
        localGridPayment.setItems(articles);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
        gridContainer.add(localGridPayment);
        gridContainer.add(buildInlinePagination(gridContainer, parentLayout));
    }

    // -------------------------------------------------------------------------
    // Pagination inline
    // -------------------------------------------------------------------------
    private HorizontalLayout buildInlinePagination(VerticalLayout gridContainer, VerticalLayout parentLayout) {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(Alignment.CENTER);
        row.getStyle().set("padding", "12px 0").set("gap", "6px");

        row.add(inlinePageBtn("«", 0,               currentPage == 0,              gridContainer, parentLayout));
        row.add(inlinePageBtn("‹", currentPage - 1, currentPage == 0,              gridContainer, parentLayout));

        int start = Math.max(0, currentPage - 2);
        int end   = Math.min(totalPages - 1, currentPage + 2);

        if (start > 0) {
            row.add(inlinePageBtn("1", 0, false, gridContainer, parentLayout));
            if (start > 1) {
                Span dots = new Span("…");
                dots.getStyle().set("color", "#6100C1").set("font-size", "0.85rem");
                row.add(dots);
            }
        }

        for (int i = start; i <= end; i++) {
            boolean isActive = (i == currentPage);
            if (isActive) {
                Div activeBtn = new Div();
                activeBtn.setText(String.valueOf(i + 1));
                activeBtn.getStyle()
                        .set("width", "32px").set("height", "32px")
                        .set("display", "flex").set("align-items", "center")
                        .set("justify-content", "center").set("border-radius", "50%")
                        .set("font-size", "0.85rem").set("font-weight", "bold")
                        .set("background-color", "#6100C1").set("color", "white")
                        .set("border", "none").set("cursor", "default");
                row.add(activeBtn);
            } else {
                row.add(inlinePageBtn(String.valueOf(i + 1), i, false, gridContainer, parentLayout));
            }
        }

        if (end < totalPages - 1) {
            if (end < totalPages - 2) {
                Span dots = new Span("…");
                dots.getStyle().set("color", "#6100C1").set("font-size", "0.85rem");
                row.add(dots);
            }
            row.add(inlinePageBtn(String.valueOf(totalPages), totalPages - 1, false, gridContainer, parentLayout));
        }

        row.add(inlinePageBtn("›", currentPage + 1, currentPage >= totalPages - 1, gridContainer, parentLayout));
        row.add(inlinePageBtn("»", totalPages - 1,  currentPage >= totalPages - 1, gridContainer, parentLayout));

        Span info = new Span("Page " + (currentPage + 1) + " / " + totalPages);
        info.getStyle()
                .set("font-size", "0.78rem").set("color", "#6100C1")
                .set("margin-left", "8px").set("align-self", "center");
        row.add(info);

        return row;
    }

    private Div inlinePageBtn(String label, int targetPage, boolean disabled,
                              VerticalLayout gridContainer, VerticalLayout parentLayout) {
        Div btn = new Div();
        btn.setText(label);
        btn.getStyle()
                .set("display", "flex").set("align-items", "center")
                .set("justify-content", "center")
                .set("width", "32px").set("height", "32px")
                .set("border-radius", "50%")
                .set("border", "1px solid " + (disabled ? "#DDD" : "#6100C1"))
                .set("color", disabled ? "#CCC" : "#6100C1")
                .set("font-size", "0.85rem").set("font-weight", "bold")
                .set("cursor", disabled ? "default" : "pointer")
                .set("pointer-events", disabled ? "none" : "auto");

        if (!disabled) {
            btn.getElement().addEventListener("click", e -> {
                currentPage = targetPage;
                switch (activeView) {
                    case "myarticlepayes"           -> refreshGridAPaye(gridContainer, parentLayout);
                    case "mywaitdelivering"         -> refreshGridMesArticlesALivrerADomicilePourMoi(gridContainer, parentLayout);
                    case "mywaitdeliveringbypost"   -> refreshGridMesArticlesAttenteLivraisonByPoste(gridContainer, parentLayout);
                    case "mywaitpayement"           -> refreshGrAttenteDePayemnt(gridContainer, parentLayout);
                    case "myobjectstodeliver"       -> refreshGridMesArticlesALivrerADomicile(gridContainer, parentLayout);
                    case "myobjectstodeliverByPost" -> refreshGridMesArticlesALivrerParPoste(gridContainer, parentLayout);
                    case "mywaitpickup"             -> refreshGridAttenteSoulevement(gridContainer, parentLayout);
                    case "myarticletopickup"        -> refreshGridAllerChercher(gridContainer, parentLayout);
                    case "mysold"                   -> refreshGridMesVendus(gridContainer, parentLayout);
                    case "mybought"                 -> refreshGridMesAchats(gridContainer, parentLayout);
                    case "myselling"                -> refreshGridMesObjetsEnVente(gridContainer, parentLayout);
                    default                         -> refreshGrid(gridContainer, parentLayout);
                }
            });
        }
        return btn;
    }

    // -------------------------------------------------------------------------
    // Stats par marque
    // -------------------------------------------------------------------------

    private VerticalLayout buildStatsBrand() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        List<ReferenceService.CategorieItem> allCategories = referenceService.findAllCategories();

        Map<String, Integer> libelleToId = allCategories.stream()
                .collect(Collectors.toMap(
                        ReferenceService.CategorieItem::nom,
                        ReferenceService.CategorieItem::id,
                        (existing, newer) -> existing
                ));

        HorizontalLayout selectors = new HorizontalLayout();

        Select<String> catSel = new Select<>();
        catSel.setItems(allCategories.stream().map(ReferenceService.CategorieItem::nom).toList());
        if (!allCategories.isEmpty()) catSel.setValue(allCategories.get(0).nom()); // ✅ valeur par défaut
        Select<String> marqueSel = new Select<>();
        List<String> marques = referenceService.findAllBrands();
        marqueSel.setItems(marques);
        if (!marques.isEmpty()) marqueSel.setValue(marques.get(0));
        marqueSel.setWidth("150px");

        // ✅ Select<Integer> au lieu de Select<String>
        Select<Integer> anneesSel = new Select<>();
        List<Integer> annees = new ArrayList<>();
        for (int i = 2026; i >= 2020; i--) annees.add(i); // ✅ boucle correcte
        anneesSel.setItems(annees);
        anneesSel.setValue(2026);

        selectors.add(catSel, marqueSel, anneesSel);
        layout.add(selectors);

        Div chartContainer = new Div();
        layout.add(chartContainer);

        Runnable refreshChart = () -> {
            String marque  = marqueSel.getValue();
            Integer annee  = anneesSel.getValue();
            Integer catId  = libelleToId.get(catSel.getValue());

            // ✅ Garder catId null si aucune catégorie sélectionnée
            if (marque == null || annee == null || catId == null) return;

            List<Number> stats = referenceService.findStatsByBrandAndYear(marque, annee, catId);
            chartContainer.removeAll();
            chartContainer.add(buildBarChart(marque + " – " + annee, months(), stats.toArray(new Number[0])));
        };

        marqueSel.addValueChangeListener(e -> refreshChart.run());
        anneesSel.addValueChangeListener(e -> refreshChart.run());
        catSel.addValueChangeListener(e -> refreshChart.run());

        refreshChart.run();

        return layout; // ✅ Un seul return
    }
    private Div buildBarChart(String title, String[] categories, Number[] data) {
        double max = 1;
        for (Number n : data) if (n.doubleValue() > max) max = n.doubleValue();
        final double maxVal = max;

        Div wrapper = new Div();
        wrapper.getStyle().set("width", "100%").set("max-width", "520px").set("padding", "8px 0");

        Div chartTitle = new Div();
        chartTitle.setText(title);
        chartTitle.getStyle().set("text-align", "center").set("font-size", "0.9rem")
                .set("font-weight", "bold").set("margin-bottom", "8px");
        wrapper.add(chartTitle);

        Div chart = new Div();
        chart.getStyle()
                .set("display", "flex").set("align-items", "flex-end")
                .set("height", "200px").set("border-left", "2px solid #333")
                .set("border-bottom", "2px solid #333").set("padding", "0 8px").set("gap", "4px");

        for (int i = 0; i < data.length; i++) {
            double val = data[i].doubleValue();
            int heightPct = maxVal > 0 ? (int) Math.round((val / maxVal) * 100) : 0;

            Div colWrapper = new Div();
            colWrapper.getStyle().set("display", "flex").set("flex-direction", "column")
                    .set("align-items", "center").set("flex", "1");

            Span valLabel = new Span(val > 0 ? String.valueOf((int) val) : "");
            valLabel.getStyle().set("font-size", "0.7rem").set("color", "#333");

            Div bar = new Div();
            bar.getStyle().set("width", "100%").set("height", heightPct + "%")
                    .set("min-height", val > 0 ? "4px" : "0").set("background-color", "#FF0000");

            colWrapper.add(valLabel, bar);
            chart.add(colWrapper);
        }
        wrapper.add(chart);

        Div xAxis = new Div();
        xAxis.getStyle().set("display", "flex").set("padding", "2px 8px 0").set("gap", "4px");
        for (String cat : categories) {
            Span lbl = new Span(cat);
            lbl.getStyle().set("flex", "1").set("text-align", "center")
                    .set("font-size", "0.65rem").set("color", "#333");
            xAxis.add(lbl);
        }
        wrapper.add(xAxis);
        return wrapper;
    }

    // -------------------------------------------------------------------------
    // Navigation secondaire
    // -------------------------------------------------------------------------
    private HorizontalLayout buildSecondaryNav() {
        HorizontalLayout nav = new HorizontalLayout();
        nav.getStyle().set("color", "#0000CC").set("font-size", "0.85rem");
        nav.add(
                navLink(getTranslation("account.nav.info"),          "/mon-compte/informations"),
                navLink(getTranslation("account.nav.bids"),          "/encheres"),
                navLink(getTranslation("account.nav.add"),    "/ajouter-article"),
                navLink(getTranslation("account.nav.ads"), "/publicite"),
                navLink(getTranslation("account.nav.calendar"), "/calendrier")
        );
        return nav;
    }

    // -------------------------------------------------------------------------
    // Dialogs
    // -------------------------------------------------------------------------

    // Vendeur confirme avoir reçu le paiement
    private void showFaireOffreDialog(Article article) {
        Dialog dlg = new Dialog();
        ResponsiveHelper.dialogWidth(dlg, 380);
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);

        H4 titre = new H4(getTranslation("account.dlg.payment.received.title"));
        titre.getStyle().set("color", "#0000CC").set("margin", "0");
        content.add(titre, new Hr());
        content.add(new Span(getTranslation("account.article.line", article.getNom() != null ? article.getNom() : "—")));

        Button annulerBtn = new Button(getTranslation("common.cancel"));
        annulerBtn.getStyle().set("background-color", "#E0E0E0").set("border", "1px solid #999");
        annulerBtn.addClickListener(e -> dlg.close());

        Button confirmerBtn = new Button(getTranslation("common.confirm"));
        confirmerBtn.getStyle().set("background-color", "#0000CC").set("color", "white").set("border", "none");
        confirmerBtn.addClickListener(e -> {
            Integer refVendeur = articleService.findRefUtilisateurByEmail(email);
            System.out.println("ref vendeur" + refVendeur);
            System.out.println("ref acheteur" + article.getRefAcheteur());
            Personne acheteur = new Personne();;
            acheteur = personneService.findById(article.getRefAcheteur());
            Personne vendeur = new Personne();;
            vendeur = personneService.findById(article.getRefVendeur().longValue());

            articleService.insertAPaye(
                    article.getIdArticle().intValue(),
                    refVendeur,
                    article.getRefAcheteur(),
                    article.getPrix() != null ? BigDecimal.valueOf(article.getPrix()) : BigDecimal.ZERO,
                    LocalDateTime.now(),
                    article.getRefModeDeLivraison(),
                    article.getRefConditionPayement(),
                    1,
                    acheteur.getRefCanton(),
                    article.getIdApaye());
            showContent("mywaitpayement");

            envoyerEmailPayementReussi(vendeur.getEmail(), vendeur.getNomUtilisateur(), article.getNom());
            //Notification.show("Paiement confirmé pour : " + article.getNom());
            dlg.close();
        });

        content.add(new HorizontalLayout(annulerBtn, confirmerBtn));
        dlg.add(content);
        dlg.open();
    }

    private void envoyerEmailAdresseManquante(String email, String nom) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setFrom("robot@avant-garde.ch");
        msg.setSubject("Votre adresse est manquante !");
        msg.setText("""
                Bonjour %s,
                
                Vous n'avez pas configurer votre adresse dans votre page personelle
                
                Veuillez vous connecter à votre compte et remplir les informations néceessairess .
                
                L'équipe Avant-Garde
                """.formatted(nom));
        try {
            mailSender.send(msg);
            Notification.show(getTranslation("account.notif.email.sent"));
        } catch (Exception e) {
            System.err.println(" Erreur envoi email acheteur : " + e.getMessage());
        }
    }

    private void envoyerEmailCompteOuTwtinzManquant(String email, String nom) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setFrom("robot@avant-garde.ch");
        msg.setSubject("Votre compte de payement est manquant !");
        msg.setText("""
                Bonjour %s,
                
                Vous n'avez pas configurer votre compte de payement / Twint
                
                Veuillez vous connecter à votre compte et remplir les informations néceessairess .
                
                L'équipe Avant-Garde
                """.formatted(nom));
        try {
            mailSender.send(msg);
            Notification.show(getTranslation("account.notif.email.sent"));
        } catch (Exception e) {
            System.err.println(" Erreur envoi email acheteur : " + e.getMessage());
        }
    }

    private void envoyerEmailPayementReussi(String email, String nomArticle, String nom) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setFrom("robot@avant-garde.ch");
        msg.setSubject("Vous avez reçu un payement!");
        msg.setText("""
                Bonjour %s,
                
                Félicitations ! Vous avez reçu un payement pour l'article : %s
                
                Veuillez vous connecter à votre compte pour procéder à la lvraison  .
                
                L'équipe Avant-Garde
                """.formatted(nomArticle, nom));
        try {
            mailSender.send(msg);
            Notification.show(getTranslation("account.notif.email.sent"));
        } catch (Exception e) {
            System.err.println("❌ Erreur envoi email acheteur : " + e.getMessage());
        }
    }

    private void envoyerEmailPayement(String email, String nomArticle, String nom) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setFrom("robot@avant-garde.ch");
        msg.setSubject("Vous avez reçu un payement!");
        msg.setText("""
                Bonjour %s,
                
                Félicitations ! Vous avez reçu un payement pour l'article : %s
                
                Veuillez vous connecter à votre compte pour le valider
                
                L'équipe Avant-Garde
                """.formatted(nomArticle, nom));
        try {
            mailSender.send(msg);
            Notification.show(getTranslation("account.notif.email.sent"));
        } catch (Exception e) {
            System.err.println("❌ Erreur envoi email acheteur : " + e.getMessage());
        }
    }


    // Acheteur confirme qu'il a payé

    private void showConfirmerPayement(Article article) {
        Dialog dlg = new Dialog();
        ResponsiveHelper.dialogWidth(dlg, 380);

        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);

        H4 titre = new H4(getTranslation("account.btn.confirm.payment"));
        titre.getStyle().set("color", "#0000CC").set("margin", "0");
        content.add(titre, new Hr());
        content.add(fieldRow(getTranslation("account.col.article"), article.getNom()));

        // Initialiser refAcheteur
        Integer personneId = this.referenceService.findPersonneId(article.getIdArticle().intValue());
        if (personneId != null) {
            article.setRefAcheteur(personneId.longValue());
        }

        // Récupérer le vendeur
        Personne personne = this.personneService.findById(article.getRefVendeur().longValue());
        if (personne == null) {
            Notification.show(getTranslation("account.notif.seller.not.found"));
            return;
        }

        // Initialiser montant
        if (article.getMontant() == null) {
            if (article.getPrix() != null) {
                article.setMontant(BigDecimal.valueOf(article.getPrix()));
            } else {
                Double offre = this.referenceService.findDerniereOffre(article.getIdArticle().intValue());
                article.setMontant(offre != null ? BigDecimal.valueOf(offre) : BigDecimal.ZERO);
            }
        }

        // Vérifier la condition de paiement
        Integer refCond = article.getRefConditionPayement();
        if (refCond == null) {
            Notification.show(getTranslation("account.notif.payment.undefined"));
            return;
        }

        boolean[] aEnvoyer = {false};

        referenceService.findAllPayements().stream()
                .filter(p -> p.id() == refCond.intValue())
                .findFirst()
                .ifPresent(p -> {
                    content.add(fieldRow(getTranslation("account.field.payment.condition"), p.nom()));

                    if (refCond.equals(5)) {
                        // Twint
                        content.add(fieldRow(getTranslation("account.field.twint"), personne.getNoTelephone()));
                        if (personne.getNoTelephone() == null || personne.getNoTelephone().isEmpty()) {
                            Notification.show(getTranslation("account.notif.twint.missing", personne.getNom()));
                            envoyerEmailCompteOuTwtinzManquant(personne.getEmail(), personne.getNom());
                        } else {
                            aEnvoyer[0] = true;
                        }

                    } else if (refCond.equals(3)) {
                        // Virement bancaire
                        content.add(fieldRow(getTranslation("account.field.iban"), personne.getIban()));
                        if (personne.getIban() == null || personne.getIban().isEmpty()) {
                            Notification.show(getTranslation("account.notif.iban.missing", personne.getNom()));
                            envoyerEmailCompteOuTwtinzManquant(personne.getEmail(), personne.getNom());
                        } else {
                            aEnvoyer[0] = true;
                        }

                    } else {
                        // Cas 1 (Paiement comptant), 2 (PayPal), 4 (Carte de crédit)
                        // Pas d'info bancaire requise → on autorise directement
                        aEnvoyer[0] = true;
                    }
                });

        // Boutons
        Button annulerBtn = new Button(getTranslation("common.cancel"));
        annulerBtn.getStyle().set("background-color", "#E0E0E0").set("border", "1px solid #999");
        annulerBtn.addClickListener(e -> dlg.close());

        Button confirmerBtn = new Button(getTranslation("common.confirm"));
        confirmerBtn.getStyle().set("background-color", "#0000CC").set("color", "white").set("border", "none");
        confirmerBtn.setEnabled(aEnvoyer[0]);

        confirmerBtn.addClickListener(e -> {
            this.articleService.updateStatutPaye(article.getIdArticle().intValue(), article);
            envoyerEmailPayementReussi(personne.getEmail(), personne.getNomUtilisateur(), article.getNom());
            dlg.close();
            showContent("myarticlepayes");
        });

        content.add(new HorizontalLayout(annulerBtn, confirmerBtn));
        dlg.add(content);
        dlg.open();
    }
    private void showConfirmerReceptionDialog(Article article) {
        Dialog dlg = new Dialog();
        ResponsiveHelper.dialogWidth(dlg, 380);
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);

        H4 titre = new H4(getTranslation("account.dlg.pickedup.title"));
        titre.getStyle().set("color", "#0000CC").set("margin", "0");
        content.add(titre, new Hr());
        content.add(new Span(getTranslation("account.article.line", article.getNom() != null ? article.getNom() : "—")));

        Button annulerBtn = new Button(getTranslation("common.cancel"));
        annulerBtn.getStyle().set("background-color", "#E0E0E0").set("border", "1px solid #999");
        annulerBtn.addClickListener(e -> dlg.close());

        Button confirmerBtn = new Button(getTranslation("account.btn.confirm.reception"));
        confirmerBtn.getStyle().set("background-color", "#0000CC").set("color", "white").set("border", "none");
        confirmerBtn.addClickListener(e -> {
            //Notification.show("A aller cherher id : " + article.getIdAlivre());
            articleService.confirmerReception(article.getIdArticle().intValue(), article.getRefAcheteur().intValue(), article);
            Notification.show(getTranslation("account.notif.reception.confirmed", article.getNom()));
            dlg.close();
            showContent("myarticletopickup");
        });

        content.add(new HorizontalLayout(annulerBtn, confirmerBtn));
        dlg.add(content);
        dlg.open();
    }

    private void showConfirmerReceptionSoulevementDialog(Article article) {
        Dialog dlg = new Dialog();
        ResponsiveHelper.dialogWidth(dlg, 380);
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);

        H4 titre = new H4(getTranslation("account.dlg.buyerpickedup.title"));
        titre.getStyle().set("color", "#0000CC").set("margin", "0");
        content.add(titre, new Hr());
        content.add(new Span(getTranslation("account.article.line", article.getNom() != null ? article.getNom() : "—")));

        Button annulerBtn = new Button(getTranslation("common.cancel"));
        annulerBtn.getStyle().set("background-color", "#E0E0E0").set("border", "1px solid #999");
        annulerBtn.addClickListener(e -> dlg.close());

        Button confirmerBtn = new Button(getTranslation("account.btn.confirm.reception"));
        confirmerBtn.getStyle().set("background-color", "#0000CC").set("color", "white").set("border", "none");
        confirmerBtn.addClickListener(e -> {
            articleService.confirmerReceptionVendeur(article.getIdArticle().intValue(), article.getRefVendeur().intValue(), article.getIdAlivre(), article);
            Notification.show(getTranslation("account.notif.reception.confirmed", article.getNom()));
            dlg.close();
            showContent("mywaitpickup");
        });

        content.add(new HorizontalLayout(annulerBtn, confirmerBtn));
        dlg.add(content);
        dlg.open();
    }

    // Acheteur confirme la réception
    private void  showConfirmerReceptionADomicileDialog(Article article) {
        Dialog dlg = new Dialog();
        ResponsiveHelper.dialogWidth(dlg, 380);
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);

        H4 titre = new H4(getTranslation("account.dlg.home.reception.title"));
        titre.getStyle().set("color", "#0000CC").set("margin", "0");
        content.add(titre, new Hr());
        content.add(new Span(getTranslation("account.article.line", article.getNom() != null ? article.getNom() : "—")));

        Button annulerBtn = new Button(getTranslation("common.cancel"));
        annulerBtn.getStyle().set("background-color", "#E0E0E0").set("border", "1px solid #999");
        annulerBtn.addClickListener(e -> dlg.close());

        Button confirmerBtn = new Button(getTranslation("account.btn.confirm.reception"));
        confirmerBtn.getStyle().set("background-color", "#0000CC").set("color", "white").set("border", "none");
        confirmerBtn.addClickListener(e -> {
            articleService.confirmerReception(article.getIdArticle().intValue(), article.getRefAcheteur().intValue(), article);
            Notification.show(getTranslation("account.notif.reception.confirmed", article.getNom()));
            dlg.close();
            showContent("mywaitdeliveringbypost");
        });

        content.add(new HorizontalLayout(annulerBtn, confirmerBtn));
        dlg.add(content);
        dlg.open();
    }
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

    // Vendeur confirme la livraison
    private void showConfirmerLivraison(Article article) {
        Dialog dlg = new Dialog();
        ResponsiveHelper.dialogWidth(dlg, 380);
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);

        H4 titre = new H4(getTranslation("account.btn.confirm.delivery"));
        titre.getStyle().set("color", "#0000CC").set("margin", "0");
        content.add(titre, new Hr());
        content.add(new Span(getTranslation("account.article.line", article.getNom() != null ? article.getNom() : "—")));

        Button annulerBtn = new Button(getTranslation("common.cancel"));
        annulerBtn.getStyle().set("background-color", "#E0E0E0").set("border", "1px solid #999");
        annulerBtn.addClickListener(e -> dlg.close());

        Button confirmerBtn = new Button(getTranslation("common.confirm"));
        confirmerBtn.getStyle().set("background-color", "#0000CC").set("color", "white").set("border", "none");

        // ✅ Initialiser refAcheteur
        Integer personneId = this.referenceService.findPersonneId(article.getIdArticle().intValue());
        Personne personne = this.personneService.findById(personneId.longValue());
        if (personneId != null) {
            article.setRefAcheteur(personneId.longValue());
        }

        // ✅ Initialiser montant
        if (article.getMontant() == null) {
            if (article.getPrix() != null) {
                article.setMontant(BigDecimal.valueOf(article.getPrix()));
            } else {
                Double offre = this.referenceService.findDerniereOffre(article.getIdArticle().intValue());
                article.setMontant(offre != null ? BigDecimal.valueOf(offre) : BigDecimal.ZERO);
            }
        }

        confirmerBtn.addClickListener(e -> {
            if (personne.getAdresse() == null || personne.getAdresse().length() == 0) {
                envoyerEmailAdresseManquante(personne.getEmail(), personne.getNom());
                Notification.show(getTranslation("account.notif.address.missing"));

            }else {
                this.articleService.updateStatutEstLivre(article.getIdArticle().intValue(), article, personne);
                Notification.show(getTranslation("account.notif.delivery.confirmed", article.getNom()));
                showContent("mywaitdelivering");
            }
            dlg.close();
        });

        content.add(new HorizontalLayout(annulerBtn, confirmerBtn));
        dlg.add(content);
        dlg.open();
    }


    private void showConfirmerLivraisonByPost(Article article) {
        Dialog dlg = new Dialog();
        ResponsiveHelper.dialogWidth(dlg, 380);
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);

        H4 titre = new H4(getTranslation("account.dlg.delivery.post.title"));
        titre.getStyle().set("color", "#0000CC").set("margin", "0");
        content.add(titre, new Hr());
        content.add(new Span(getTranslation("account.article.line", article.getNom() != null ? article.getNom() : "—")));

        Button annulerBtn = new Button(getTranslation("common.cancel"));
        annulerBtn.getStyle().set("background-color", "#E0E0E0").set("border", "1px solid #999");
        annulerBtn.addClickListener(e -> dlg.close());

        Button confirmerBtn = new Button(getTranslation("common.confirm"));
        confirmerBtn.getStyle().set("background-color", "#0000CC").set("color", "white").set("border", "none");

        // ✅ Initialiser refAcheteur
        Integer personneId = this.referenceService.findPersonneId(article.getIdArticle().intValue());
        Personne personne = this.personneService.findById(personneId.longValue());
        if (personneId != null) {
            article.setRefAcheteur(personneId.longValue());
        }

        // ✅ Initialiser montant
        if (article.getMontant() == null) {
            if (article.getPrix() != null) {
                article.setMontant(BigDecimal.valueOf(article.getPrix()));
            } else {
                Double offre = this.referenceService.findDerniereOffre(article.getIdArticle().intValue());
                article.setMontant(offre != null ? BigDecimal.valueOf(offre) : BigDecimal.ZERO);
            }
        }
        if (personne.getAdresse() == null || personne.getAdresse().length() == 0) {
            envoyerEmailAdresseManquante(personne.getEmail(), personne.getNom());
            Notification.show(getTranslation("account.notif.address.missing"));

        }else {
            confirmerBtn.addClickListener(e -> {
                this.articleService.updateStatutEstLivreParPoste(article.getIdArticle().intValue(), article, personne);
                Notification.show(getTranslation("account.notif.delivery.confirmed", article.getNom()));
                showContent("myobjectstodeliverByPost");
                dlg.close();
            });

            content.add(new HorizontalLayout(annulerBtn, confirmerBtn));
            dlg.add(content);
            dlg.open();
        }
    }


    private void showAcheteurDetails(Article article) {
        Dialog dlg = new Dialog();
        ResponsiveHelper.dialogWidth(dlg, 380);
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);

        H4 titre = new H4(getTranslation("account.dlg.buyer.address.title"));
        titre.getStyle().set("color", "#0000CC").set("margin", "0");
        content.add(titre, new Hr());

        //Integer personneId = this.referenceService.findPersonneId(article.getIdArticle().intValue());
        Integer personneId = this.referenceService.findIdAcheteur(article.getIdArticle().intValue());
        System.out.println(" Vendeur id : "  + personneId);
        if (personneId != null) {
            Personne personne = this.personneService.findById(personneId.longValue());
            if (personne != null) {
                TextField nom = new TextField(getTranslation("account.field.fullname"));
                nom.setValue(personne.getNom() != null ? personne.getNom() : "");
                nom.setReadOnly(true);

                TextField adresse = new TextField(getTranslation("account.field.address"));
                adresse.setValue(personne.getAdresse() != null ? personne.getAdresse() : "");
                adresse.setReadOnly(true);


                TextField ville = new TextField(getTranslation("account.field.city"));
                ville.setValue(personne.getVille() != null ? personne.getVille(): "");
                ville.setReadOnly(true);

                TextField pays = new TextField(getTranslation("account.field.country"));
                pays.setValue(personne.getPays() != null ? personne.getPays() : "");
                pays.setReadOnly(true);

                content.add(nom, adresse, ville, pays);
            }
        }

        Button fermerBtn = new Button(getTranslation("common.close"));
        fermerBtn.getStyle().set("background-color", "#E0E0E0").set("border", "1px solid #999");
        fermerBtn.addClickListener(e -> dlg.close());

        content.add(new HorizontalLayout(fermerBtn));
        dlg.add(content);
        dlg.open();
    }

    private void showVendeurDetails(Article article) {
        Dialog dlg = new Dialog();
        ResponsiveHelper.dialogWidth(dlg, 380);
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);

        H4 titre = new H4(getTranslation("account.dlg.seller.address.title"));
        titre.getStyle().set("color", "#0000CC").set("margin", "0");
        content.add(titre, new Hr());

        //Integer personneId = this.referenceService.findPersonneId(article.getIdArticle().intValue());
        Integer personneId = this.referenceService.findIdVendeur(article.getIdArticle().intValue());
        if (personneId != null) {
            Personne personne = this.personneService.findById(personneId.longValue());
            if (personne != null) {
                TextField nom = new TextField(getTranslation("account.field.fullname"));
                nom.setValue(personne.getNom() != null ? personne.getNom() : "");
                nom.setReadOnly(true);

                TextField adresse = new TextField(getTranslation("account.field.address"));
                adresse.setValue(personne.getAdresse() != null ? personne.getAdresse() : "");
                adresse.setReadOnly(true);


                TextField ville = new TextField(getTranslation("account.field.city"));
                ville.setValue(personne.getVille() != null ? personne.getVille(): "");
                ville.setReadOnly(true);

                TextField pays = new TextField(getTranslation("account.field.country"));
                pays.setValue(personne.getPays() != null ? personne.getPays() : "");
                pays.setReadOnly(true);

                content.add(nom, adresse, ville, pays);
            }
        }

        Button fermerBtn = new Button(getTranslation("common.close"));
        fermerBtn.getStyle().set("background-color", "#E0E0E0").set("border", "1px solid #999");
        fermerBtn.addClickListener(e -> dlg.close());

        content.add(new HorizontalLayout(fermerBtn));
        dlg.add(content);
        dlg.open();
    }

    // Vendeur voit les détails de l'acheteur
    private void showAcheteurDetail(Article article) {
        Dialog dlg = new Dialog();
        ResponsiveHelper.dialogWidth(dlg, 380);
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);

        H4 titre = new H4(getTranslation("account.dlg.buyer.details.title"));
        titre.getStyle().set("color", "#0000CC").set("margin", "0");
        content.add(titre, new Hr());
        Integer acheteurId = article.getRefAcheteur().intValue();
        //Integer personneId = this.referenceService.findPersonneId(article.getIdArticle().intValue());
        if (acheteurId != null) {
            Personne personne = this.personneService.findById(acheteurId.longValue());
            if (personne != null) {
                TextField nom = new TextField(getTranslation("account.field.fullname"));
                nom.setValue(personne.getNom() != null ? personne.getNom() : "");
                nom.setReadOnly(true);

                TextField adresse = new TextField(getTranslation("account.field.address"));
                adresse.setValue(personne.getAdresse() != null ? personne.getAdresse() : "");
                adresse.setReadOnly(true);

                TextField pays = new TextField(getTranslation("account.field.country"));
                pays.setValue(personne.getPays() != null ? personne.getPays() : "");
                pays.setReadOnly(true);

                content.add(nom, adresse, pays);
            }
        }

        Button fermerBtn = new Button(getTranslation("common.close"));
        fermerBtn.getStyle().set("background-color", "#E0E0E0").set("border", "1px solid #999");
        fermerBtn.addClickListener(e -> dlg.close());

        content.add(new HorizontalLayout(fermerBtn));
        dlg.add(content);
        dlg.open();
    }

    // Acheteur note un article
    private void showNoterDIalogue(Article article) {
        Dialog dlg = new Dialog();
        ResponsiveHelper.dialogWidth(dlg, 380);
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);

        H4 titre = new H4(getTranslation("account.dlg.rate.title"));
        titre.getStyle().set("color", "#0000CC").set("margin", "0");
        content.add(titre, new Hr());
        content.add(new Span(getTranslation("account.article.line", article.getNom() != null ? article.getNom() : "—")));

        ComboBox<Integer> comboNotes = new ComboBox<>(getTranslation("account.field.rating"));
        comboNotes.setItems(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        content.add(comboNotes);

        Button annulerBtn = new Button(getTranslation("common.cancel"));
        annulerBtn.getStyle().set("background-color", "#E0E0E0").set("border", "1px solid #999");
        annulerBtn.addClickListener(e -> dlg.close());

        Button confirmerBtn = new Button(getTranslation("common.confirm"));
        confirmerBtn.getStyle().set("background-color", "#0000CC").set("color", "white").set("border", "none");
        confirmerBtn.addClickListener(e -> {
            Integer note = comboNotes.getValue();
            if (note != null) {
                Integer refAcheteur = this.articleService.findRefUtilisateurByEmail(email);
                this.referenceService.enregistrerNote(
                        article.getIdArticle().intValue(),
                        refAcheteur.intValue(),
                        note
                );
                Notification.show(getTranslation("account.notif.rating.saved"));
            }
            dlg.close();
        });

        content.add(new HorizontalLayout(annulerBtn, confirmerBtn));
        dlg.add(content);
        dlg.open();
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private Anchor navLink(String text, String href) {
        Anchor a = new Anchor(href, text);
        a.getStyle().set("color", "#0000CC").set("font-size", "0.85rem").set("text-decoration", "none");
        return a;
    }

    private Span cell(String text, String width) {
        Span s = new Span(text);
        s.getStyle().set("width", width).set("font-size", "0.85rem");
        return s;
    }

    private OAuth2User getConnectedUser() {
        return (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
