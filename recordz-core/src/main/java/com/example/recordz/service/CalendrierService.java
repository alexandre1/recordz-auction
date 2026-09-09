package com.example.recordz.service;

import com.example.recordz.model.domain.EvenementCalendrier;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// ✅ Imports manquants pour field() et table()
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Service
public class CalendrierService {

    private final DSLContext dsl;

    public CalendrierService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<EvenementCalendrier> findEvenements() {
        return dsl.selectDistinct(
                        field("id_article"),
                        field("nom"),
                        field("enchere_date_debut"),
                        field("enchere_date_fin")
                )
                .from(table("article"))
                .where(field("vendu").eq(0))
                .and(field("enchere_date_debut").isNotNull())
                .fetch()
                .map(r -> {
                    EvenementCalendrier evt = new EvenementCalendrier();
                    evt.setId(r.get("id_article", Long.class));
                    evt.setTitre(r.get("nom", String.class));
                    evt.setDebut(r.get("enchere_date_debut", LocalDateTime.class));
                    evt.setFin(r.get("enchere_date_fin", LocalDateTime.class));
                    evt.setCouleur("#6100C1");
                    return evt;
                });
    }

    public List<EvenementCalendrier> findArticlesByVendeurAndStatut(String email) {
        return dsl.select(
                        field("a.id_article").as("id_article"),
                        field("a.nom").as("nom"),
                        field("a.enchere_date_debut").as("enchere_date_debut"),
                        field("a.enchere_date_fin").as("enchere_date_fin")
                )
                .from(table("met_en_vente").as("m"))
                .join(table("article").as("a"))
                .on(field("m.ref_article").eq(field("a.id_article")))
                .join(table("personne").as("p"))
                .on(field("m.ref_vendeur").eq(field("p.id_personne")))
                .where(field("p.email").eq(email))
                .and(field("a.enchere_date_debut").isNotNull())
                .fetch()
                .map(r -> {
                    EvenementCalendrier evt = new EvenementCalendrier();
                    evt.setId(r.get("id_article", Long.class));
                    evt.setTitre(r.get("nom", String.class));
                    evt.setDebut(r.get("enchere_date_debut", LocalDateTime.class));
                    evt.setFin(r.get("enchere_date_fin", LocalDateTime.class));
                    evt.setCouleur("#6100C1");
                    return evt;
                });
    }

    public List<EvenementCalendrier> findArticlesByVendeurAndStatut(String email, Integer idArticle) {
        return dsl.select(
                        field("a.id_article").as("id_article"),
                        field("a.nom").as("nom"),
                        field("m.date_visite").as("enchere_date_debut"),
                        field("m.date_visite_fin").as("enchere_date_fin"),
                        field("m.nom").as("nom_personne"),
                        field("m.prenom").as("prenom_personne")
                )
                .from(table("demande_visite").as("m"))
                .join(table("article").as("a"))
                .on(field("m.ref_article").eq(field("a.id_article")))
                .join(table("personne").as("p"))
                .on(field("m.ref_vendeur").eq(field("p.id_personne")))
                .where(field("m.date_visite").isNotNull())
                .and(field("a.id_article").eq(idArticle))
                .fetch()
                .map(r -> {
                    EvenementCalendrier evt = new EvenementCalendrier();
                    evt.setId(r.get("id_article", Long.class));
                    evt.setTitre(r.get("nom", String.class) + " Personne : " + r.get("nom_personne", String.class) +  " " +
                            r.get("prenom_personne", String.class));
                    evt.setDebut(r.get("enchere_date_debut", LocalDateTime.class));
                    evt.setFin(r.get("enchere_date_fin", LocalDateTime.class));
                    evt.setCouleur("#6100C1");
                    return evt;
                });
    }
}