package com.example.recordz.ui.layouts;

import com.example.recordz.service.ReferenceService;
import com.example.recordz.ui.CategorieView;
import com.example.recordz.ui.StyleHelper;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import java.util.Locale;

@Layout
@AnonymousAllowed
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainLayout extends AppLayout {

    private final ReferenceService referenceService;

    public MainLayout(ReferenceService referenceService) {
        this.referenceService = referenceService;
        setPrimarySection(Section.DRAWER);
        buildNavbar();
        buildDrawer();
    }

    // ── Navbar ────────────────────────────────────────────────────────────

    private void buildNavbar() {
        // ✅ Bouton hamburger : indispensable sur mobile pour ouvrir le drawer
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Ouvrir le menu des catégories");

        Select<Locale> langSelect = buildLanguageSelect();
        FlexLayout mainNav = buildMainNav();

        HorizontalLayout top = new HorizontalLayout(toggle, mainNav, langSelect);
        top.expand(mainNav);          // pousse le sélecteur à droite
        top.setWidthFull();
        top.setPadding(false);
        top.setSpacing(false);
        top.setAlignItems(FlexComponent.Alignment.CENTER);
        VerticalLayout navbar = new VerticalLayout();
        navbar.setSpacing(false);
        navbar.setPadding(false);
        navbar.setWidthFull();
        navbar.addClassName("layout-navbar");
        navbar.getStyle().set("background-color", "white");
        navbar.add(top, buildSearchBar());
        addToNavbar(true, navbar);

        getElement().getStyle()
                .set("--lumo-base-color", "white")
                .set("background-color", "white");
    }

    private FlexLayout buildMainNav() {
        FlexLayout nav = new FlexLayout();
        nav.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        nav.addClassName("layout-nav-links");
        nav.getStyle().set("padding", "0 0.5rem").set("gap", "0.25rem 1rem");

        String[][] links = {
                {getTranslation("nav.home"),    ""},
                {getTranslation("nav.account"), "mon-compte"},
                {getTranslation("nav.about"),   "a-propos"},
                {getTranslation("nav.pricing"), "tarif"}
        };
        TextField nomField = new TextField();
        StyleHelper.applyTextFieldStyle(nomField);
        nomField.addClassName("layout-search-field");
        nomField.setPlaceholder(getTranslation("search.name"));
        Button searchBtn = styledButton(getTranslation("search.button"));
        for (String[] l : links) {
            Anchor a = new Anchor("/" + l[1], l[0]);
            a.addClassName("layout-nav-link");
            a.getStyle()
                    .set("color", "#0000CC")
                    .set("font-size", "0.85rem")
                    .set("text-decoration", "none")
                    .set("padding", "8px 0");   // zone tactile plus confortable
            nav.add(a);
        }
        return nav;
    }

    private FlexLayout buildSearchBar() {
        // ✅ FlexLayout + wrap : les champs passent à la ligne au lieu de déborder
        FlexLayout bar = new FlexLayout();
        bar.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        bar.setAlignItems(FlexComponent.Alignment.CENTER);
        bar.setWidthFull();
        bar.addClassName("layout-search-bar");
        bar.getStyle().set("padding", "4px 1rem 8px 1rem").set("gap", "6px");

        Span icon = new Span("🔍");
        Span nomLabel = new Span("Nom");
        nomLabel.getStyle().set("color", "#0000CC").set("font-weight", "bold");

        TextField nomField = new TextField();
        StyleHelper.applyTextFieldStyle(nomField);
        nomField.addClassName("layout-search-field");
        nomField.setPlaceholder("Nom");
        nomField.setMinWidth("140px");
        nomField.setMaxWidth("240px");
        nomField.setWidth("200px");
        nomField.getStyle().set("flex", "1 1 140px").set("border", "1px solid #0000CC");

        Select<ReferenceService.CategorieItem> catSelect = StyleHelper.styledSelect("");
        catSelect.addClassName("layout-search-field");

        // Item par défaut "--------"
        ReferenceService.CategorieItem defaultItem = new ReferenceService.CategorieItem(0, "--------");

        List<ReferenceService.CategorieItem> allItems = new ArrayList<>();
        allItems.add(defaultItem);
        allItems.addAll(referenceService.findAllCategories());

        catSelect.setItems(allItems);
        catSelect.setItemLabelGenerator(ReferenceService.CategorieItem::nom);
        catSelect.setValue(defaultItem);
        catSelect.setMinWidth("140px");
        catSelect.setMaxWidth("220px");
        catSelect.setWidth("175px");
        catSelect.getStyle().set("flex", "1 1 140px");

        Button searchBtn = styledButton("Rechercher");

        Runnable doSearch = () -> searchBtn.getUI().ifPresent(ui -> {
            String nom = nomField.getValue() == null ? "" : nomField.getValue();
            ReferenceService.CategorieItem selected = catSelect.getValue();
            int refCat = (selected != null && selected.id() != 0) ? selected.id() : 0;
            // ✅ Encodage de la valeur saisie (espaces, &, accents…)
            ui.navigate("categorie/" + refCat + "?nom="
                    + URLEncoder.encode(nom, StandardCharsets.UTF_8));
        });

        searchBtn.addClickListener(e -> doSearch.run());
        // ✅ Touche "Entrée" / "Rechercher" du clavier mobile
        nomField.addKeyDownListener(Key.ENTER, e -> doSearch.run());

        bar.add(icon, nomLabel, nomField, catSelect, searchBtn);
        return bar;
    }

    private Button styledButton(String label) {
        Button btn = new Button(label);
        btn.addClassName("layout-search-btn");
        btn.getStyle()
                .set("background", "none")
                .set("border", "1px solid #6100C1")
                .set("color", "#6100C1")
                .set("border-radius", "20px")
                .set("font-size", "0.8rem")
                .set("cursor", "pointer")
                .set("padding", "4px 16px")
                .set("min-height", "40px");      // cible tactile
        return btn;
    }

    // ── Drawer (catégories) ───────────────────────────────────────────────

    private void buildDrawer() {
        VerticalLayout sidebar = new VerticalLayout();
        sidebar.setSpacing(false);
        sidebar.setPadding(true);
        sidebar.setWidthFull();                  // ✅ plus de 185px fixes
        sidebar.addClassName("layout-drawer");
        sidebar.getStyle().set("background-color", "white");

        referenceService.findAllCategories().forEach(cat -> {
            RouterLink link = new RouterLink(cat.nom(), CategorieView.class, cat.id());
            link.getStyle()
                    .set("color", "#0000CC")
                    .set("font-weight", "bold")
                    .set("font-size", "0.9rem")
                    .set("display", "block")
                    .set("padding", "8px 0")     // ✅ cible tactile (remplace les marges)
                    .set("text-decoration", "none");
            sidebar.add(link);
        });

        addToDrawer(sidebar);
    }

    public static final String SESSION_LOCALE = "app.locale";

    private Select<Locale> buildLanguageSelect() {
        Select<Locale> langSelect = new Select<>();
        langSelect.setItems(Locale.FRENCH, Locale.ENGLISH, Locale.GERMAN);
        langSelect.setItemLabelGenerator(l -> switch (l.getLanguage()) {
            case "de" -> "🇩🇪 Deutsch";
            case "en" -> "🇬🇧 English";
            default   -> "🇫🇷 Français";
        });
        langSelect.setWidth("140px");
        langSelect.setAriaLabel("Langue / Language / Sprache");
        langSelect.getStyle().set("margin-right", "0.5rem");

        // Sélection initiale d'après la locale courante de l'UI
        String lang = UI.getCurrent() != null ? UI.getCurrent().getLocale().getLanguage() : "fr";
        langSelect.setValue(switch (lang) {
            case "de" -> Locale.GERMAN;
            case "en" -> Locale.ENGLISH;
            default   -> Locale.FRENCH;
        });

        langSelect.addValueChangeListener(e -> {
            if (!e.isFromClient() || e.getValue() == null) {
                return;
            }
            Locale newLocale = e.getValue();
            VaadinSession.getCurrent().setAttribute(SESSION_LOCALE, newLocale);
            UI ui = UI.getCurrent();
            ui.setLocale(newLocale);
            ui.getPage().reload();   // reconstruit les vues avec la nouvelle langue
        });
        return langSelect;
    }
}
