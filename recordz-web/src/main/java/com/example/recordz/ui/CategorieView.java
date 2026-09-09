package com.example.recordz.ui;

import com.example.recordz.model.domain.Article;
import com.example.recordz.model.domain.ArticleCategory;
import com.example.recordz.model.domain.ArticleSubCategory;
import com.example.recordz.model.domain.FiltreArticle;
import com.example.recordz.service.ArticleService;
import com.example.recordz.service.ReferenceService;
import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.vaadin.flow.component.dialog.Dialog;

@Route(value = "categorie", layout = MainLayout.class)
@AnonymousAllowed
@PageTitle("Avant-Garde - Catégorie")
public class CategorieView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final ReferenceService referenceService;
    private final ArticleService   articleService;

    private final Select<String> categorieSelect     = new Select<>();
    private final Select<String> sousCategorieSelect = new Select<>();
    private final Button         advSearch           = new Button("Recherche avancée");

    private int currentPage = 1;
    private int totalPages  = 1;

    private final List<ReferenceService.CategorieItem> allCategories;
    private final Map<String, Integer>                 libelleToId;

    // ✅ Source de vérité unique pour tous les filtres
    private final FiltreArticle filtre = new FiltreArticle();

    // Variables uniquement pour la recherche avancée (dialog)
    private String  filtreNom          = null;
    private String  filtreMarque       = null;
    private Double  filtrePrixMin      = null;
    private Double  filtrePrixMax      = null;
    private boolean filtreEncheresOnly = false;

    // ✅ Flag pour bloquer le listener sousCategorieSelect lors des setItems programmatiques
    private boolean sousCatChanging = false;

    private static final int PAGE_SIZE = 10;

    // ─────────────────────────────────────────────────────────────────────────
    // Constructeur
    // ─────────────────────────────────────────────────────────────────────────
    public CategorieView(ReferenceService referenceService, ArticleService articleService) {
        this.referenceService = referenceService;
        this.articleService   = articleService;
        this.allCategories    = referenceService.findAllCategories();
        this.libelleToId      = allCategories.stream().collect(
                Collectors.toMap(
                        ReferenceService.CategorieItem::nom,
                        ReferenceService.CategorieItem::id,
                        (existing, newer) -> existing
                ));
        buildLayout();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Layout initial
    // ─────────────────────────────────────────────────────────────────────────
    private void buildLayout() {
        setPadding(false);
        setSpacing(false);

        // ── Catégorie ─────────────────────────────────────────────────────────
        categorieSelect.setItems(
                allCategories.stream().map(ReferenceService.CategorieItem::nom).toList()
        );
        categorieSelect.setWidth("180px");
        categorieSelect.addValueChangeListener(e -> {
            Integer refCat = libelleToId.get(e.getValue());
            if (refCat == null) return;
            // ✅ Mettre à jour filtre directement
            filtre.refCategorie     = refCat;
            filtre.refSousCategorie = null;
            refreshSousCategories(refCat);
            currentPage = 1;
            reloadArticlesAvecFiltres();
        });

        // ── Sous-catégorie ────────────────────────────────────────────────────
        sousCategorieSelect.setItems("--------");
        sousCategorieSelect.setValue("--------");
        sousCategorieSelect.setWidth("160px");
        sousCategorieSelect.addValueChangeListener(e -> {
            if (sousCatChanging) return; // ignorer les changements programmatiques

            String nomSousCat = e.getValue();
            Integer refCat    = libelleToId.get(categorieSelect.getValue());
            if (refCat == null) return;

            if (nomSousCat == null || nomSousCat.equals("--------")) {
                // ✅ Recharger avec juste la catégorie, sans sous-catégorie
                filtre.refCategorie     = refCat;
                filtre.refSousCategorie = null;
                currentPage = 1;
                reloadArticlesAvecFiltres();
                return;
            }

            Integer refSousCat = referenceService.findSousCategoriesByCategorie(refCat)
                    .stream()
                    .filter(sc -> sc.nom().equals(nomSousCat))
                    .map(ReferenceService.SousCategorieItem::id)
                    .findFirst()
                    .orElse(null);

            // ✅ Mettre à jour filtre directement
            filtre.refCategorie     = refCat;
            filtre.refSousCategorie = refSousCat;
            currentPage = 1;
            reloadArticlesAvecFiltres();
        });

        HorizontalLayout selectsBar = new HorizontalLayout(categorieSelect, sousCategorieSelect);
        selectsBar.setAlignItems(Alignment.BASELINE);
        selectsBar.setSpacing(true);

        advSearch.getStyle()
                .set("background", "none")
                .set("border", "none")
                .set("color", "#6100C1")
                .set("text-decoration", "underline")
                .set("cursor", "pointer")
                .set("font-size", "0.85rem")
                .set("padding", "0");
        advSearch.addClickListener(e -> openRechercheAvancee());

        VerticalLayout topSection = new VerticalLayout(selectsBar);
        topSection.setPadding(false);
        topSection.setSpacing(false);
        topSection.setAlignItems(Alignment.START);

        add(topSection);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Rafraîchissement des sous-catégories — avec flag guard
    // ─────────────────────────────────────────────────────────────────────────
    private void refreshSousCategories(int refCategorie) {
        sousCatChanging = true;
        var items = referenceService.findSousCategoriesByCategorie(refCategorie)
                .stream()
                .map(ReferenceService.SousCategorieItem::nom)
                .toList();
        sousCategorieSelect.setItems(items.isEmpty() ? List.of("--------") : items);
        sousCategorieSelect.setValue("--------");
        sousCatChanging = false;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Rechargement des articles — filtre est la seule source de vérité
    // ─────────────────────────────────────────────────────────────────────────
    private void reloadArticlesAvecFiltres() {
        // ✅ Synchroniser uniquement les filtres simples (recherche avancée)
        // Ne JAMAIS écraser filtre.refCategorie et filtre.refSousCategorie ici
        filtre.nom          = filtreNom;
        filtre.marque       = filtreMarque;
        filtre.prixMin      = filtrePrixMin;
        filtre.prixMax      = filtrePrixMax;
        filtre.encheresOnly = filtreEncheresOnly;
        filtre.page         = currentPage;
        filtre.pageSize     = PAGE_SIZE;

        var articles = articleService.findByFiltresDynamiques(filtre);
        int total    = articleService.countByFiltresDynamiques(filtre);
        totalPages   = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        var topBar = getChildren().findFirst().orElse(null);
        removeAll();
        if (topBar != null) add(topBar);

        HorizontalLayout paginationTop = buildPagination(currentPage, totalPages);
        paginationTop.add(advSearch);
        paginationTop.setAlignItems(Alignment.CENTER);
        add(paginationTop);

        add(buildArticlesTable(articles));
        add(buildPagination(currentPage, totalPages));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // setParameter — navigation par URL
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public void setParameter(BeforeEvent event, Integer refCategorie) {
        String pageParam = event.getLocation().getQueryParameters()
                .getParameters()
                .getOrDefault("page", List.of("1"))
                .get(0);
        try { currentPage = Math.max(1, Integer.parseInt(pageParam)); }
        catch (NumberFormatException ex) { currentPage = 1; }

        // ✅ Lire le paramètre "nom" depuis l'URL
        String nomParam = event.getLocation().getQueryParameters()
                .getParameters()
                .getOrDefault("nom", List.of(""))
                .get(0);
        filtreNom = nomParam.isBlank() ? null : nomParam;

        // ✅ 0 = toutes catégories
        filtre.refCategorie     = (refCategorie == null || refCategorie == 0) ? null : refCategorie;
        filtre.refSousCategorie = null;

        if (refCategorie != null && refCategorie != 0) {
            allCategories.stream()
                    .filter(c -> c.id() == refCategorie)
                    .findFirst()
                    .ifPresent(cat -> {
                        categorieSelect.setValue(cat.nom());
                        refreshSousCategories(cat.id());
                    });
        }

        reloadArticlesAvecFiltres();
    }  // ─────────────────────────────────────────────────────────────────────────
    // Pagination
    // ─────────────────────────────────────────────────────────────────────────
    private HorizontalLayout buildPagination(int currentPage, int totalPages) {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(Alignment.CENTER);
        row.getStyle().set("padding", "12px 0").set("gap", "6px");

        String base = "/categorie/" + (filtre.refCategorie != null ? filtre.refCategorie : 0);

        row.add(paginationBtn("«", base + "?page=1",                         currentPage == 1));
        row.add(paginationBtn("‹", base + "?page=" + (currentPage - 1),      currentPage == 1));

        int start = Math.max(1, currentPage - 2);
        int end   = Math.min(totalPages, currentPage + 2);

        if (start > 1) {
            row.add(paginationBtn("1", base + "?page=1", false));
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
                String href = base + "?page=" + i;
                pageBtn.getElement().addEventListener("click", e ->
                        pageBtn.getUI().ifPresent(ui -> ui.navigate(href))
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
            row.add(paginationBtn(String.valueOf(totalPages), base + "?page=" + totalPages, false));
        }

        row.add(paginationBtn("›", base + "?page=" + (currentPage + 1), currentPage == totalPages));
        row.add(paginationBtn("»", base + "?page=" + totalPages,        currentPage == totalPages));

        Span info = new Span("Page " + currentPage + " / " + totalPages);
        info.getStyle()
                .set("font-size", "0.78rem").set("color", "#6100C1")
                .set("margin-left", "8px").set("align-self", "center");
        row.add(info);

        return row;
    }

    private Anchor paginationBtn(String label, String href, boolean disabled) {
        Anchor btn = new Anchor(disabled ? "#" : href, label);
        btn.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("width", "32px").set("height", "32px")
                .set("border-radius", "50%")
                .set("border", "1px solid " + (disabled ? "#DDD" : "#6100C1"))
                .set("color", disabled ? "#CCC" : "#6100C1")
                .set("font-size", "1rem")
                .set("text-decoration", "none")
                .set("pointer-events", disabled ? "none" : "auto")
                .set("font-weight", "bold");
        return btn;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Recherche avancée
    // ─────────────────────────────────────────────────────────────────────────
    private void openRechercheAvancee() {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");
        dialog.setHeaderTitle("Recherche avancée");

        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.setPadding(false);

        TextField nomField = new TextField("Nom de l'article");
        nomField.setWidthFull();
        nomField.setValue(filtreNom != null ? filtreNom : "");

        TextField marqueField = new TextField("Marque / Fabricant");
        marqueField.setWidthFull();
        marqueField.setValue(filtreMarque != null ? filtreMarque : "");

        Select<String> catSelect = new Select<>();
        catSelect.setLabel("Catégorie");
        catSelect.setItems(allCategories.stream().map(ReferenceService.CategorieItem::nom).toList());
        catSelect.setWidthFull();

        Select<String> sousCatSelect = new Select<>();
        sousCatSelect.setLabel("Sous-catégorie");
        sousCatSelect.setItems("--------");
        sousCatSelect.setValue("--------");
        sousCatSelect.setWidthFull();

        Div dynamicFields = new Div();
        dynamicFields.setWidthFull();

        catSelect.addValueChangeListener(e -> {
            Integer refCat = libelleToId.get(e.getValue());
            if (refCat != null) {
                var items = referenceService.findSousCategoriesByCategorie(refCat)
                        .stream().map(ReferenceService.SousCategorieItem::nom).toList();
                sousCatSelect.setItems(items.isEmpty() ? List.of("--------") : items);
                sousCatSelect.setValue("--------");
                refreshDynamicSearchFields(dynamicFields, refCat, null);
            }
        });

        sousCatSelect.addValueChangeListener(e -> {
            Integer refCat = libelleToId.get(catSelect.getValue());
            if (refCat != null && e.getValue() != null && !e.getValue().equals("--------")) {
                Integer refSousCat = referenceService.findSousCategoriesByCategorie(refCat)
                        .stream()
                        .filter(sc -> sc.nom().equals(e.getValue()))
                        .map(ReferenceService.SousCategorieItem::id)
                        .findFirst()
                        .orElse(null);
                refreshDynamicSearchFields(dynamicFields, refCat, refSousCat);
            }
        });

        HorizontalLayout prixRow = new HorizontalLayout();
        TextField prixMin = new TextField("Prix min (CHF)");
        TextField prixMax = new TextField("Prix max (CHF)");
        prixMin.setWidth("48%");
        prixMax.setWidth("48%");
        prixMin.setValue(filtrePrixMin != null ? String.valueOf(filtrePrixMin) : "");
        prixMax.setValue(filtrePrixMax != null ? String.valueOf(filtrePrixMax) : "");
        prixRow.add(prixMin, prixMax);
        prixRow.setWidthFull();

        com.vaadin.flow.component.checkbox.Checkbox encheresOnly =
                new com.vaadin.flow.component.checkbox.Checkbox("Enchères seulement");
        encheresOnly.setValue(filtreEncheresOnly);

        content.add(nomField, marqueField, catSelect, sousCatSelect, dynamicFields, prixRow, encheresOnly);

        Button rechercher = new Button("Rechercher", e -> {
            filtreNom          = nomField.getValue().isBlank()   ? null : nomField.getValue();
            filtreMarque       = marqueField.getValue().isBlank() ? null : marqueField.getValue();
            filtreEncheresOnly = encheresOnly.getValue();

            // ✅ Mettre à jour filtre directement
            Integer refCat = libelleToId.get(catSelect.getValue());
            filtre.refCategorie = refCat;

            String nomSousCat = sousCatSelect.getValue();
            if (refCat != null && nomSousCat != null && !nomSousCat.equals("--------")) {
                filtre.refSousCategorie = referenceService.findSousCategoriesByCategorie(refCat)
                        .stream()
                        .filter(sc -> sc.nom().equals(nomSousCat))
                        .map(ReferenceService.SousCategorieItem::id)
                        .findFirst()
                        .orElse(null);
            } else {
                filtre.refSousCategorie = null;
            }

            try { filtrePrixMin = prixMin.getValue().isBlank() ? null : Double.parseDouble(prixMin.getValue()); }
            catch (NumberFormatException ex) { filtrePrixMin = null; }
            try { filtrePrixMax = prixMax.getValue().isBlank() ? null : Double.parseDouble(prixMax.getValue()); }
            catch (NumberFormatException ex) { filtrePrixMax = null; }

            collectDynamicSearchFilters(dynamicFields);
            currentPage = 1;
            reloadArticlesAvecFiltres();
            dialog.close();
        });

        Button reinitialiser = new Button("Réinitialiser", e -> {
            filtreNom               = null;
            filtreMarque            = null;
            filtrePrixMin           = null;
            filtrePrixMax           = null;
            filtreEncheresOnly      = false;
            filtre.refSousCategorie = null;
            currentPage             = 1;
            reloadArticlesAvecFiltres();
            dialog.close();
        });

        Button annuler = new Button("Annuler", e -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(rechercher, reinitialiser, annuler);
        buttons.setSpacing(true);
        dialog.add(content, buttons);
        dialog.open();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Champs dynamiques selon catégorie
    // ─────────────────────────────────────────────────────────────────────────
    private void refreshDynamicSearchFields(Div container, int catId, Integer subcatId) {
        container.removeAll();

        ArticleCategory cat = ArticleCategory.fromId(catId);
        com.vaadin.flow.component.formlayout.FormLayout form =
                new com.vaadin.flow.component.formlayout.FormLayout();
        form.setResponsiveSteps(
                new com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep("0", 1),
                new com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep("400px", 2)
        );

        if (cat == ArticleCategory.VOITURE || cat == ArticleCategory.MOTO) {
            form.add(taggedField("search_nb_km",       new TextField("Kilométrage max")));
            form.add(taggedField("search_annee_min",   new TextField("Année min")));
            form.add(taggedField("search_annee_max",   new TextField("Année max")));
            form.add(taggedField("search_nb_cheveaux", new TextField("Chevaux min (CV)")));

            Select<String> carburantSelect = new Select<>();
            carburantSelect.setLabel("Carburant");
            carburantSelect.setId("search_essence");
            List<String> fuels = referenceService.findAllFuelTypes()
                    .stream().map(ReferenceService.ReferenceItem::nom).toList();
            carburantSelect.setItems(fuels.isEmpty() ? List.of("--------") : fuels);
            form.add(carburantSelect);

            Select<String> boiteSelect = new Select<>();
            boiteSelect.setLabel("Boîte de vitesse");
            boiteSelect.setId("search_boite");
            List<String> boites = referenceService.findAllGearboxTypes()
                    .stream().map(ReferenceService.ReferenceItem::nom).toList();
            boiteSelect.setItems(boites.isEmpty() ? List.of("--------") : boites);
            form.add(boiteSelect);
        } else if (cat == ArticleCategory.IMMOBILIER) {
            form.add(taggedField("search_nb_piece_min", new TextField("Nb pièces min")));
            form.add(taggedField("search_surface_min",  new TextField("Surface min (m²)")));
            form.add(taggedField("search_surface_max",  new TextField("Surface max (m²)")));
            form.add(taggedField("search_lieu",         new TextField("Ville / Lieu")));
            form.add(taggedField("search_npa",          new TextField("Code postal")));
        } else if (cat == ArticleCategory.VINS) {
            form.add(taggedField("search_millesime_min", new TextField("Millésime min")));
            form.add(taggedField("search_millesime_max", new TextField("Millésime max")));

            Select<String> typeVinSelect = new Select<>();
            typeVinSelect.setLabel("Type de vin");
            typeVinSelect.setId("search_type_vin");
            List<String> types = referenceService.findAllWineTypes()
                    .stream().map(ReferenceService.ReferenceItem::nom).toList();
            typeVinSelect.setItems(types.isEmpty() ? List.of("--------") : types);
            form.add(typeVinSelect);
        } else if (cat == ArticleCategory.DVD) {
            form.add(taggedField("search_realisateur", new TextField("Réalisateur")));
            form.add(taggedField("search_acteurs",     new TextField("Acteur(s)")));
            form.add(taggedField("search_annee_min",   new TextField("Année min")));
            form.add(taggedField("search_annee_max",   new TextField("Année max")));
        } else if (cat == ArticleCategory.LIVRES) {
            form.add(taggedField("search_auteur",    new TextField("Auteur")));
            form.add(taggedField("search_editeur",   new TextField("Éditeur")));
            form.add(taggedField("search_annee_min", new TextField("Année min")));
        } else if (cat == ArticleCategory.JEUX) {
            Select<String> typeJeuxSelect = new Select<>();
            typeJeuxSelect.setLabel("Type de jeu");
            typeJeuxSelect.setId("search_type_jeux");
            List<String> types = referenceService.findAllGameTypes()
                    .stream().map(ReferenceService.ReferenceItem::nom).toList();
            typeJeuxSelect.setItems(types.isEmpty() ? List.of("--------") : types);
            form.add(typeJeuxSelect);
        }

        if (subcatId != null) {
            if (subcatId == ArticleSubCategory.TV_ECRAN_PLAT_ID) {
                form.add(taggedField("search_dimension_min", new TextField("Pouces min")));
                form.add(taggedField("search_dimension_max", new TextField("Pouces max")));

                Select<String> ecranSelect = new Select<>();
                ecranSelect.setLabel("Type d'écran");
                ecranSelect.setId("search_type_ecran");
                List<String> types = referenceService.findAllScreenTypes()
                        .stream().map(ReferenceService.ReferenceItem::nom).toList();
                ecranSelect.setItems(types.isEmpty() ? List.of("--------") : types);
                form.add(ecranSelect);
            }
            if (ArticleSubCategory.INFO_PC_SUBCATS.contains(subcatId)) {
                form.add(taggedField("search_ram",        new TextField("RAM (Go)")));
                form.add(taggedField("search_processeur", new TextField("Processeur min (GHz)")));
                form.add(taggedField("search_disque_dur", new TextField("Disque dur min (Go)")));
            }
            if (ArticleSubCategory.WEAR_SIZE_SUBCATS.contains(subcatId)) {
                form.add(taggedField("search_taille", new TextField("Taille")));
            }
        }

        if (form.getChildren().findAny().isPresent()) {
            container.add(form);
        }
    }

    private <T extends com.vaadin.flow.component.Component> T taggedField(String id, T field) {
        field.setId(id);
        return field;
    }

    private void collectDynamicSearchFilters(Div container) {
        container.getChildren().forEach(c -> {
            if (!(c instanceof com.vaadin.flow.component.formlayout.FormLayout form)) return;
            form.getChildren().forEach(field -> {
                if (field instanceof TextField tf && tf.getId().isPresent()) {
                    String val = tf.getValue();
                    switch (tf.getId().get()) {
                        case "search_nb_km"         -> filtre.nbKmMax       = parseIntOrNull(val);
                        case "search_annee_min"     -> filtre.anneeMin      = parseIntOrNull(val);
                        case "search_annee_max"     -> filtre.anneeMax      = parseIntOrNull(val);
                        case "search_nb_cheveaux"   -> filtre.nbCheveauxMin = parseIntOrNull(val);
                        case "search_nb_piece_min"  -> filtre.nbPieceMin    = parseIntOrNull(val);
                        case "search_surface_min"   -> filtre.surfaceMin    = parseIntOrNull(val);
                        case "search_surface_max"   -> filtre.surfaceMax    = parseIntOrNull(val);
                        case "search_lieu"          -> filtre.lieu          = val.isBlank() ? null : val;
                        case "search_npa"           -> filtre.npa           = val.isBlank() ? null : val;
                        case "search_millesime_min" -> filtre.millesimeMin  = parseIntOrNull(val);
                        case "search_millesime_max" -> filtre.millesimeMax  = parseIntOrNull(val);
                        case "search_realisateur"   -> filtre.realisateur   = val.isBlank() ? null : val;
                        case "search_acteurs"       -> filtre.acteurs       = val.isBlank() ? null : val;
                        case "search_auteur"        -> filtre.auteur        = val.isBlank() ? null : val;
                        case "search_editeur"       -> filtre.editeur       = val.isBlank() ? null : val;
                        case "search_ram"           -> filtre.ram           = val.isBlank() ? null : val;
                        case "search_processeur"    -> filtre.processeurMin = val.isBlank() ? null : val;
                        case "search_taille"        -> filtre.taille        = val.isBlank() ? null : val;
                        case "search_dimension_min" -> filtre.dimensionMin  = parseIntOrNull(val);
                        case "search_dimension_max" -> filtre.dimensionMax  = parseIntOrNull(val);
                    }
                }
                if (field instanceof Select<?> sel && sel.getId().isPresent()) {
                    switch (sel.getId().get()) {
                        case "search_essence"    -> filtre.refEssence        = resolveRefId(sel, referenceService.findAllFuelTypes());
                        case "search_boite"      -> filtre.refBoiteDeVitesse = resolveRefId(sel, referenceService.findAllGearboxTypes());
                        case "search_type_vin"   -> filtre.refTypeDeVin      = resolveRefId(sel, referenceService.findAllWineTypes());
                        case "search_type_jeux"  -> filtre.refTypeDeJeux     = resolveRefId(sel, referenceService.findAllGameTypes());
                        case "search_type_ecran" -> filtre.refTypeEcran      = resolveRefId(sel, referenceService.findAllScreenTypes());
                    }
                }
            });
        });
    }

    private Integer parseIntOrNull(String val) {
        if (val == null || val.isBlank()) return null;
        try { return Integer.parseInt(val.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    @SuppressWarnings("unchecked")
    private Integer resolveRefId(Select<?> sel, List<ReferenceService.ReferenceItem> items) {
        Object val = sel.getValue();
        if (val == null || "--------".equals(val)) return null;
        return items.stream()
                .filter(i -> i.nom().equals(val))
                .map(ReferenceService.ReferenceItem::id)
                .findFirst()
                .orElse(null);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Table articles
    // ─────────────────────────────────────────────────────────────────────────
    private Div buildArticlesTable(List<Article> articles) {
        Div table = new Div();
        table.add(buildTableHeader());
        table.getStyle()
                .set("border-radius", "20px")
                .set("border", "1px solid #6100C1")
                .set("text-decoration", "none")
                .set("overflow", "hidden");

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
        header.getStyle()
                .set("font-weight", "bold")
                .set("color", "#0000CC")
                .set("padding", "4px 8px")
                .set("width", "100%")
                .set("border-bottom", "2px solid #6600CC");
        header.add(
                cell("Image",            "80px"),
                cell("Nom de l'article", "210px"),
                cell("Marque",           "165px"),
                cell("Prix en CHF",      "130px")
        );
        return header;
    }

    private HorizontalLayout buildArticleRow(Article art) {
        HorizontalLayout row = new HorizontalLayout();
        row.setAlignItems(FlexComponent.Alignment.CENTER);
        row.getStyle()
                .set("padding", "6px 0")
                .set("border-bottom", "1px solid #EEE")
                .set("text-decoration", "none")
                .set("cursor", "pointer");

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

        String nom = art.getNom() != null ? art.getNom() : "(sans nom)";
        Anchor nomLink = new Anchor("/detail/" + art.getIdArticle(), nom);
        nomLink.getStyle()
                .set("color", "#0000CC").set("width", "210px")
                .set("font-size", "0.9rem")
                .set("text-decoration", "none")
                .set("white-space", "nowrap")

                .set("overflow", "hidden");

        Span marqueSpan = new Span(art.getMarque() != null ? art.getMarque() : "—");
        marqueSpan.getStyle().set("color", "#0000CC").set("width", "165px").set("font-size", "0.85rem");

        Span prixSpan = new Span(art.getPrix() != null ? String.format("%.2f CHF", art.getPrix()) : "—");
        prixSpan.getStyle().set("width", "130px").set("font-size", "0.85rem");

        row.add(imgBox, nomLink, marqueSpan, prixSpan);

        Long id = art.getIdArticle();
        row.getElement().addEventListener("click", e ->
                row.getUI().ifPresent(ui -> ui.navigate("detail/" + id))
        );
        return row;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────
    private Span cell(String text, String width) {
        Span s = new Span(text);
        s.getStyle().set("width", width).set("font-size", "0.9rem");
        return s;
    }

    public record ArticleDto(String nom, String fabricant, double prixChf) {}
}
