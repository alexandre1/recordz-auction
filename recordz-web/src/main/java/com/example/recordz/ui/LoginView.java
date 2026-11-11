package com.example.recordz.ui;

import com.example.recordz.security.AuthenticatedUser;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * Page de connexion Recordz.
 * Accessible sans authentification (@AnonymousAllowed).
 * Redirige vers OAuth2 Google via le endpoint Spring Security.
 */
@Route("login")
@PageTitle("Connexion — Recordz")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;

    public LoginView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background", "white");

        // Style du survol du bouton Google (pas de style multi-ligne dans un attribut)
        add(new Html("<style>.google-btn:hover{box-shadow:0 2px 8px rgba(0,0,0,0.18) !important;}</style>"));
        add(buildLoginCard());
    }

    private VerticalLayout buildLoginCard() {
        VerticalLayout card = new VerticalLayout();
        card.setAlignItems(Alignment.CENTER);
        card.setWidth("400px");
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("padding", "40px")
                .set("box-shadow", "0 4px 24px rgba(0,0,0,0.10)");

        // ── Image ─────────────────────────────────────────────
        Image img = new Image("/images/logo2.png", "Avant-Garde");
        img.setWidth("50%");
        img.setHeight("auto");
        img.getStyle()
                .set("max-width", "50%")
                .set("object-fit", "contain")
                .set("display", "block")
                .set("margin", "0 auto")              // centre l'image horizontalement
                .set("mix-blend-mode", "multiply");
        Div imgBox = new Div(img);
        imgBox.setWidth("100%");
        imgBox.getStyle().set("margin-bottom", "12px");
        // ── Logo / titre ──────────────────────────────────────
        Paragraph subtitle = new Paragraph("La marketplace suisse");
        subtitle.getStyle()
                .set("color", "#666")
                .set("margin-top", "6px")
                .set("font-size", "0.95rem");

        Div logo = new Div(subtitle);
        logo.getStyle()
                .set("text-align", "center")
                .set("margin-bottom", "8px");

        // ── Bouton Google OAuth2 ──────────────────────────────
        // Spring Security expose automatiquement /oauth2/authorization/google
        Anchor googleButton = new Anchor("/oauth2/authorization/google");
        googleButton.setRouterIgnore(true); // lien serveur, pas une route Vaadin
        googleButton.addClassName("google-btn");

        Image googleIcon = new Image(
                "https://www.gstatic.com/firebasejs/ui/2.0.0/images/auth/google.svg", "Google");
        googleIcon.setWidth("22px");
        googleIcon.setHeight("22px");

        googleButton.add(googleIcon, new Span("Se connecter avec Google"));
        googleButton.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("gap", "12px")
                .set("width", "100%")
                .set("box-sizing", "border-box")
                .set("padding", "12px 24px")
                .set("margin-top", "24px")
                .set("background", "white")
                .set("color", "#444")
                .set("border", "1px solid #ddd")
                .set("border-radius", "8px")
                .set("font-size", "1rem")
                .set("font-weight", "500")
                .set("text-decoration", "none")
                .set("cursor", "pointer")
                .set("box-shadow", "0 1px 4px rgba(0,0,0,0.08)")
                .set("transition", "box-shadow 0.2s");

        // ── Message d'info ────────────────────────────────────
        Paragraph info = new Paragraph(
                "En vous connectant, vous acceptez les conditions d'utilisation de Recordz.");
        info.getStyle()
                .set("text-align", "center")
                .set("color", "#999")
                .set("font-size", "0.80rem")
                .set("margin-top", "20px");

        card.add(imgBox, logo, googleButton, info);
        return card;
    }

    /**
     * Si l'utilisateur est déjà connecté, on le redirige vers l'accueil.
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.isLoggedIn()) {
            event.forwardTo("");
        }
    }
}
