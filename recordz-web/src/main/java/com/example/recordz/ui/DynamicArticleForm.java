package com.example.recordz.ui;

import com.example.recordz.model.domain.ArticleCategory;
import com.example.recordz.model.domain.ArticleSubCategory;
import com.example.recordz.model.domain.dto.ArticleFormData;
import com.example.recordz.service.ArticleDynamicDataService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class DynamicArticleForm extends VerticalLayout {

    /**
     * ✅ Catégories dont TOUTES les sous-catégories affichent le champ "Taille"
     * (habits femmes, hommes, enfants + lingerie, comme dans DetailArticleView).
     *
     * On compare sur le NOM de l'enum plutôt que sur la constante, pour que cette classe
     * compile même si FASHION_ENFANT n'existe pas encore dans ArticleCategory :
     * dans ce cas, ajoute la constante dans l'enum (avec son id de base) et le champ apparaît.
     */
    private static final Set<String> CATEGORIES_AVEC_TAILLE = Set.of(
            "FASHION_FEMME", "FASHION_HOMME", "FASHION_ENFANT", "LINGERIE");

    // ---- Services ----
    private final ArticleDynamicDataService dataService;
    private final String lang;

    // ---- Sélecteurs principaux ----
    private final ComboBox<ArticleDynamicDataService.LabelValue> categorySelect;
    private final ComboBox<ArticleDynamicDataService.LabelValue> subCategorySelect;

    // ---- Zone des champs dynamiques ----
    private final Div dynamicFieldsContainer;

    // ---- Champs communs ----
    private final TextField    nameField        = new TextField(getTranslation("artform.name"));
    private final TextArea     descriptionField = new TextArea(getTranslation("artform.description"));
    private final NumberField  priceField       = new NumberField(getTranslation("artform.price"));
    private final Select<ArticleDynamicDataService.LabelValue> paymentSelect  = new Select<>();
    private final Select<ArticleDynamicDataService.LabelValue> deliverySelect = new Select<>();
    private final Select<ArticleDynamicDataService.LabelValue> conditionSelect = new Select<>();
    private final IntegerField quantityField    = new IntegerField(getTranslation("artform.quantity"));

    // ---- Upload image ----
    private final MemoryBuffer uploadBuffer = new MemoryBuffer();
    private String uploadedFileName    = null;
    private String uploadedContentType = null;

    // ---- Enchère ----
    private final Select<ArticleDynamicDataService.LabelValue> auctionSelect = new Select<>();
    private final Div auctionDetailsContainer = new Div();
    private TextField auctionStartField;
    private Select<Integer> auctionDurationSelect;
    private TextField auctionEndField;

    // ---- Notifie le parent (ArticleFormView) des changements de catégorie —
    // utilisé notamment pour afficher/masquer le panneau de capture vidéo
    // selon que la catégorie sélectionnée en nécessite une ou non.
    private Consumer<Integer> onCategoryChanged;

    // ---- Champs dynamiques (références gardées pour collecte) ----
    // Voiture/Moto
    private NumberField carHorseField, carCylinderField, carKmField;
    private TextField   carYearFabField, carYearServiceField;
    private Select<ArticleDynamicDataService.LabelValue> carFuelSelect, carGearboxSelect;
    private Checkbox    carClimaCheck;
    // Livres
    private TextField   bookAuthorField, bookEditorField;
    private IntegerField bookYearField;
    // Immo
    private Select<ArticleDynamicDataService.LabelValue> immoTypeSelect, immoCountrySelect, immoRegionSelect;
    private String      immoRegionKind;   // "CANTON", "DEPARTEMENT" ou null
    private TextField   immoCityField, immoAddressField, immoPostalField, immoBuildYearField, immoTerrainField;
    private IntegerField immoRoomsField, immoSurfaceField;
    // Vins
    private ComboBox<ArticleDynamicDataService.LabelValue> wineCountrySelect, wineRegionSelect;
    private TextField  wineMillesimeField;
    private ComboBox<ArticleDynamicDataService.LabelValue> wineCepageSelect;
    private Select<ArticleDynamicDataService.LabelValue> wineTypeSelect;
    // Jeux
    private Select<ArticleDynamicDataService.LabelValue> gameTypeSelect;
    // DVD$
    private TextField dvdActorsField, dvdDirectorField, dvdDurationField;
    private IntegerField dvdYearField;
    // TV
    private Select<ArticleDynamicDataService.LabelValue> tvScreenTypeSelect;
    private TextField tvDimensionField;
    // PC
    private NumberField pcProcessorField;
    private TextField pcRamField, pcHddField;
    // Vêtements
    private TextField clothingSizeField;
    // Fabricant
    private TextField fabricantField;

    /** Langue de l'interface (fr / en / de) pour charger les listes depuis la base. */
    private static String currentLang() {
        UI ui = UI.getCurrent();
        String l = ui != null ? ui.getLocale().getLanguage() : "fr";
        return switch (l) {
            case "en" -> "en";
            case "de" -> "de";
            default   -> "fr";
        };
    }

    /** Utilise automatiquement la langue sélectionnée dans l'interface. */
    public DynamicArticleForm(ArticleDynamicDataService dataService) {
        this(dataService, currentLang());
    }

    public DynamicArticleForm(ArticleDynamicDataService dataService, String lang) {
        this.dataService = dataService;
        this.lang = lang != null ? lang : currentLang();

        setSpacing(true);
        setPadding(true);

        categorySelect    = buildCategorySelect();
        subCategorySelect = buildSubCategorySelect();

        dynamicFieldsContainer = new Div();
        dynamicFieldsContainer.setId("dynamic-fields");
        dynamicFieldsContainer.getStyle().set("width", "100%");

        add(
                new H3(getTranslation("artform.title")),
                buildCommonForm(),
                categorySelect,
                subCategorySelect,
                dynamicFieldsContainer,
                buildAuctionSection()
        );

        categorySelect.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                int catId = Integer.parseInt(e.getValue().value());
                refreshSubCategories(catId);
                refreshDynamicFields(catId, -1);
                if (onCategoryChanged != null) onCategoryChanged.accept(catId);
            } else {
                // ✅ Aucune catégorie : on vide aussi les champs dynamiques et la sous-catégorie
                dynamicFieldsContainer.removeAll();
                clearDynamicRefs();
                subCategorySelect.clear();
                subCategorySelect.setVisible(false);
                if (onCategoryChanged != null) onCategoryChanged.accept(null);
            }
        });
        subCategorySelect.addValueChangeListener(e -> {
            if (e.getValue() != null && categorySelect.getValue() != null) {
                refreshDynamicFields(
                        Integer.parseInt(categorySelect.getValue().value()),
                        Integer.parseInt(e.getValue().value())
                );
            }
        });

        auctionSelect.addValueChangeListener(e -> {
            boolean isAuction = e.getValue() != null && "yes".equals(e.getValue().value());
            auctionDetailsContainer.setVisible(isAuction);
            quantityField.setVisible(!isAuction);
        });
    }

    /**
     * Enregistre un callback appelé à chaque changement de catégorie, avec
     * l'id de la catégorie sélectionnée (ou null si aucune). Utilisé par
     * ArticleFormView pour afficher le panneau de capture vidéo uniquement
     * sur les catégories qui le nécessitent.
     */
    public void setOnCategoryChanged(Consumer<Integer> listener) {
        this.onCategoryChanged = listener;
    }

    // =========================================================================
    // COLLECTE DES VALEURS — appelée depuis ArticleFormView au clic "Publier"
    // =========================================================================

    public ArticleFormData collectValues() {
        ArticleFormData d = new ArticleFormData();

        // ---- Communs ----
        d.nom   = nameField.getValue();
        d.label = descriptionField.getValue();
        d.prix  = priceField.getValue() != null ? priceField.getValue() : 0.0;
        d.refConditionPayement = selectedId(paymentSelect);
        d.refModeDelivraison   = selectedId(deliverySelect);
        d.refEtat              = selectedId(conditionSelect);
        d.quantite  = quantityField.getValue();
        d.pochette            = uploadedFileName    != null ? uploadedFileName    : "";
        d.pochetteStream      = uploadedFileName    != null ? uploadBuffer.getInputStream() : null;
        d.pochetteContentType = uploadedContentType != null ? uploadedContentType : "image/jpeg";

        // ---- Catégorie ----
        if (categorySelect.getValue() != null)
            d.refCategorie = Integer.parseInt(categorySelect.getValue().value());
        if (subCategorySelect.getValue() != null)
            d.refSubcategorie = Integer.parseInt(subCategorySelect.getValue().value());

        // ---- Enchère ----
        d.enchere = auctionSelect.getValue() != null && "yes".equals(auctionSelect.getValue().value());
        if (d.enchere) {
            String today = java.time.LocalDate.now().toString();
            String timeNow = java.time.LocalTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));

            // ✅ Date début = aujourd'hui + heure actuelle
            d.enchereDateDebut = today + " " + timeNow;

            // ✅ Date fin = date choisie + heure actuelle (pour que le scheduler match exactement)
            d.enchereDateFin = auctionEndField != null && !auctionEndField.getValue().isBlank()
                    ? auctionEndField.getValue() + " " + timeNow
                    : null;
        }
        // ---- Fabricant ----
        if (fabricantField != null) d.marque = fabricantField.getValue();

        // ---- Voiture / Moto ----
        if (carHorseField    != null) d.nbCheveaux = carHorseField.getValue()    != null ? carHorseField.getValue().intValue()    : null;
        if (carCylinderField != null) d.nbCylindre = carCylinderField.getValue() != null ? carCylinderField.getValue().intValue() : null;
        if (carKmField       != null) d.nbKm       = carKmField.getValue()       != null ? carKmField.getValue().intValue()       : null;
        if (carYearFabField    != null) d.annee                  = carYearFabField.getValue();
        if (carYearServiceField != null) d.premiereImmatriculation = carYearServiceField.getValue();
        if (carFuelSelect    != null) d.essenceOuDiesel    = selectedId(carFuelSelect);
        if (carGearboxSelect != null) d.refBoiteDeVitesse  = selectedId(carGearboxSelect);
        if (carClimaCheck    != null) d.clima               = carClimaCheck.getValue();

        // ---- Livres ----
        if (bookAuthorField != null) d.auteur = bookAuthorField.getValue();
        if (bookEditorField != null) d.marque = bookEditorField.getValue(); // éditeur → marque
        if (bookYearField   != null) d.annee  = bookYearField.getValue() != null ? bookYearField.getValue().toString() : null;

        // ---- Immobilier ----
        if (immoTypeSelect    != null) d.refLocationOuAchat = selectedId(immoTypeSelect);
        if (immoCountrySelect != null) d.refPays            = selectedId(immoCountrySelect);
        if (immoRegionSelect  != null) {
            // Canton ou Département selon le select visible
            // (basé sur immoRegionKind et non sur le libellé, qui est maintenant traduit)
            if ("CANTON".equals(immoRegionKind))              d.refCanton      = selectedId(immoRegionSelect);
            else if ("DEPARTEMENT".equals(immoRegionKind))    d.refDepartement = selectedId(immoRegionSelect);
        }
        if (immoCityField     != null) d.lieu               = immoCityField.getValue();
        if (immoAddressField  != null) d.adresse            = immoAddressField.getValue();
        if (immoPostalField   != null) d.npa                = immoPostalField.getValue();
        if (immoRoomsField    != null) d.nbPiece            = immoRoomsField.getValue();
        if (immoSurfaceField  != null) d.surfaceHabitable   = immoSurfaceField.getValue();
        if (immoTerrainField  != null) d.superficieTerrain  = immoTerrainField.getValue();
        if (immoBuildYearField != null) d.anneeConstruction = immoBuildYearField.getValue();

        // ---- Vins ----
        if (wineCountrySelect != null) d.refPaysRegionVin = selectedId(wineCountrySelect);
        if (wineRegionSelect  != null && wineRegionSelect.isVisible()) d.refPaysRegionVin = selectedId(wineRegionSelect);
        if (wineCepageSelect  != null) d.refCepage = selectedId(wineCepageSelect);
        if (wineMillesimeField != null) d.millesime = wineMillesimeField.getValue();
        if (wineTypeSelect    != null) d.refTypeDeVin     = selectedId(wineTypeSelect);


        // ---- Jeux ----
        if (gameTypeSelect != null) d.refTypeDeJeux = selectedId(gameTypeSelect);

        // ---- DVD ----
        if (dvdActorsField   != null) d.acteurs     = dvdActorsField.getValue();
        if (dvdDirectorField != null) d.realisateur = dvdDirectorField.getValue();
        if (dvdDurationField != null) d.duree       = dvdDurationField.getValue();
        if (dvdYearField     != null) d.annee       = dvdYearField.getValue() != null ? dvdYearField.getValue().toString() : null;

        // ---- TV ----
        if (tvScreenTypeSelect != null) d.refTypeEcran = selectedId(tvScreenTypeSelect);
        // ✅ CORRECTION : Integer.parseInt("") (champ vide) ou texte non numérique levait une exception
        if (tvDimensionField   != null) d.dimension    = parseIntOrNull(tvDimensionField.getValue());

        // ---- PC ----
        if (pcProcessorField != null) d.processeur = pcProcessorField.getValue();
        if (pcRamField       != null) d.ram        = pcRamField.getValue();
        if (pcHddField       != null) d.disqueDur  = pcHddField.getValue();

        // ---- Vêtements ----
        if (clothingSizeField != null) d.taille = clothingSizeField.getValue();

        return d;
    }

    // =========================================================================
    // VALIDATION MINIMALE
    // =========================================================================

    public boolean isValid() {
        boolean ok = true;
        if (nameField.getValue() == null || nameField.getValue().isBlank()) {
            nameField.setErrorMessage(getTranslation("artform.err.name"));
            nameField.setInvalid(true);
            ok = false;
        } else {
            nameField.setInvalid(false);
        }
        if (categorySelect.getValue() == null) {
            categorySelect.setErrorMessage(getTranslation("artform.err.category"));
            categorySelect.setInvalid(true);
            ok = false;
        } else {
            categorySelect.setInvalid(false);
        }
        if (priceField.getValue() == null || priceField.getValue() < 0) {
            priceField.setErrorMessage(getTranslation("artform.err.price"));
            priceField.setInvalid(true);
            ok = false;
        } else {
            priceField.setInvalid(false);
        }
        return ok;
    }

    // =========================================================================
    // FORMULAIRE COMMUN
    // =========================================================================

    private FormLayout buildCommonForm() {
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2)
        );

        nameField.setRequired(true);
        nameField.setPlaceholder(getTranslation("artform.name"));
        descriptionField.setMinHeight("80px");
        priceField.setMin(0);
        priceField.setSuffixComponent(new Div());

        paymentSelect.setLabel(getTranslation("artform.payment"));
        paymentSelect.setItems(dataService.loadPaymentConditions(lang));
        paymentSelect.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);

        deliverySelect.setLabel(getTranslation("artform.delivery"));
        deliverySelect.setItems(dataService.loadDeliveryConditions(lang));
        deliverySelect.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);

        conditionSelect.setLabel(getTranslation("artform.condition"));
        conditionSelect.setItems(dataService.loadConditionOptions(lang));
        conditionSelect.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);

        quantityField.setMin(1);
        quantityField.setValue(1);
        quantityField.setStepButtonsVisible(true);

        Upload upload = new Upload(uploadBuffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif", "image/webp");
        upload.setMaxFileSize(1024 * 1024);
        upload.addSucceededListener(event -> {
            uploadedFileName    = event.getFileName();
            uploadedContentType = event.getMIMEType();
        });

        Span imageNote = new Span(getTranslation("artform.imageNote"));
        imageNote.getStyle().set("font-size", "0.8rem").set("color", "#666");
        Span imageLbl = new Span(getTranslation("artform.image"));
        imageLbl.getStyle().set("width", "165px").set("font-size", "0.85rem").set("color", "#0000CC");
        HorizontalLayout imageRow = new HorizontalLayout(imageLbl, upload, imageNote);
        imageRow.setAlignItems(Alignment.BASELINE);

        form.add(nameField, 2);
        form.add(descriptionField, 2);
        form.add(priceField, paymentSelect, deliverySelect, conditionSelect, quantityField);
        form.add(imageRow, 2);

        return form;
    }

    // =========================================================================
    // SECTION ENCHÈRE
    // =========================================================================

    private Component buildAuctionSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(false);

        auctionSelect.setLabel(getTranslation("artform.auction"));
        auctionSelect.setItems(dataService.loadAuctionOptions(lang));
        auctionSelect.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);

        FormLayout auctionForm = new FormLayout();

        auctionStartField = new TextField(getTranslation("artform.auctionStart"));
        auctionStartField.setReadOnly(true);
        auctionStartField.setValue(java.time.LocalDate.now().toString());

        auctionDurationSelect = new Select<>();
        auctionDurationSelect.setLabel(getTranslation("artform.auctionDuration"));
        auctionDurationSelect.setItems(
                java.util.stream.IntStream.rangeClosed(1, 30).boxed().toList()
        );

        auctionEndField = new TextField(getTranslation("artform.auctionEnd"));
        auctionEndField.setReadOnly(true);

        auctionDurationSelect.addValueChangeListener(e -> {
            if (e.getValue() != null)
                auctionEndField.setValue(java.time.LocalDate.now().plusDays(e.getValue()).toString());
        });

        auctionForm.add(auctionStartField, auctionDurationSelect, auctionEndField);
        auctionDetailsContainer.add(auctionForm);
        auctionDetailsContainer.setVisible(false);

        section.add(auctionSelect, auctionDetailsContainer);
        return section;
    }

    // =========================================================================
    // SÉLECTEURS CATÉGORIE / SOUS-CATÉGORIE
    // =========================================================================

    private ComboBox<ArticleDynamicDataService.LabelValue> buildCategorySelect() {
        ComboBox<ArticleDynamicDataService.LabelValue> select = new ComboBox<>(getTranslation("artform.category"));
        select.setItems(dataService.loadCategories(lang));
        select.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);
        select.setWidth("100%");
        select.setRequired(true);
        return select;
    }

    private ComboBox<ArticleDynamicDataService.LabelValue> buildSubCategorySelect() {
        ComboBox<ArticleDynamicDataService.LabelValue> select = new ComboBox<>(getTranslation("artform.subcategory"));
        select.setWidth("100%");
        select.setVisible(false);
        return select;
    }

    private void refreshSubCategories(int categoryId) {
        List<ArticleDynamicDataService.LabelValue> subs =
                dataService.loadSubCategories(categoryId, lang);
        subCategorySelect.setItems(subs);
        subCategorySelect.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);
        subCategorySelect.setVisible(!subs.isEmpty());
        subCategorySelect.clear();
    }
    // =========================================================================
    // CHAMPS DYNAMIQUES
    // =========================================================================

    /**
     * @param subcatId id de la sous-catégorie, ou -1 si seule la catégorie vient d'être choisie.
     */
    private void refreshDynamicFields(int catId, int subcatId) {
        // ✅ Quand seule la sous-catégorie change, on conserve ce que l'utilisateur
        // a déjà saisi dans "fabricant" et "taille" (avant, ces champs étaient vidés).
        String previousMaker = null;
        String previousSize  = null;
        if (subcatId != -1) {
            previousMaker = fabricantField   != null ? fabricantField.getValue()   : null;
            previousSize  = clothingSizeField != null ? clothingSizeField.getValue() : null;
        }

        dynamicFieldsContainer.removeAll();
        clearDynamicRefs();

        ArticleCategory cat = ArticleCategory.fromId(catId);
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2)
        );

        if (cat != ArticleCategory.LINGERIE && cat != ArticleCategory.IMMOBILIER
                && cat != ArticleCategory.LIVRES && cat != ArticleCategory.DVD) {
            fabricantField = new TextField(getTranslation("artform.maker"));
            fabricantField.setRequired(true);
            if (previousMaker != null) fabricantField.setValue(previousMaker);
            form.add(fabricantField);
        }

        if (cat == ArticleCategory.VOITURE || cat == ArticleCategory.MOTO) addCarFields(form);
        if (cat == ArticleCategory.LIVRES)     addBookFields(form);
        if (cat == ArticleCategory.IMMOBILIER) addImmobilierFields(form);
        if (cat == ArticleCategory.VINS)       addVinFields(form);

        if (cat == ArticleCategory.JEUX) {
            gameTypeSelect = new Select<>();
            gameTypeSelect.setLabel(getTranslation("artform.gameType"));
            gameTypeSelect.setItems(dataService.loadGameTypes(lang));
            gameTypeSelect.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);
            form.add(gameTypeSelect, 2);
        }

        if (cat == ArticleCategory.DVD) addDvdFields(form);

        if (subcatId == ArticleSubCategory.TV_ECRAN_PLAT_ID) addTvFields(form);
        if (ArticleSubCategory.INFO_PC_SUBCATS.contains(subcatId)) addPcFields(form);

        // ✅ CORRECTION : le champ "Taille" dépend maintenant de la CATÉGORIE (habits femmes,
        // hommes, enfants, lingerie) et non plus d'une liste d'ids de sous-catégories.
        // Il s'affiche donc dès le choix de la catégorie, pour toutes ses sous-catégories,
        // et n'est plus exposé par erreur à des ids de sous-catégories qui existent aussi
        // dans d'autres catégories (ex. les ids 1 à 4 des vins).
        if (showsSizeField(cat)) {
            clothingSizeField = new TextField(getTranslation("artform.size"));
            if (previousSize != null) clothingSizeField.setValue(previousSize);
            form.add(clothingSizeField);
        }

        if (form.getChildren().findAny().isPresent())
            dynamicFieldsContainer.add(new H3(categorySelect.getValue() != null ? categorySelect.getValue().label() : cat.getLabel()), form);
    }

    /** Vrai si la catégorie est une catégorie "habits" (toutes sous-catégories confondues). */
    private boolean showsSizeField(ArticleCategory cat) {
        return cat != null && CATEGORIES_AVEC_TAILLE.contains(cat.name());
    }

    // =========================================================================
    // BUILDERS DE SECTIONS DYNAMIQUES
    // =========================================================================

    private void addCarFields(FormLayout form) {
        form.add(new H3(getTranslation("artform.car.title")), 2);
        carHorseField    = new NumberField(getTranslation("artform.car.hp"));
        carCylinderField = new NumberField(getTranslation("artform.car.cylinders"));
        carKmField       = new NumberField(getTranslation("artform.car.km"));
        carYearFabField     = new TextField(getTranslation("artform.car.yearMade"));
        carYearServiceField = new TextField(getTranslation("artform.car.firstReg"));

        carFuelSelect = new Select<>();
        carFuelSelect.setLabel(getTranslation("artform.car.fuel"));
        carFuelSelect.setItems(dataService.loadFuelTypes(lang));
        carFuelSelect.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);

        carGearboxSelect = new Select<>();
        carGearboxSelect.setLabel(getTranslation("artform.car.gearbox"));
        carGearboxSelect.setItems(dataService.loadGearboxTypes(lang));
        carGearboxSelect.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);

        carClimaCheck = new Checkbox(getTranslation("artform.car.ac"));

        form.add(carHorseField, carCylinderField, carKmField,
                carYearFabField, carYearServiceField,
                carFuelSelect, carGearboxSelect, carClimaCheck);
    }

    private void addBookFields(FormLayout form) {
        bookAuthorField = new TextField(getTranslation("artform.book.author"));
        bookEditorField = new TextField(getTranslation("artform.book.publisher"));
        bookYearField   = buildYearField();
        form.add(bookAuthorField, bookEditorField, bookYearField);
    }

    private void addImmobilierFields(FormLayout form) {
        form.add(new H3(getTranslation("artform.immo.title")), 2);

        immoTypeSelect = new Select<>();
        immoTypeSelect.setLabel(getTranslation("artform.immo.type"));
        immoTypeSelect.setItems(dataService.loadLocationOrBuyOptions(lang));
        immoTypeSelect.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);

        immoCountrySelect = new Select<>();
        immoCountrySelect.setLabel(getTranslation("artform.country"));
        immoCountrySelect.setItems(dataService.loadCountries());
        immoCountrySelect.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);

        immoRegionSelect = new Select<>();
        immoRegionSelect.setLabel(getTranslation("artform.immo.region"));
        immoRegionSelect.setVisible(false);

        immoCountrySelect.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                String country = e.getValue().label();
                if ("Suisse".equals(country)) {
                    immoRegionSelect.setItems(dataService.loadCantons(lang));
                    immoRegionSelect.setLabel(getTranslation("artform.canton"));
                    immoRegionKind = "CANTON";
                    immoRegionSelect.setVisible(true);
                } else if ("France".equals(country)) {
                    immoRegionSelect.setItems(dataService.loadDepartements());
                    immoRegionSelect.setLabel(getTranslation("artform.departement"));
                    immoRegionKind = "DEPARTEMENT";
                    immoRegionSelect.setVisible(true);
                } else {
                    immoRegionKind = null;
                    immoRegionSelect.setVisible(false);
                }
                immoRegionSelect.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);
                immoRegionSelect.clear();
            }
        });

        immoCityField    = new TextField(getTranslation("artform.immo.city"));
        immoAddressField = new TextField(getTranslation("artform.immo.address"));
        immoPostalField  = new TextField(getTranslation("artform.immo.zip"));
        immoRoomsField   = new IntegerField(getTranslation("artform.immo.rooms"));
        immoSurfaceField = new IntegerField(getTranslation("artform.immo.living"));
        immoTerrainField = new TextField(getTranslation("artform.immo.land"));
        immoBuildYearField = new TextField(getTranslation("artform.immo.yearBuilt"));

        form.add(immoTypeSelect, immoCountrySelect, immoRegionSelect,
                immoCityField, immoAddressField, immoPostalField,
                immoRoomsField, immoSurfaceField, immoTerrainField, immoBuildYearField);
    }

    private void addVinFields(FormLayout form) {
        form.add(new H3(getTranslation("artform.wine.title")), 2);

        wineCountrySelect = new ComboBox<>(getTranslation("artform.country"));
        wineCountrySelect.setItems(dataService.loadWineCountries());
        wineCountrySelect.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);

        wineRegionSelect = new ComboBox<>(getTranslation("artform.wine.region"));
        wineRegionSelect.setVisible(false);

        wineCepageSelect = new ComboBox<>(getTranslation("artform.wine.grape"));
        wineCepageSelect.setItems(dataService.loadCepages());
        wineCepageSelect.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);
        wineMillesimeField = new TextField(getTranslation("artform.wine.vintage"));
        wineMillesimeField.setPlaceholder(getTranslation("artform.wine.vintage.ph"));

        wineTypeSelect = new Select<>();
        wineTypeSelect.setLabel(getTranslation("artform.wine.type"));
        wineTypeSelect.setItems(dataService.loadWineTypes(lang));
        wineTypeSelect.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);

        wineCountrySelect.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                List<ArticleDynamicDataService.LabelValue> regions =
                        dataService.loadWineRegions(e.getValue().label());
                wineRegionSelect.setItems(regions);
                wineRegionSelect.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);
                wineRegionSelect.setVisible(!regions.isEmpty());
                wineRegionSelect.clear();
                wineMillesimeField.clear();
            }
        });

        // ✅ CORRECTION : wineMillesimeField était ajouté deux fois
        form.add(wineCountrySelect, wineRegionSelect, wineMillesimeField, wineTypeSelect, wineCepageSelect);
    }

    private void addDvdFields(FormLayout form) {
        dvdActorsField   = new TextField(getTranslation("artform.dvd.actors"));
        dvdDirectorField = new TextField(getTranslation("artform.dvd.director"));
        dvdYearField     = buildYearField();
        dvdDurationField = new TextField(getTranslation("artform.dvd.duration"));
        form.add(dvdActorsField, dvdDirectorField, dvdYearField, dvdDurationField);
    }

    private void addTvFields(FormLayout form) {
        tvScreenTypeSelect = new Select<>();
        tvScreenTypeSelect.setLabel(getTranslation("artform.tv.screenType"));
        tvScreenTypeSelect.setItems(dataService.loadScreenTypes(lang));
        tvScreenTypeSelect.setItemLabelGenerator(ArticleDynamicDataService.LabelValue::label);
        tvDimensionField = new TextField(getTranslation("artform.tv.dimension"));
        form.add(tvScreenTypeSelect, tvDimensionField);
    }

    private void addPcFields(FormLayout form) {
        pcProcessorField = new NumberField(getTranslation("artform.pc.cpu"));
        pcRamField       = new TextField(getTranslation("artform.pc.ram"));
        pcHddField       = new TextField(getTranslation("artform.pc.disk"));
        form.add(pcProcessorField, pcRamField, pcHddField);
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    /** Remet à null toutes les références de champs dynamiques entre deux changements de catégorie */
    private void clearDynamicRefs() {
        fabricantField = null;
        carHorseField = carCylinderField = carKmField = null;
        carYearFabField = carYearServiceField = null;
        carFuelSelect = carGearboxSelect = null; carClimaCheck = null;
        bookAuthorField = bookEditorField = null; bookYearField = null;
        immoTypeSelect = immoCountrySelect = immoRegionSelect = null;
        immoCityField = immoAddressField = immoPostalField = immoBuildYearField = immoTerrainField = null;
        immoRoomsField = immoSurfaceField = null;
        immoRegionKind = null;
        // ✅ CORRECTION : wineMillesimeField et wineTypeSelect n'étaient pas remis à null
        // (ancienne valeur collectée à tort après un changement de catégorie), et
        // wineCepageSelect était réinitialisé deux fois.
        wineCountrySelect = wineRegionSelect = null;
        wineCepageSelect = null;
        wineMillesimeField = null;
        wineTypeSelect = null;
        gameTypeSelect = null;
        dvdActorsField = dvdDirectorField = dvdDurationField = null; dvdYearField = null;
        tvScreenTypeSelect = null; tvDimensionField = null;
        pcProcessorField = null; pcRamField = pcHddField = null;
        clothingSizeField = null;
    }

    private IntegerField buildYearField() {
        IntegerField f = new IntegerField(getTranslation("artform.year"));
        f.setMin(1900);
        f.setMax(java.time.Year.now().getValue());
        f.setStepButtonsVisible(true);
        return f;
    }

    /** Convertit un texte en entier, retourne null si vide ou non numérique. */
    private Integer parseIntOrNull(String s) {
        if (s == null || s.isBlank()) return null;
        try { return Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    /** Extrait l'id (value) d'un Select<LabelValue>, retourne null si rien de sélectionné */
    private Integer selectedId(Select<ArticleDynamicDataService.LabelValue> select) {
        if (select == null || select.getValue() == null) return null;
        try { return Integer.parseInt(select.getValue().value()); }
        catch (NumberFormatException e) { return null; }
    }

    private Integer selectedId(ComboBox<ArticleDynamicDataService.LabelValue> combo) {
        if (combo == null || combo.getValue() == null) return null;
        try { return Integer.parseInt(combo.getValue().value()); }
        catch (NumberFormatException e) { return null; }
    }
}
