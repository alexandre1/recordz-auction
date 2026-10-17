package com.example.recordz.ui;

import com.example.recordz.model.domain.Personne;
import com.example.recordz.service.PersonneService;
import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@Route(value = "mon-compte/informations", layout = MainLayout.class)
@PermitAll
public class InformationsPersonnellesView extends VerticalLayout
        implements BeforeEnterObserver, HasDynamicTitle {

    /** Valeurs stockées en base (français) ; seul l'affichage est traduit. */
    private static final String[] PAYS = {"Suisse", "France", "Belgique", "Allemagne", "Italie"};

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

        // Utilisation de person.getXxx() pour préremplir les champs
        TextField prenomF  = field(nullSafe(person.getPrenom()));
        TextField nomF     = field(nullSafe(person.getNom()));
        TextField adresseF = field(nullSafe(person.getAdresse()));
        TextField villeF   = field(nullSafe(person.getVille()));
        TextField npaF     = field(person.getNpa() != null ? person.getNpa().toString() : "");
        TextField telF     = field(nullSafe(person.getNoTelephone()));
        TextField ibanF    = new TextField();
        ibanF.setValue(nullSafe(person.getIban()));
        ibanF.setWidth("250px");
        ibanF.setPlaceholder("CH00 0000 0000 0000 0000 0");

        TextField paypalF = new TextField();
        paypalF.setValue(nullSafe(person.getPaypalMe()));
        paypalF.setWidth("250px");
        paypalF.setPlaceholder(getTranslation("info.placeholder.paypal"));

        Select<String> paysSel = new Select<>();
        paysSel.setItems(PAYS);
        paysSel.setItemLabelGenerator(this::countryLabel);   // valeur = français, libellé = langue de l'UI
        paysSel.setValue(nullSafe(person.getPays(), "Suisse"));
        paysSel.setWidth("200px");

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
            person.setPrenom(prenomF.getValue());
            person.setNom(nomF.getValue());
            person.setAdresse(adresseF.getValue());
            person.setVille(villeF.getValue());
            person.setNpa(npa);
            person.setNoTelephone(telF.getValue());
            person.setIban(ibanF.getValue());
            person.setPaypalMe(paypalF.getValue());
            person.setPays(paysSel.getValue());
            personneService.updateProfil(person);
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
                row(getTranslation("info.field.phone"),    telF),
                rowComp("IBAN", ibanF),
                rowComp("PayPal", paypalF),
                new HorizontalLayout(send, reset)
        );

        return card;
    }

    /** Libellé traduit d'un pays ; retombe sur la valeur brute si aucune traduction n'existe. */
    private String countryLabel(String value) {
        String key = "info.country." + value;
        String translated = getTranslation(key);
        return key.equals(translated) ? value : translated;
    }

    // Évite les NullPointerException sur les champs potentiellement null
    private String nullSafe(String value) {
        return value != null ? value : "";
    }

    private String nullSafe(String value, String fallback) {
        return (value != null && !value.isEmpty()) ? value : fallback;
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
