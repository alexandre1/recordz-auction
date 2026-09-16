package com.example.recordz.ui.layouts;

import com.example.recordz.ui.StyleHelper;
import com.example.recordz.service.ReferenceService;
import com.example.recordz.ui.CategorieView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.ArrayList;
import java.util.List;

@Layout
@AnonymousAllowed
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainLayout extends AppLayout {

    private final ReferenceService referenceService;

    public MainLayout(ReferenceService referenceService) {
        this.referenceService = referenceService;
        buildNavbar();
        buildDrawer();
    }

    private void buildNavbar() {
        VerticalLayout navbar = new VerticalLayout();
        navbar.setSpacing(false);
        navbar.setPadding(false);
        navbar.getStyle()
                .set("background-color", "white")
                .set("width", "100%");
        navbar.add(buildMainNav(), buildSearchBar());
        addToNavbar(true, navbar);

        getElement().getStyle()
                .set("--lumo-base-color", "white")
                .set("background-color", "white");
    }

    private HorizontalLayout buildMainNav() {
        HorizontalLayout nav = new HorizontalLayout();
        nav.getStyle().set("padding", "0 1rem").set("gap", "1rem").set("flex-wrap", "wrap");

        String[][] links = {
                {"Home",       ""},
                {"Mon Compte", "mon-compte"},
                {"A propos",   "a-propos"},
                {"Tarif",      "tarif"}
        };

        for (String[] l : links) {
            Anchor a = new Anchor("/" + l[1], l[0]);
            a.getStyle()
                    .set("color", "#0000CC")
                    .set("font-size", "0.85rem")
                    .set("text-decoration", "none");
            nav.add(a);
        }
        return nav;
    }

    private HorizontalLayout buildSearchBar() {
        HorizontalLayout bar = new HorizontalLayout();
        bar.getStyle().set("padding", "4px 1rem").set("align-items", "center").set("gap", "6px");

        Span icon = new Span("🔍");
        Span nomLabel = new Span("Nom");
        nomLabel.getStyle().set("color", "#0000CC").set("font-weight", "bold");

        TextField nomField = new TextField();
        StyleHelper.applyTextFieldStyle(nomField);
        nomField.setWidth("200px");
        nomField.getStyle().set("border", "1px solid #0000CC");

        Select<ReferenceService.CategorieItem> catSelect = StyleHelper.styledSelect("");

        // ✅ Item par défaut "--------"
        ReferenceService.CategorieItem defaultItem = new ReferenceService.CategorieItem(0, "--------");

        // ✅ Liste avec "--------" en tête
        List<ReferenceService.CategorieItem> allItems = new ArrayList<>();
        allItems.add(defaultItem);
        allItems.addAll(referenceService.findAllCategories());

        catSelect.setItems(allItems);
        catSelect.setItemLabelGenerator(ReferenceService.CategorieItem::nom);
        catSelect.setValue(defaultItem); // ✅ Sélectionner "--------" par défaut
        catSelect.setWidth("175px");

        Button searchBtn = styledButton("Rechercher");
        searchBtn.addClickListener(e ->
                searchBtn.getUI().ifPresent(ui -> {
                    String nom = nomField.getValue();
                    ReferenceService.CategorieItem selected = catSelect.getValue();
                    // ✅ Si "--------" sélectionné, refCat = 0 (toutes catégories)
                    int refCat = (selected != null && selected.id() != 0) ? selected.id() : 0;
                    ui.navigate("categorie/" + refCat + "?nom=" + nom);
                })
        );

        bar.add(icon, nomLabel, nomField, catSelect, searchBtn);
        return bar;
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

    private HorizontalLayout buildLogo() {
        Image logo = new Image("/images/xenia_white.png", "Avant-Garde");
        logo.getStyle().set("height", "60px");

        HorizontalLayout logoLayout = new HorizontalLayout(logo);
        logoLayout.setPadding(false);
        logoLayout.setSpacing(false);
        logoLayout.setAlignItems(com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER);
        logoLayout.getStyle()
                .set("background-color", "white")
                .set("padding", "4px 1rem");
        return logoLayout;
    }

    private HorizontalLayout buildQuickFilters() {
        HorizontalLayout nav = new HorizontalLayout();
        nav.getStyle().set("padding", "0 1rem").set("gap", "1rem").set("flex-wrap", "wrap");

        String[][] filtres = {
                {"Seulement les encheres",            "?enchere=1"},
                {"Les articles de la derniere heure", "?filtre=derniere-heure"},
                {"Que les articles de boutique neuf", "?filtre=boutique-neuf"}
        };

        for (String[] l : filtres) {
            Anchor a = new Anchor("/" + l[1], l[0]);
            a.getStyle()
                    .set("color", "#0000CC")
                    .set("font-size", "0.85rem")
                    .set("text-decoration", "none");
            nav.add(a);
        }
        return nav;
    }

    private void buildDrawer() {
        VerticalLayout sidebar = new VerticalLayout();
        sidebar.setSpacing(false);
        sidebar.setPadding(true);
        sidebar.setWidth("185px");
        sidebar.getStyle().set("background-color", "white");

        referenceService.findAllCategories().forEach(cat -> {
            RouterLink link = new RouterLink(cat.nom(), CategorieView.class, cat.id());
            link.getStyle()
                    .set("color", "#0000CC")
                    .set("font-weight", "bold")
                    .set("font-size", "0.85rem")
                    .set("display", "block")
                    .set("margin-bottom", "4px")
                    .set("margin-top", "10px")
                    .set("text-decoration", "none");
            sidebar.add(link);
        });

        addToDrawer(sidebar);
        setPrimarySection(Section.DRAWER);
    }
}