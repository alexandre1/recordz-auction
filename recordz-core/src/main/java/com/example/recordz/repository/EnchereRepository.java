package com.example.recordz.repository;

import com.example.recordz.model.domain.Enchere;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

@Repository
public class EnchereRepository {

    private final DSLContext dsl;

    public EnchereRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<Enchere> findByArticle(Long refArticle) {
        return dsl.select()
                .from(table("enchere"))
                .where(field("ref_article").eq(refArticle))
                .orderBy(field("prix").desc())
                .fetch()
                .map(this::toEnchere);
    }
    public List<Enchere> findAllByVendeur(int limit, int offset, String email) {
        return dsl.select()
                .from(table("enchere").as("e"))
                .join(table("article").as("a"))
                .on(field("e.ref_article").eq(field("a.id_article")))
                .join(table("utilisateur").as("u"))
                .on(field("a.ref_vendeur").eq(field("u.id_utilisateur")))
                .where(field("u.email").eq(email))
                .orderBy(field("e.date_enchere").desc())
                .limit(limit)
                .offset(offset)
                .fetch()
                .map(this::toEnchere);
    }

    public int countAllByVendeur(String email) {
        return dsl.fetchCount(
                dsl.select()
                        .from(table("enchere").as("e"))
                        .join(table("article").as("a"))
                        .on(field("e.ref_article").eq(field("a.id_article")))
                        .join(table("utilisateur").as("u"))
                        .on(field("a.ref_vendeur").eq(field("u.id_utilisateur")))
                        .where(field("u.email").eq(email))
        );
    }

    public List<Enchere> findAllByEnchereur(String email) {
        return dsl.select()
                .from(table("enchere").as("e"))
                .join(table("utilisateur").as("u"))
                .on(field("e.ref_enchereur").eq(field("u.id_utilisateur")))
                .where(field("u.email").eq(email))
                .orderBy(field("e.date_enchere").desc())
                .fetch()
                .map(this::toEnchere);
    }
    public Optional<Enchere> findHighestBid(Long refArticle) {
        return dsl.select()
                .from(table("enchere"))
                .where(field("ref_article").eq(refArticle))
                .orderBy(field("prix").desc())
                .limit(1)
                .fetchOptional()
                .map(this::toEnchere);
    }

    public Optional<Enchere> findByEnchereur(Long refArticle, Long refEnchereur) {
        return dsl.select()
                .from(table("enchere"))
                .where(field("ref_article").eq(refArticle))
                .and(field("ref_enchereur").eq(refEnchereur))
                .orderBy(field("prix").desc())
                .limit(1)
                .fetchOptional()
                .map(this::toEnchere);
    }

    /**
     * Enregistre une nouvelle mise et incrémente le compteur sur l'article.
     */
    public void save(Long refArticle, Long refEnchereur, BigDecimal prix) {
        dsl.insertInto(table("enchere"))
                .set(field("ref_article"),   refArticle)
                .set(field("ref_enchereur"), refEnchereur)
                .set(field("prix"),          prix)
                .set(field("date_enchere"),  LocalDateTime.now())
                .execute();

        // Incrémenter le compteur d'enchères sur l'article
        dsl.update(table("article"))
                .set(field("nbr_enchere", Long.class), field("nbr_enchere", Long.class).add(1))
                .where(field("id_article").eq(refArticle))
                .execute();
    }

    public int countByArticle(Long refArticle) {
        return dsl.fetchCount(table("enchere"),
                field("ref_article").eq(refArticle));
    }

    // ── Mapper ────────────────────────────────────────────────

    private Enchere toEnchere(org.jooq.Record r) {
        Enchere e = new Enchere();
        e.setIdEnchere(r.get(field("id_enchere"), Long.class));
        e.setRefArticle(r.get(field("ref_article"), Long.class));
        e.setRefEnchereur(r.get(field("ref_enchereur"), Long.class));
        e.setPrix(r.get(field("prix", BigDecimal.class)));
        e.setDateEnchere(r.get(field("date_enchere", LocalDateTime.class)));
        return e;
    }
}
