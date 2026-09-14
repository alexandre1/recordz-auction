package com.example.recordz.repository;

import com.example.recordz.model.domain.Personne;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.jooq.impl.DSL.*;

@Repository
public class PersonneRepository {

    private static final int MAX_USERNAME_ATTEMPTS = 10;

    private final DSLContext dsl;

    public PersonneRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public Optional<Personne> findById(Long id) {
        return dsl.select()
                .from(table("personne"))
                .where(field("id_personne").eq(id))
                .fetchOptional()
                .map(this::toPersonne);
    }

    public Optional<Personne> findByEmail(String email) {
        return dsl.select()
                .from(table("personne"))
                .where(field("email").eq(email))
                .fetchOptional()
                .map(this::toPersonne);
    }

    public Optional<Personne> findByNomUtilisateur(String nomUtilisateur) {
        return dsl.select()
                .from(table("personne"))
                .where(field("nom_utilisateur").eq(nomUtilisateur))
                .fetchOptional()
                .map(this::toPersonne);
    }

    /**
     * Upsert OAuth2 : crée ou met à jour la personne à partir de son email Google.
     *
     * Utilise un pattern "retry-on-conflict" plutôt qu'un check-then-act :
     * avec les virtual threads, plusieurs requêtes concurrentes pour le même
     * email (double-clic, plusieurs onglets) sont plus probables, donc on
     * s'appuie sur les contraintes UNIQUE en base (email, nom_utilisateur)
     * comme filet de sécurité atomique, et on réessaie en cas de collision
     * sur le nom d'utilisateur.
     *
     * Prérequis en base (Flyway) : contraintes UNIQUE sur personne.email
     * et personne.nom_utilisateur.
     */
    public Personne upsertFromOAuth(String email, String displayName) {
        Optional<Personne> existing = findByEmail(email);
        if (existing.isPresent()) {
            return existing.get();
        }

        String baseUsername = email.split("@")[0];

        for (int attempt = 0; attempt <= MAX_USERNAME_ATTEMPTS; attempt++) {
            String candidate = attempt == 0 ? baseUsername : baseUsername + attempt;
            try {
                dsl.insertInto(table("personne"))
                        .set(field("nom_utilisateur"), candidate)
                        .set(field("mot_de_passe"), "")           // non utilisé avec OAuth2
                        .set(field("nom"), displayName != null ? displayName : "")
                        .set(field("prenom"), "")
                        .set(field("adresse"), "")
                        .set(field("npa"), 0)
                        .set(field("ville"), "")
                        .set(field("pays"), "")
                        .set(field("email"), email)
                        .set(field("no_telephone"), "")
                        .set(field("active"), 1)
                        .set(field("level"), 1)
                        .set(field("ref_host"), "")
                        .execute();

                return findByEmail(email).orElseThrow();

            } catch (DataAccessException e) {
                if (!isDuplicateKeyViolation(e)) {
                    throw e;
                }

                // Un autre thread vient peut-être de créer le même email
                // entre notre findByEmail initial et cet insert : on lui cède la main.
                Optional<Personne> raceWinner = findByEmail(email);
                if (raceWinner.isPresent()) {
                    return raceWinner.get();
                }

                // Sinon, c'est le nom_utilisateur qui est en collision : on réessaie
                // avec un suffixe, sauf si on a épuisé les tentatives.
                if (attempt == MAX_USERNAME_ATTEMPTS) {
                    throw e;
                }
            }
        }

        // Inatteignable, mais requis par le compilateur
        throw new IllegalStateException("Échec de l'upsert OAuth2 pour " + email);
    }

    private boolean isDuplicateKeyViolation(DataAccessException e) {
        return e.getMessage() != null && e.getMessage().contains("Duplicate entry");
    }

    public void update(Personne p) {
        dsl.update(table("personne"))
                .set(field("nom"), p.getNom())
                .set(field("prenom"), p.getPrenom())
                .set(field("adresse"), p.getAdresse())
                .set(field("npa"), p.getNpa())
                .set(field("ville"), p.getVille())
                .set(field("pays"), p.getPays())
                .set(field("no_telephone"), p.getNoTelephone())
                .set(field("lang"), p.getLang())
                .set(field("banque"), p.getBanque())
                .set(field("no_compte"), p.getNoCompte())
                .set(field("iban"), p.getIban())
                .set(field("paypal_me"), p.getPaypalMe())
                .set(field("ref_canton"), p.getRefCanton())
                .where(field("id_personne").eq(p.getIdPersonne()))
                .execute();
    }

    // ── Mapper ────────────────────────────────────────────────

    private Personne toPersonne(org.jooq.Record r) {
        Personne p = new Personne();
        p.setIdPersonne(r.get(field("id_personne"), Long.class));
        p.setNomUtilisateur(r.get(field("nom_utilisateur"), String.class));
        p.setMotDePasse(r.get(field("mot_de_passe"), String.class));
        p.setNom(r.get(field("nom"), String.class));
        p.setPrenom(r.get(field("prenom"), String.class));
        p.setAdresse(r.get(field("adresse"), String.class));
        p.setNpa(r.get(field("npa"), Integer.class));
        p.setVille(r.get(field("ville"), String.class));
        p.setPays(r.get(field("pays"), String.class));
        p.setEmail(r.get(field("email"), String.class));
        p.setNoTelephone(r.get(field("no_telephone"), String.class));
        p.setActive(r.get(field("active"), Integer.class));
        p.setLevel(r.get(field("level"), Integer.class));
        p.setRefHost(r.get(field("ref_host"), String.class));
        p.setLang(r.get(field("lang"), String.class));
        p.setBanque(r.get(field("banque"), String.class));
        p.setNoCompte(r.get(field("no_compte"), String.class));
        p.setIban(r.get(field("iban"), String.class));
        p.setRefCanton(r.get(field("ref_canton"), Integer.class));
        p.setPaypalMe(r.get(field("paypal_me"), String.class));
        return p;
    }
}