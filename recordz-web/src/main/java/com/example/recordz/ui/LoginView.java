package com.example.recordz.ui;

import com.example.recordz.security.AuthenticatedUser;
import com.vaadin.flow.component.Html;
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
        getStyle().set("background", "#f5f5f5");

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

        // Logo / titre
        Html logo = new Html("""
                <div style="text-align:center; margin-bottom: 8px;">
                    <span style="font-size:2rem; font-weight:700; color:#1a1a2e;">
                        🎵 Recordz
                    </span>
                    <p style="color:#666; margin-top:6px; font-size:0.95rem;">
                        La marketplace suisse
                    </p>
                </div>
                """);

        // Bouton Google OAuth2
        // Spring Security expose automatiquement /oauth2/authorization/google
        Html googleButton = new Html("""
                <a href="/oauth2/authorization/google"
                   style="
                     display: flex;
                     align-items: center;
                     justify-content: center;
                     gap: 12px;
                     width: 100%;
                     padding: 12px 24px;
                     margin-top: 24px;
                     background: white;
                     color: #444;
                     border: 1px solid #ddd;
                     border-radius: 8px;
                     font-size: 1rem;
                     font-weight: 500;
                     text-decoration: none;
                     cursor: pointer;
                     box-shadow: 0 1px 4px rgba(0,0,0,0.08);
                     transition: box-shadow 0.2s;
                   "
                   onmouseover="this.style.boxShadow='0 2px 8px rgba(0,0,0,0.18)'"
                   onmouseout="this.style.boxShadow='0 1px 4px rgba(0,0,0,0.08)'"
                >
                  <img src="https://www.gstatic.com/firebasejs/ui/2.0.0/images/auth/google.svg"
                       width="22" height="22" alt="Google"/>
                  Se connecter avec Google
                </a>
                """);

        // Message d'info
        Html info = new Html("""
                <p style="
                  text-align:center;
                  color:#999;
                  font-size:0.80rem;
                  margin-top:20px;
                ">
                  En vous connectant, vous acceptez les conditions d'utilisation de Recordz.
                </p>
                """);

        card.add(logo, googleButton, info);
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
