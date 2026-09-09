package com.example.recordz.ui;

import com.example.recordz.model.domain.Personne;
import com.example.recordz.service.PersonneService;
import com.example.recordz.ui.layouts.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@Route(value = "mon-compte/informations", layout = MainLayout.class)
@PageTitle("Avant-Garde - Mes informations")
@PermitAll
class InformationsPersonnellesView extends VerticalLayout implements BeforeEnterObserver {


    private final PersonneService personneService;

    public InformationsPersonnellesView(PersonneService personneService) {
        this.personneService = personneService;
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
                .set("width", "640px")
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

        // ✅ Utilisation de person.getXxx() pour préremplir les champs
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
        paypalF.setPlaceholder("paypal.me/votrecompte");

        Select<String> paysSel = new Select<>();
        paysSel.setItems("Suisse", "France", "Belgique", "Allemagne", "Italie");
        paysSel.setValue(nullSafe(person.getPays(), "Suisse"));
        paysSel.setWidth("200px");

        // Bouton Save qui appelle le service
        Button send = new Button("Enregistrer", e -> {
            person.setPrenom(prenomF.getValue());
            person.setNom(nomF.getValue());
            person.setAdresse(adresseF.getValue());
            person.setVille(villeF.getValue());
            person.setNpa(npaF.getValue().isEmpty() ? 0 : Integer.parseInt(npaF.getValue()));
            person.setNoTelephone(telF.getValue());
            person.setIban(ibanF.getValue());
            person.setPaypalMe(paypalF.getValue());
            person.setPays(paysSel.getValue());
//            personneService.update(person);
        });

        Button reset = new Button("Reset", e -> {
            prenomF.clear(); nomF.clear(); adresseF.clear();
            villeF.clear();  npaF.clear(); telF.clear();
        });

        send.getStyle().set("background-color", "#E0E0E0");
        reset.getStyle().set("background-color", "#E0E0E0");

        card.add(

                row("Nom et prénom",      nomF),
                rowComp("Pays", paysSel),
                row("Adresse",  adresseF),
                row("Ville",    villeF),
                row("NPA",      npaF),
                row("Téléphone", telF),
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

    private String nullSafe(String value, String fallback) {
        return (value != null && !value.isEmpty()) ? value : fallback;
    }



    private HorizontalLayout row(String label, TextField field) {
        HorizontalLayout r = new HorizontalLayout();
        r.setAlignItems(Alignment.BASELINE);
        r.getStyle()
                .set("margin-bottom", "5px")
                .set("padding-left", "8px");  // ← AJOUTER
        Span lbl = new Span(label);
        lbl.getStyle().set("width", "200px").set("color", "#0000CC").set("font-size", "0.85rem");
        r.add(lbl, field);
        return r;
    }
    private HorizontalLayout rowComp(String label, com.vaadin.flow.component.Component comp) {
        HorizontalLayout r = new HorizontalLayout();
        r.setAlignItems(Alignment.BASELINE);
        r.getStyle()
                .set("margin-bottom", "5px")
                .set("padding-left", "8px");  // ← AJOUTER
        Span lbl = new Span(label);
        lbl.getStyle().set("width", "200px").set("color", "#0000CC").set("font-size", "0.85rem");
        r.add(lbl, comp);
        return r;
    }

    private HorizontalLayout rowNote(String note, TextField field) {
        HorizontalLayout r = new HorizontalLayout();
        r.setAlignItems(Alignment.CENTER);
        r.getStyle()
                .set("margin-bottom", "5px")
                .set("padding-left", "8px");  // ← AJOUTER
        Span lbl = new Span(note);
        lbl.getStyle().set("width", "200px").set("font-size", "0.75rem").set("color", "#0000CC");
        r.add(lbl, field);
        return r;
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
                navLink("Faire de la publicité", "/publicite"),
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

}
