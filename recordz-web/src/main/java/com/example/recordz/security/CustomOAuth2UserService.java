package com.example.recordz.security;

import com.example.recordz.model.domain.Personne;
import com.example.recordz.service.PersonneService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

/**
 * Service OAuth2 personnalisé : à chaque connexion Google,
 * synchronise l'utilisateur OIDC avec la table {@code personne}.
 *
 * Flux :
 *  1. Spring Security reçoit le token Google
 *  2. Ce service est appelé pour charger/créer l'utilisateur
 *  3. On fait un upsert dans {@code personne} via {@link PersonneService}
 *  4. On retourne l'OidcUser standard pour que Spring Security continue
 */
@Service
public class CustomOAuth2UserService extends OidcUserService {

    private final PersonneService personneService;

    public CustomOAuth2UserService(PersonneService personneService) {
        this.personneService = personneService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. Déléguer le chargement standard à Spring Security
        OidcUser oidcUser = super.loadUser(userRequest);

        // 2. Extraire les infos du token Google
        String email       = oidcUser.getEmail();
        String displayName = oidcUser.getFullName();

        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException("Email manquant dans le token Google");
        }

        // 3. Upsert dans la table personne
        //    - Si l'email existe déjà → on retourne la personne existante
        //    - Sinon → on crée un nouveau compte
        Personne personne = personneService.syncOAuth2User(email, displayName);

        // 4. On peut logger ou auditer ici si besoin
        // log.info("Connexion OAuth2 : {} (id={})", email, personne.getIdPersonne());

        // 5. Retourner l'OidcUser tel quel — Spring Security l'utilise pour la session
        return oidcUser;
    }
}
