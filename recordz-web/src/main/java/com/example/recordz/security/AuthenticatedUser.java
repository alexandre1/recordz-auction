package com.example.recordz.security;

import com.example.recordz.model.domain.Personne;
import com.example.recordz.repository.PersonneRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Fournit l'accès à la {@link Personne} connectée via le SecurityContext Spring.
 *
 * Utilisation dans une vue Vaadin :
 * <pre>
 *   {@code
 *   @Autowired
 *   private AuthenticatedUser authenticatedUser;
 *
 *   Optional<Personne> user = authenticatedUser.get();
 *   }
 * </pre>
 */
@Component
public class AuthenticatedUser {

    private final PersonneRepository personneRepository;

    public AuthenticatedUser(PersonneRepository personneRepository) {
        this.personneRepository = personneRepository;
    }

    /**
     * Retourne la {@link Personne} connectée, ou {@link Optional#empty()} si anonyme.
     */
    public Optional<Personne> get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof OidcUser oidcUser)) {
            return Optional.empty();
        }

        String email = oidcUser.getEmail();
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }

        return personneRepository.findByEmail(email);
    }

    /**
     * Retourne true si un utilisateur est actuellement connecté.
     */
    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof OidcUser;
    }

    public Optional<OidcUser> getOidcUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof OidcUser oidcUser) {
            return Optional.of(oidcUser);
        }
        return Optional.empty();
    }
}
