package com.example.recordz.repository;

import com.example.recordz.model.domain.Commande;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

@Repository
public class CommandeRepository {

    private final DSLContext dsl;

    public CommandeRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    // ── Lecture ───────────────────────────────────────────────

    public Optional<Commande> findById(Long id) {
        return dsl.select()
                .from(table("commande"))
                .where(field("id_commande").eq(id))
                .fetchOptional()
                .map(this::toCommande);
    }

    /**
     * Toutes les commandes passées par un acheteur (client_ref).
     */
    public List<Commande> findByAcheteur(Long clientRef) {
        return dsl.select()
                .from(table("commande"))
                .where(field("client_ref").eq(clientRef))
                .orderBy(field("date").desc())
                .fetch()
                .map(this::toCommande);
    }

    /**
     * Toutes les commandes reçues par un vendeur.
     */
    public List<Commande> findByVendeur(Long refVendeur) {
        return dsl.select()
                .from(table("commande"))
                .where(field("ref_vendeur").eq(refVendeur))
                .orderBy(field("date").desc())
                .fetch()
                .map(this::toCommande);
    }

    /**
     * Commandes filtrées par statut (ex. 1 = en attente, 2 = payée, 3 = livrée).
     */
    public List<Commande> findByStatut(Integer refStatut) {
        return dsl.select()
                .from(table("commande"))
                .where(field("ref_statut").eq(refStatut))
                .orderBy(field("date").desc())
                .fetch()
                .map(this::toCommande);
    }

    /**
     * Articles liés à une commande (table commande_article).
     */
    public List<Long> findArticlesByCommande(Long refCommande) {
        return dsl.select(field("ref_article", Long.class))
                .from(table("commande_article"))
                .where(field("ref_commande").eq(refCommande))
                .fetch()
                .map(r -> r.get(field("ref_article", Long.class)));
    }

    // ── Écriture ──────────────────────────────────────────────

    /**
     * Insère une nouvelle commande et retourne son id généré.
     */
    public Long save(Commande c) {
        var record = dsl.insertInto(table("commande"))
                .set(field("session_id"),            c.getSessionId())
                .set(field("client_ref"),            c.getClientRef())
                .set(field("date"),                  LocalDate.now())
                .set(field("ref_mode_de_payement"),  c.getRefModeDePayement())
                .set(field("ref_vendeur"),            c.getRefVendeur())
                .set(field("ref_statut"),             c.getRefStatut() != null ? c.getRefStatut() : 1)
                .set(field("ref_mode_de_livraison"), c.getRefModeDeLivraison())
                .returningResult(field("id_commande"))
                .fetchOne();

        return record != null ? record.get(field("id_commande"), Long.class) : null;
    }

    /**
     * Associe un article à une commande existante.
     */
    public void addArticle(Long refCommande, Long refArticle) {
        dsl.insertInto(table("commande_article"))
                .set(field("ref_commande"),  refCommande)
                .set(field("ref_article"),   refArticle)
                .set(field("date_payement"), (LocalDate) null)
                .execute();
    }

    /**
     * Enregistre la date de paiement d'un article dans une commande.
     */
    public void marquerArticlePaye(Long refCommande, Long refArticle) {
        dsl.update(table("commande_article"))
                .set(field("date_payement"), LocalDate.now())
                .where(field("ref_commande").eq(refCommande))
                .and(field("ref_article").eq(refArticle))
                .execute();
    }

    /**
     * Met à jour le statut d'une commande.
     */
    public void updateStatut(Long idCommande, Integer refStatut) {
        dsl.update(table("commande"))
                .set(field("ref_statut"), refStatut)
                .where(field("id_commande").eq(idCommande))
                .execute();
    }

    /**
     * Enregistre la date de paiement globale de la commande.
     */
    public void marquerPayee(Long idCommande) {
        dsl.update(table("commande"))
                .set(field("date_payement"), LocalDate.now())
                .set(field("ref_statut"),    2)          // 2 = payée
                .where(field("id_commande").eq(idCommande))
                .execute();
    }

    /**
     * Supprime une commande et ses articles associés (cascade manuelle).
     */
    public void delete(Long idCommande) {
        dsl.deleteFrom(table("commande_article"))
                .where(field("ref_commande").eq(idCommande))
                .execute();

        dsl.deleteFrom(table("commande"))
                .where(field("id_commande").eq(idCommande))
                .execute();
    }

    // ── Mapper ────────────────────────────────────────────────

    private Commande toCommande(org.jooq.Record r) {
        Commande c = new Commande();
        c.setIdCommande(r.get(field("id_commande", Long.class)));
        c.setSessionId(r.get(field("session_id", String.class)));
        c.setClientRef(r.get(field("client_ref", Long.class)));
        c.setDate(r.get(field("date", LocalDate.class)));
        c.setRefModeDePayement(r.get(field("ref_mode_de_payement", Integer.class)));
        c.setDatePayement(r.get(field("date_payement", LocalDate.class)));
        c.setRefVendeur(r.get(field("ref_vendeur", Long.class)));
        c.setRefStatut(r.get(field("ref_statut", Integer.class)));
        c.setRefModeDeLivraison(r.get(field("ref_mode_de_livraison", Integer.class)));
        return c;
    }
}
