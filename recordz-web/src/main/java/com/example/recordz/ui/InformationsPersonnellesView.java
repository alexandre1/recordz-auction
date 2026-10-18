package com.example.recordz.ui;

import com.example.recordz.model.domain.Personne;
import com.example.recordz.service.PersonneService;
import com.example.recordz.ui.PaysPhoneUtil.PaysPhone;
import com.example.recordz.ui.PaysPhoneUtil.PhoneParts;
import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;
import java.util.Locale;

@Route(value = "mon-compte/informations", layout = MainLayout.class)
@PermitAll
public class InformationsPersonnellesView extends VerticalLayout
        implements BeforeEnterObserver, HasDynamicTitle {

    /** Préfixe PayPal fixe : affiché dans le champ, non éditable et non traduit. */
    private static final String PAYPAL_PREFIX = "paypal.me/";

    private final PersonneService personneService;

    public InformationsPersonnellesView(PersonneService personneService) {
        this.personneService = personneService;
    }

    @Override
    public String getPageTitle() {
        return getTranslation("info.page.title");
    }

    private TextField field(String val) {
        TextField tf = new TextField();
        tf.setValue(val);
        tf.setWidth("200px");
        return tf;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // 1. Récupérer l'email depuis le contexte de sécurité
        OidcUser user = (OidcUser) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        String email = user.getAttribute("email");

        // 2. Récupérer la personne depuis la base
        Personne person = personneService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Personne non trouvée"));

        // 3. Construire l'UI avec les vraies valeurs
        removeAll();
        setPadding(true);
        setSpacing(false);

        getStyle()
                .set("border-radius", "20px")
                .set("overflow", "hidden")
                .set("width", "100%")
                .set("max-width", "640px")
                .set("box-sizing", "border-box")
                .set("margin-bottom", "24px");

        add(buildSecondaryNav());
        add(new Hr());
        add(buildForm(person));
    }

    private Div buildForm(Personne person) {
        Div card = new Div();
        card.getStyle()
                .set("border-radius", "20px")
                .set("border", "1px solid #6100C1")
                .set("overflow", "hidden")
                .set("padding", "8px 16px");

        // ── Liste des pays (même source que DetailArticleView) ──
        List<PaysPhone> pays = PaysPhoneUtil.buildList(getLocale());

        // Pays de la personne : la valeur en base est le nom français ("Suisse", "France"…)
        PaysPhone suisse = PaysPhoneUtil.findByCode(pays, "CH");
        final PaysPhone paysInit = java.util.Optional
                .ofNullable(PaysPhoneUtil.findByFrenchName(pays, person.getPays()))
                .orElse(suisse);

        // Téléphone : indicatif + numéro déduits du numéro stocké
        PhoneParts tel = PaysPhoneUtil.splitStored(person.getNoTelephone(), paysInit, pays);

        // Utilisation de person.getXxx() pour préremplir les champs
        TextField prenomF  = field(nullSafe(person.getPrenom()));
        TextField nomF     = field(nullSafe(person.getNom()));
        nomF.setReadOnly(true);
        TextField adresseF = field(nullSafe(person.getAdresse()));

        TextField villeF   = field(nullSafe(person.getVille()));
        TextField npaF     = field(person.getNpa() != null ? person.getNpa().toString() : "");
        TextField ibanF    = new TextField();
        ibanF.setValue(nullSafe(person.getIban()));
        ibanF.setWidth("250px");
        ibanF.setPlaceholder("CH00 0000 0000 0000 0000 0");

        // ── PayPal : "paypal.me/" est un préfixe fixe (non éditable, non traduit),
        //    seul le nom du compte est saisi ──
        TextField paypalF = new TextField();
        Span paypalPrefix = new Span(PAYPAL_PREFIX);
        paypalPrefix.getStyle().set("color", "#555").set("font-size", "0.85rem");
        paypalF.setPrefixComponent(paypalPrefix);
        paypalF.setPlaceholder("username");           // volontairement non traduit
        paypalF.setWidth("280px");
        paypalF.setValue(extractPaypalUser(person.getPaypalMe()));

        // Si l'utilisateur colle un lien complet, on ne garde que le nom du compte
        paypalF.addValueChangeListener(e -> {
            String clean = extractPaypalUser(e.getValue());
            if (!clean.equals(e.getValue())) {
                paypalF.setValue(clean);
            }
        });

        // ── Pays : ComboBox avec drapeaux ──
        ComboBox<PaysPhone> paysSel = new ComboBox<>();
        paysSel.setItems(pays);
        PaysPhoneUtil.applyFlags(paysSel, false);   // avant setValue
        paysSel.setValue(paysInit);
        paysSel.setWidth("200px");

        // ── Téléphone : indicatif (avec drapeau) + numéro ──
        ComboBox<PaysPhone> indicatifBox = new ComboBox<>();
        indicatifBox.setItems(pays);
        PaysPhoneUtil.applyFlags(indicatifBox, true);   // avant setValue
        indicatifBox.setValue(tel.pays());
        indicatifBox.setWidth("160px");

        TextField telF = new TextField();
        telF.setValue(tel.numero());
        telF.setWidth("190px");
        telF.setPlaceholder("XX XXX XX XX");

        // Mise en forme du numéro selon le pays de l'indicatif
        // (un numéro saisi en "+..." est laissé tel quel)
        telF.addValueChangeListener(e -> {
            String v = e.getValue();
            PaysPhone dial = indicatifBox.getValue();
            if (v == null || v.isBlank() || v.trim().startsWith("+") || dial == null) return;
            String formatted = PaysPhoneUtil.formatAsYouType(v, dial.code());
            if (!formatted.equals(v)) {
                telF.setValue(formatted);
            }
        });

        // Quand l'utilisateur change l'indicatif, on reformate le numéro saisi
        indicatifBox.addValueChangeListener(e -> {
            String v = telF.getValue();
            PaysPhone dial = e.getValue();
            if (dial == null || v == null || v.isBlank() || v.trim().startsWith("+")) return;
            telF.setValue(PaysPhoneUtil.formatAsYouType(v, dial.code()));
        });

        // Quand l'utilisateur change le pays, l'indicatif suit
        // (isFromClient : on ne touche pas à l'indicatif lors du préremplissage)
        paysSel.addValueChangeListener(e -> {
            if (e.isFromClient() && e.getValue() != null) {
                indicatifBox.setValue(e.getValue());
            }
        });

        HorizontalLayout phoneLayout = new HorizontalLayout(indicatifBox, telF);
        phoneLayout.setAlignItems(Alignment.BASELINE);

        // Bouton Save qui appelle le service
        Button send = new Button(getTranslation("info.btn.save"), e -> {
            int npa = 0;
            String npaText = npaF.getValue() == null ? "" : npaF.getValue().trim();
            if (!npaText.isEmpty()) {
                try {
                    npa = Integer.parseInt(npaText);
                } catch (NumberFormatException ex) {
                    Notification.show(getTranslation("info.notif.npa.invalid"));
                    return;
                }
            }

            PaysPhone paysChoisi = paysSel.getValue() != null ? paysSel.getValue() : paysInit;
            PaysPhone dial = indicatifBox.getValue() != null ? indicatifBox.getValue() : paysChoisi;

            // Numéro enregistré au format international sans zéro de tête : "+41791234567"
            String telE164 = PaysPhoneUtil.toE164(telF.getValue(), dial.code());
            if (telE164 == null) {
                Notification.show(getTranslation("info.notif.phone.invalid"));
                return;
            }

            // PayPal : on enregistre "paypal.me/nomDuCompte" (ou vide si rien n'est saisi)
            String paypalUser = extractPaypalUser(paypalF.getValue());

            person.setPrenom(prenomF.getValue());
            person.setNom(nomF.getValue());
            person.setAdresse(adresseF.getValue());
            person.setVille(villeF.getValue());
            person.setNpa(npa);
            person.setNoTelephone(telE164);
            person.setIban(ibanF.getValue());
            person.setPaypalMe(paypalUser.isEmpty() ? "" : PAYPAL_PREFIX + paypalUser);
            person.setPays(paysChoisi.nomFrancais());   // valeur stockée en français
            personneService.updateProfil(person);
            telF.setValue(PaysPhoneUtil.splitStored(telE164, dial, pays).numero());
            Notification.show(getTranslation("info.notif.saved"));
        });

        Button reset = new Button(getTranslation("info.btn.reset"), e -> {
            prenomF.clear(); nomF.clear(); adresseF.clear();
            villeF.clear();  npaF.clear(); telF.clear();
        });

        send.getStyle().set("background-color", "#E0E0E0");
        reset.getStyle().set("background-color", "#E0E0E0");

        card.add(
                row(getTranslation("info.field.fullname"), nomF),
                rowComp(getTranslation("info.field.country"), paysSel),
                row(getTranslation("info.field.address"),  adresseF),
                row(getTranslation("info.field.city"),     villeF),
                row(getTranslation("info.field.zip"),      npaF),
                rowComp(getTranslation("info.field.phone"), phoneLayout),
                rowComp("IBAN", ibanF),
                rowComp("PayPal", paypalF),
                new HorizontalLayout(send, reset)
        );

        return card;
    }

    // Évite les NullPointerException sur les champs potentiellement null
    private String nullSafe(String value) {
        return value != null ? value : "";
    }

    /**
     * Extrait le nom du compte d'une valeur comme "paypal.me/xyz",
     * "https://www.paypal.me/xyz" ou "xyz".
     */
    private String extractPaypalUser(String value) {
        if (value == null) return "";
        String s = value.trim();
        int i = s.toLowerCase(Locale.ROOT).indexOf(PAYPAL_PREFIX);
        if (i >= 0) {
            s = s.substring(i + PAYPAL_PREFIX.length());
        }
        return s.replaceAll("[\\s/]+", "");
    }

    private HorizontalLayout row(String label, TextField field) {
        return rowComp(label, field);
    }

    private HorizontalLayout rowComp(String label, Component comp) {
        HorizontalLayout r = new HorizontalLayout();
        r.setAlignItems(Alignment.BASELINE);
        r.getStyle()
                .set("margin-bottom", "5px")
                .set("padding-left", "8px");
        Span lbl = new Span(label);
        lbl.getStyle().set("width", "200px").set("color", "#0000CC").set("font-size", "0.85rem");
        r.add(lbl, comp);
        return r;
    }

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
}
