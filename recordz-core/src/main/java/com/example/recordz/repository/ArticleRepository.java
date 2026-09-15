package com.example.recordz.repository;

import com.example.recordz.model.domain.Article;
import com.example.recordz.model.domain.FiltreArticle;
import com.example.recordz.model.domain.Personne;
import com.example.recordz.model.domain.dto.ArticleFormData;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

@Repository
public class ArticleRepository {

    private final DSLContext dsl;

    public ArticleRepository(DSLContext dsl) {

        this.dsl = dsl;
    }

    // ── Liste partagée des champs article avec dates castées (sans alias de table) ──
    private org.jooq.Field<?>[] articleFields() {
        return new org.jooq.Field<?>[] {
                field("id_article"), field("nom"), field("marque"), field("label"),
                field("prix"),
                field("pub"),
                field("pochette"),
                field("pochette2"),
                field("presound"), field("ref_genre"),
                field("etat"), field("ref_categorie"), field("ref_subcategorie"),
                field("quantite"), field("owned"), field("ref_statut"), field("ref_depot"),
                field("enchere"), field("ref_condition_payement"),
                field("ref_mode_de_livraison"),
                field("vendu"), field("visites"), field("nbr_enchere"), field("lang"),
                field("ref_canton"), field("lieu"), field("adresse"), field("npa"),
                field("ref_pays"), field("link_youtube"), field("nb_cheveaux"),
                field("nb_cylindre"), field("nb_km"), field("annee"),
                field("premiere_immatriculation"), field("essence_ou_diesel"),
                field("ref_boite_de_vitesse"), field("clima"), field("nb_piece"),
                field("surface_habitable"), field("superficie_terrain"),
                field("annee_construction"), field("ref_location_ou_achat"),
                field("ref_departement"), field("ref_cepage"), field("tx"),
                field("ref_type_de_vin"), field("ref_pays_region_vin"), field("acteurs"),
                field("realisateur"), field("auteur"), field("ref_type_de_jeux"),
                field("ref_type_ecran"), field("dimension"), field("processeur"),
                field("ram"), field("disque_dur"), field("taille"),
                // ✅ Dates castées pour éviter "Zero date value prohibited"
                field("NULLIF(CAST(date AS CHAR), '0000-00-00 00:00:00')").as("date"),
                field("NULLIF(CAST(enchere_date_debut AS CHAR), '0000-00-00 00:00:00')").as("enchere_date_debut"),
                field("NULLIF(CAST(enchere_date_fin AS CHAR), '0000-00-00 00:00:00')").as("enchere_date_fin")
        };
    }

    // ── Liste des champs avec alias de table "a" + .as() pour toArticle ──────
    private org.jooq.Field<?>[] articleFieldsWithAlias() {
        return new org.jooq.Field<?>[] {
                field("a.id_article").as("id_article"),
                field("a.nom").as("nom"),
                field("a.marque").as("marque"),
                field("a.label").as("label"),
                field("a.prix").as("prix"),
                field("a.pochette").as("pochette"),
                field("a.pochette2").as("pochette2"),
                field("a.presound").as("presound"),
                field("a.ref_genre").as("ref_genre"),
                field("a.etat").as("etat"),
                field("a.ref_categorie").as("ref_categorie"),
                field("a.ref_subcategorie").as("ref_subcategorie"),
                field("a.quantite").as("quantite"),
                field("a.owned").as("owned"),
                field("a.ref_statut").as("ref_statut"),
                field("a.ref_depot").as("ref_depot"),
                field("a.enchere").as("enchere"),
                field("a.ref_condition_payement").as("ref_condition_payement"),
                field("a.ref_mode_de_livraison").as("ref_mode_de_livraison"),
                field("a.vendu").as("vendu"),
                field("a.visites").as("visites"),
                field("a.nbr_enchere").as("nbr_enchere"),
                field("a.lang").as("lang"),
                field("a.ref_canton").as("ref_canton"),
                field("a.lieu").as("lieu"),
                field("a.adresse").as("adresse"),
                field("a.npa").as("npa"),
                field("a.ref_pays").as("ref_pays"),
                field("a.link_youtube").as("link_youtube"),
                field("a.nb_cheveaux").as("nb_cheveaux"),
                field("a.nb_cylindre").as("nb_cylindre"),
                field("a.nb_km").as("nb_km"),
                field("a.annee").as("annee"),
                field("a.premiere_immatriculation").as("premiere_immatriculation"),
                field("a.essence_ou_diesel").as("essence_ou_diesel"),
                field("a.ref_boite_de_vitesse").as("ref_boite_de_vitesse"),
                field("a.clima").as("clima"),
                field("a.nb_piece").as("nb_piece"),
                field("a.surface_habitable").as("surface_habitable"),
                field("a.superficie_terrain").as("superficie_terrain"),
                field("a.annee_construction").as("annee_construction"),
                field("a.ref_location_ou_achat").as("ref_location_ou_achat"),
                field("a.ref_departement").as("ref_departement"),
                field("a.ref_cepage").as("ref_cepage"),
                field("a.tx").as("tx"),
                field("a.ref_type_de_vin").as("ref_type_de_vin"),
                field("a.ref_pays_region_vin").as("ref_pays_region_vin"),
                field("a.acteurs").as("acteurs"),
                field("a.realisateur").as("realisateur"),
                field("a.auteur").as("auteur"),
                field("a.ref_type_de_jeux").as("ref_type_de_jeux"),
                field("a.ref_type_ecran").as("ref_type_ecran"),
                field("a.dimension").as("dimension"),
                field("a.processeur").as("processeur"),
                field("a.ram").as("ram"),
                field("a.disque_dur").as("disque_dur"),
                field("a.taille").as("taille"),
                // ✅ Dates castées avec alias sans préfixe pour toArticle
                field("NULLIF(CAST(a.date AS CHAR), '0000-00-00 00:00:00')").as("date"),
                field("NULLIF(CAST(a.enchere_date_debut AS CHAR), '0000-00-00 00:00:00')").as("enchere_date_debut"),
                field("NULLIF(CAST(a.enchere_date_fin AS CHAR), '0000-00-00 00:00:00')").as("enchere_date_fin")
        };
    }

    // ── Méthode privée partagée — construit la requête de base ───────────────
    private org.jooq.SelectConditionStep<?> buildFiltresQuery(FiltreArticle f) {
        var query = dsl.select(articleFields())
                .from(table("article"))
                .where(field("vendu").eq(0));

        if (f.refCategorie     != null) query = query.and(field("ref_categorie").eq(f.refCategorie));
        if (f.refSousCategorie != null) query = query.and(field("ref_subcategorie").eq(f.refSousCategorie));
        if (f.nom    != null && !f.nom.isBlank())    query = query.and(field("nom").like("%" + f.nom + "%"));
        if (f.marque != null && !f.marque.isBlank()) query = query.and(field("marque").like("%" + f.marque + "%"));
        if (f.taille != null && !f.taille.isBlank()) query = query.and(field("taille").like("%" + f.taille + "%"));
        if (f.prixMin != null) query = query.and(field("prix").greaterOrEqual(f.prixMin));
        if (f.prixMax != null) query = query.and(field("prix").lessOrEqual(f.prixMax));
        if (f.encheresOnly)    query = query.and(field("enchere").eq(1));
        if (f.nbKmMax           != null) query = query.and(field("nb_km").lessOrEqual(f.nbKmMax));
        if (f.anneeMin          != null) query = query.and(field("annee").greaterOrEqual(f.anneeMin));
        if (f.anneeMax          != null) query = query.and(field("annee").lessOrEqual(f.anneeMax));
        if (f.nbCheveauxMin     != null) query = query.and(field("nb_cheveaux").greaterOrEqual(f.nbCheveauxMin));
        if (f.refEssence        != null) query = query.and(field("essence_ou_diesel").eq(f.refEssence));
        if (f.refBoiteDeVitesse != null) query = query.and(field("ref_boite_de_vitesse").eq(f.refBoiteDeVitesse));
        if (f.nbPieceMin != null) query = query.and(field("nb_piece").greaterOrEqual(f.nbPieceMin));
        if (f.surfaceMin != null) query = query.and(field("surface_habitable").greaterOrEqual(f.surfaceMin));
        if (f.surfaceMax != null) query = query.and(field("surface_habitable").lessOrEqual(f.surfaceMax));
        if (f.lieu != null && !f.lieu.isBlank()) query = query.and(field("lieu").like("%" + f.lieu + "%"));
        if (f.npa  != null && !f.npa.isBlank())  query = query.and(field("npa").eq(f.npa));
        if (f.millesimeMin != null) query = query.and(field("tx").greaterOrEqual(String.valueOf(f.millesimeMin)));
        if (f.millesimeMax != null) query = query.and(field("tx").lessOrEqual(String.valueOf(f.millesimeMax)));
        if (f.refTypeDeVin != null) query = query.and(field("ref_type_de_vin").eq(f.refTypeDeVin));
        if (f.realisateur != null && !f.realisateur.isBlank()) query = query.and(field("realisateur").like("%" + f.realisateur + "%"));
        if (f.acteurs     != null && !f.acteurs.isBlank())     query = query.and(field("acteurs").like("%" + f.acteurs + "%"));
        if (f.auteur  != null && !f.auteur.isBlank())  query = query.and(field("auteur").like("%" + f.auteur + "%"));
        if (f.editeur != null && !f.editeur.isBlank()) query = query.and(field("marque").like("%" + f.editeur + "%"));
        if (f.refTypeDeJeux != null) query = query.and(field("ref_type_de_jeux").eq(f.refTypeDeJeux));
        if (f.dimensionMin  != null) query = query.and(field("dimension").greaterOrEqual(f.dimensionMin));
        if (f.dimensionMax  != null) query = query.and(field("dimension").lessOrEqual(f.dimensionMax));
        if (f.refTypeEcran  != null) query = query.and(field("ref_type_ecran").eq(f.refTypeEcran));
        if (f.ram != null && !f.ram.isBlank()) query = query.and(field("ram").eq(f.ram));
        if (f.processeurMin != null && !f.processeurMin.isBlank())
            query = query.and(field("processeur").greaterOrEqual(new java.math.BigDecimal(f.processeurMin)));

        return query;
    }

    public List<Article> findByFiltresDynamiques(FiltreArticle f) {
        int page     = f.page     > 0 ? f.page     : 1;
        int pageSize = f.pageSize > 0 ? f.pageSize : 10;

        return buildFiltresQuery(f)
                .orderBy(field("id_article").desc())
                .limit(pageSize)
                .offset((page - 1) * pageSize)
                .fetch()
                .map(this::toArticle);
    }

    // ✅ COUNT direct pour compatibilité MariaDB
    public int countByFiltresDynamiques(FiltreArticle f) {
        var query = dsl.selectCount()
                .from(table("article"))
                .where(field("vendu").eq(0));

        if (f.refCategorie     != null) query = query.and(field("ref_categorie").eq(f.refCategorie));
        if (f.refSousCategorie != null) query = query.and(field("ref_subcategorie").eq(f.refSousCategorie));
        if (f.nom    != null && !f.nom.isBlank())    query = query.and(field("nom").like("%" + f.nom + "%"));
        if (f.marque != null && !f.marque.isBlank()) query = query.and(field("marque").like("%" + f.marque + "%"));
        if (f.taille != null && !f.taille.isBlank()) query = query.and(field("taille").like("%" + f.taille + "%"));
        if (f.prixMin != null) query = query.and(field("prix").greaterOrEqual(f.prixMin));
        if (f.prixMax != null) query = query.and(field("prix").lessOrEqual(f.prixMax));
        if (f.encheresOnly)    query = query.and(field("enchere").eq(1));
        if (f.nbKmMax           != null) query = query.and(field("nb_km").lessOrEqual(f.nbKmMax));
        if (f.anneeMin          != null) query = query.and(field("annee").greaterOrEqual(f.anneeMin));
        if (f.anneeMax          != null) query = query.and(field("annee").lessOrEqual(f.anneeMax));
        if (f.nbCheveauxMin     != null) query = query.and(field("nb_cheveaux").greaterOrEqual(f.nbCheveauxMin));
        if (f.refEssence        != null) query = query.and(field("essence_ou_diesel").eq(f.refEssence));
        if (f.refBoiteDeVitesse != null) query = query.and(field("ref_boite_de_vitesse").eq(f.refBoiteDeVitesse));
        if (f.nbPieceMin != null) query = query.and(field("nb_piece").greaterOrEqual(f.nbPieceMin));
        if (f.surfaceMin != null) query = query.and(field("surface_habitable").greaterOrEqual(f.surfaceMin));
        if (f.surfaceMax != null) query = query.and(field("surface_habitable").lessOrEqual(f.surfaceMax));
        if (f.lieu != null && !f.lieu.isBlank()) query = query.and(field("lieu").like("%" + f.lieu + "%"));
        if (f.npa  != null && !f.npa.isBlank())  query = query.and(field("npa").eq(f.npa));
        if (f.millesimeMin != null) query = query.and(field("tx").greaterOrEqual(String.valueOf(f.millesimeMin)));
        if (f.millesimeMax != null) query = query.and(field("tx").lessOrEqual(String.valueOf(f.millesimeMax)));
        if (f.refTypeDeVin != null) query = query.and(field("ref_type_de_vin").eq(f.refTypeDeVin));
        if (f.realisateur != null && !f.realisateur.isBlank()) query = query.and(field("realisateur").like("%" + f.realisateur + "%"));
        if (f.acteurs     != null && !f.acteurs.isBlank())     query = query.and(field("acteurs").like("%" + f.acteurs + "%"));
        if (f.auteur  != null && !f.auteur.isBlank())  query = query.and(field("auteur").like("%" + f.auteur + "%"));
        if (f.editeur != null && !f.editeur.isBlank()) query = query.and(field("marque").like("%" + f.editeur + "%"));
        if (f.refTypeDeJeux != null) query = query.and(field("ref_type_de_jeux").eq(f.refTypeDeJeux));
        if (f.dimensionMin  != null) query = query.and(field("dimension").greaterOrEqual(f.dimensionMin));
        if (f.dimensionMax  != null) query = query.and(field("dimension").lessOrEqual(f.dimensionMax));
        if (f.refTypeEcran  != null) query = query.and(field("ref_type_ecran").eq(f.refTypeEcran));
        if (f.ram != null && !f.ram.isBlank()) query = query.and(field("ram").eq(f.ram));
        if (f.processeurMin != null && !f.processeurMin.isBlank())
            query = query.and(field("processeur").greaterOrEqual(new java.math.BigDecimal(f.processeurMin)));

        return query.fetchOne(0, Integer.class);
    }

    public int countByCategorie(int refCategorie) {
        return dsl.fetchCount(
                dsl.selectFrom(table("article"))
                        .where(field("ref_categorie").eq(refCategorie))
                        .and(field("vendu").eq(0))
        );
    }

    public List<Article> findAll(int limit, int offset) {
        return dsl.select(articleFields())
                .from(table("article"))
                .where(field("vendu").eq(0))
                .orderBy(field("id_article").desc())
                .limit(limit).offset(offset)
                .fetch()
                .map(this::toArticle);
    }

    public List<Article> findAllTries(int limit, int offset) {

        List<Article> articles = dsl.select(articleFields())
                .from(table("article"))
                .where(field("vendu").eq(0))
                .orderBy(field("id_article").desc())
                .limit(limit).offset(offset)
                .fetch()
                .map(this::toArticle);

        // ✅ Trier : pub=1 en premier, puis les autres par id_article desc
        articles.sort(
                Comparator
                        // pub=1 passe avant pub=null ou pub=0
                        .<Article, Integer>comparing(
                                a -> Integer.valueOf(1).equals(a.getPub()) ? 0 : 1
                        )
                        // à égalité de statut pub, trier par id_article décroissant
                        .thenComparing(
                                Comparator.comparing(Article::getIdArticle).reversed()
                        )
        );

        return articles;
    }
    public int countActiveArticlesByVendeur(String email) {
        return dsl.selectCount()
                .from(table("met_en_vente").as("m"))
                .join(table("article").as("a"))
                .on(field("m.ref_article").eq(field("a.id_article")))
                .join(table("personne").as("p"))
                .on(field("m.ref_vendeur").eq(field("p.id_personne")))
                .where(field("p.email").eq(email))
                .and(field("a.ref_statut").eq(1))
                .fetchOne(0, Integer.class);
    }

    public int countMesAchatsByVendeur(String email) {
        return dsl.selectCount()
                .from(table("a_paye").as("m"))
                .join(table("article").as("a"))
                .on(field("m.ref_article").eq(field("a.id_article")))
                .join(table("personne").as("p"))
                .on(field("m.ref_vendeur").eq(field("p.id_personne")))
                .where(field("p.email").eq(email))
                .and(field("m.ref_statut").eq(12))
                .fetchOne(0, Integer.class);
    }

    public List<Article> findArticlesAchetes(String email, int limit, int offset) {
        return dsl.select(articleFieldsWithAlias())
                .from(table("a_paye").as("m"))
                .join(table("article").as("a"))
                .on(field("m.ref_article").eq(field("a.id_article")))
                .join(table("personne").as("p"))
                .on(field("m.ref_acheteur").eq(field("p.id_personne")))

                .where(field("p.email").eq(email))
                .and(field("m.ref_statut").eq(14))
                .limit(limit)
                .offset(offset)
                .fetch()
                .map(this::toArticle);
    }

    public List<Article>  findArticlesVendus (String email, int limit, int offset) {
        return dsl.select(articleFieldsWithAlias())
                .from(table("a_paye").as("m"))
                .join(table("article").as("a"))
                .on(field("m.ref_article").eq(field("a.id_article")))
                .join(table("personne").as("p"))
                .on(field("m.ref_vendeur").eq(field("p.id_personne")))

                .where(field("p.email").eq(email))
                .and(field("m.ref_statut").eq(14))
                .limit(limit)
                .offset(offset)
                .fetch()
                .map(this::toArticle);
    }

    // ✅ articleFieldsWithAlias() — alias .as() sur chaque champ pour toArticle
    public List<Article> findArticlesByVendeurAndStatut(String email, int limit, int offset, int statut) {
        return dsl.select(articleFieldsWithAlias())
                .from(table("met_en_vente").as("m"))
                .join(table("article").as("a"))
                .on(field("m.ref_article").eq(field("a.id_article")))
                .join(table("personne").as("p"))
                .on(field("m.ref_vendeur").eq(field("p.id_personne")))
                .where(field("p.email").eq(email))
                .and(field("a.ref_statut").eq(statut))
                .limit(limit)
                .offset(offset)
                .fetch()
                .map(this::toArticle);
    }

    public List<Article> findArticlesAchetesByVendeur(String email, int limit, int offset) {
        return dsl.select(
                        table("a").asterisk(),
                        field("m.ref_vendeur")
                )
                .from(table("met_en_vente").as("m"))
                .join(table("article").as("a"))
                .on(field("m.ref_article").eq(field("a.id_article")))
                .join(table("personne").as("p"))
                .on(field("m.ref_vendeur").eq(field("p.id_personne")))
                .where(field("p.email").eq(email))
                .and(field("a.vendu").eq(1))
                .limit(limit)
                .offset(offset)
                .fetch()
                .map(this::toArticle);
    }

    // ✅ articleFieldsWithAlias() — alias .as() sur chaque champ pour toArticle
    public List<Article> findActiveArticlesByVendeur(String email, int limit, int offset) {
        return dsl.select(articleFieldsWithAlias())
                .from(table("met_en_vente").as("m"))
                .join(table("article").as("a"))
                .on(field("m.ref_article").eq(field("a.id_article")))
                .join(table("personne").as("p"))
                .on(field("m.ref_vendeur").eq(field("p.id_personne")))
                .where(field("p.email").eq(email))
                .and(field("a.ref_statut").eq(1))
                .limit(limit)
                .offset(offset)
                .fetch()
                .map(this::toArticle);
    }


    public List<Article> findActiveArticlesByVendeurSansPub(String email, int limit, int offset) {
        return dsl.select(articleFieldsWithAlias())
                .from(table("met_en_vente").as("m"))
                .join(table("article").as("a"))
                .on(field("m.ref_article").eq(field("a.id_article")))
                .join(table("personne").as("p"))
                .on(field("m.ref_vendeur").eq(field("p.id_personne")))
                .where(field("p.email").eq(email))
                .and(field("a.ref_statut").eq(1))          // ✅ garde uniquement eq(1)
                .and(
                        field("a.pub").isNull()                // pub IS NULL
                                .or(field("a.pub").eq(0))              // OU pub = 0
                )
                .limit(limit)
                .offset(offset)
                .fetch()
                .map(this::toArticle);
    }
    public List<Article> findArticlesAPaye(String email, int page, int pageSize) {

        return dsl.select(
                        field("article.id_article"),
                        field("article.pochette"),
                        field("article.pochette2"),
                        field("article.nom"),
                        field("article.marque"),
                        field("article.prix"),
                        field("a_paye.ref_vendeur"),
                        field("a_paye.ref_acheteur"),  // ✅ ajout
                        field("a_paye.id_a_paye"),
                        field("article.ref_mode_de_livraison"),   // ✅ ajout
                        field("article.ref_condition_payement")   // ✅ ajout
                )
                .from(
                        table("article"),
                        table("a_paye"),
                        table("personne")
                )
                .where(field("a_paye.ref_article").eq(field("article.id_article")))
                .and(field("a_paye.ref_acheteur").eq(field("personne.id_personne")))
                .and(field("personne.email").eq(email))
                .and(field("a_paye.ref_statut").eq(8)) //Juste
                .limit(pageSize)               // doit être 10
                .offset((page - 1) * pageSize) // doit être 0
                .fetch()
                .map(r -> {
                    Article a = new Article();
                    a.setIdArticle(           r.get(field("article.id_article"),              Long.class));
                    a.setNom(                 r.get(field("article.nom"),                     String.class));
                    a.setPochette(            r.get(field("article.pochette"),                String.class));
                    a.setPochette2(            r.get(field("article.pochette2"),                String.class));
                    a.setMarque(              r.get(field("article.marque"),                  String.class));
                    a.setPrix(                r.get(field("article.prix"),                    Double.class));
                    a.setRefVendeur(          r.get(field("a_paye.ref_vendeur"),              Integer.class));
                    a.setRefAcheteur(         r.get(field("a_paye.ref_acheteur"),             Long.class)); // ✅
                    a.setRefModeDeLivraison(  r.get(field("article.ref_mode_de_livraison"),   Integer.class)); // ✅
                    a.setRefConditionPayement(r.get(field("article.ref_condition_payement"),  Integer.class)); // ✅
                    return a;
                });
    }

    public List<Article> findArticlesALivrerEnAttente(String email, int page, int pageSize) {
        return dsl.select(
                        field("article.id_article"),
                        field("article.pochette"),
                        field("article.pochette2"),
                        field("article.nom"),
                        field("a_livre.ref_vendeur"),
                        field("a_livre.ref_acheteur"),
                        field("a_livre.id_a_livre"),
                        field("a_livre.montant"),
                        field("article.prix"),
                        field("article.ref_mode_de_livraison"),
                        field("article.ref_condition_payement"),
                        field("condition_livraison_libelle_langue.ref_mode_de_livraison")
                )
                .from(table("article"))
                .join(table("a_livre"))
                .on(field("a_livre.ref_article").eq(field("article.id_article")))
                .join(table("personne"))
                .on(field("a_livre.ref_acheteur").eq(field("personne.id_personne")))
                .leftJoin(table("condition_livraison_libelle_langue"))
                .on(field("article.ref_mode_de_livraison")
                        .eq(field("condition_livraison_libelle_langue.ref_mode_de_livraison")))
                .where(field("personne.email").eq(email))
                .and(field("a_livre.ref_statut").eq(8)) //Montre tous les articles à livrés
                .and(field("a_livre.ref_mode_de_livraison").equal(4)) // ✅ groupé correctement
                .limit(pageSize)
                .offset((page - 1) * pageSize)
                .fetch()
                .map(r -> {
                    Article a = new Article();
                    a.setIdArticle(           r.get(field("article.id_article"),              Long.class));
                    a.setRefAcheteur(         r.get(field("a_livre.ref_acheteur"),             Long.class));
                    a.setRefVendeur(          r.get(field("a_livre.ref_vendeur"),              Integer.class));
                    a.setNom(                 r.get(field("article.nom"),                     String.class));
                    a.setPochette(            r.get(field("article.pochette"),                String.class));
                    a.setPochette2(            r.get(field("article.pochette2"),                String.class));
                    a.setPrix(               r.get(field("article.prix"),                    Double.class));
                    a.setMontant(            r.get(field("a_livre.montant"),                  BigDecimal.class));
                    a.setRefModeDeLivraison( r.get(field("article.ref_mode_de_livraison"),   Integer.class));
                    a.setRefConditionPayement(r.get(field("article.ref_condition_payement"), Integer.class));
                    return a;
                });
    }

    public List<Article> findArticlesALivrerADomicile(String email, int page, int pageSize) {
        return dsl.select(
                        field("article.id_article"),
                        field("article.pochette"),
                        field("article.pochette2"),
                        field("article.nom"),
                        field("a_livre.ref_vendeur"),
                        field("a_livre.ref_acheteur"),
                        field("a_livre.id_a_livre"),
                        field("a_livre.montant"),
                        field("article.prix"),
                        field("article.ref_mode_de_livraison"),
                        field("article.ref_condition_payement"),
                        field("condition_livraison_libelle_langue.ref_mode_de_livraison")
                )
                .from(table("article"))
                .join(table("a_livre"))
                .on(field("a_livre.ref_article").eq(field("article.id_article")))
                .join(table("personne"))
                .on(field("a_livre.ref_vendeur").eq(field("personne.id_personne")))
                .leftJoin(table("condition_livraison_libelle_langue"))
                .on(field("article.ref_mode_de_livraison")
                        .eq(field("condition_livraison_libelle_langue.ref_mode_de_livraison")))
                .where(field("personne.email").eq(email))
                .and(field("a_livre.ref_statut").eq(8)) //Montre tous les articles à livrés
                .and(field("a_livre.ref_mode_de_livraison").equal(4)) // ✅ groupé correctement
                .limit(pageSize)
                .offset((page - 1) * pageSize)
                .fetch()
                .map(r -> {
                    Article a = new Article();
                    a.setIdArticle(           r.get(field("article.id_article"),              Long.class));
                    a.setRefAcheteur(         r.get(field("a_livre.ref_acheteur"),             Long.class));
                    a.setRefVendeur(          r.get(field("a_livre.ref_vendeur"),              Integer.class));
                    a.setNom(                 r.get(field("article.nom"),                     String.class));
                    a.setPochette(            r.get(field("article.pochette"),                String.class));
                    a.setPochette2(            r.get(field("article.pochette2"),                String.class));
                    a.setPrix(               r.get(field("article.prix"),                    Double.class));
                    a.setMontant(            r.get(field("a_livre.montant"),                  BigDecimal.class));
                    a.setRefModeDeLivraison( r.get(field("article.ref_mode_de_livraison"),   Integer.class));
                    a.setRefConditionPayement(r.get(field("article.ref_condition_payement"), Integer.class));
                    return a;
                });
    }
    public List<Article>  findArticlesALivrerADomicilePourMoi(String email, int page, int pageSize) {
        return dsl.select(
                        field("article.id_article"),
                        field("article.pochette"),
                        field("article.pochette2"),
                        field("article.nom"),
                        field("a_livre.ref_vendeur"),
                        field("a_livre.ref_acheteur"),
                        field("a_livre.id_a_livre"),
                        field("a_livre.montant"),
                        field("article.prix"),
                        field("article.ref_mode_de_livraison"),
                        field("article.ref_condition_payement"),
                        field("condition_livraison_libelle_langue.ref_mode_de_livraison")
                )
                .from(table("article"))
                .join(table("a_livre"))
                .on(field("a_livre.ref_article").eq(field("article.id_article")))
                .join(table("personne"))
                .on(field("a_livre.ref_acheteur").eq(field("personne.id_personne")))
                .leftJoin(table("condition_livraison_libelle_langue"))
                .on(field("article.ref_mode_de_livraison")
                        .eq(field("condition_livraison_libelle_langue.ref_mode_de_livraison")))
                .where(field("personne.email").eq(email))
                .and(field("a_livre.ref_statut").eq(8)) //Montre tous les articles à livrés
                .and(field("a_livre.ref_mode_de_livraison").equal(4)) // ✅ groupé correctement
                .limit(pageSize)
                .offset((page - 1) * pageSize)
                .fetch()
                .map(r -> {
                    Article a = new Article();
                    a.setIdArticle(           r.get(field("article.id_article"),              Long.class));
                    a.setRefAcheteur(         r.get(field("a_livre.ref_acheteur"),             Long.class));
                    a.setRefVendeur(          r.get(field("a_livre.ref_vendeur"),              Integer.class));
                    a.setNom(                 r.get(field("article.nom"),                     String.class));
                    a.setPochette(            r.get(field("article.pochette"),                String.class));
                    a.setPochette2(            r.get(field("article.pochette2"),                String.class));
                    a.setPrix(               r.get(field("article.prix"),                    Double.class));
                    a.setMontant(            r.get(field("a_livre.montant"),                  BigDecimal.class));
                    a.setRefModeDeLivraison( r.get(field("article.ref_mode_de_livraison"),   Integer.class));
                    a.setRefConditionPayement(r.get(field("article.ref_condition_payement"), Integer.class));
                    return a;
                });
    }


    public List<Article>  findArticlesALivrerPourMoiParPoste(String email, int page, int pageSize) {
        return dsl.select(
                        field("article.id_article"),
                        field("article.pochette"),
                        field("article.pochette2"),
                        field("article.nom"),
                        field("a_livre.ref_vendeur"),
                        field("a_livre.ref_acheteur"),
                        field("a_livre.id_a_livre"),
                        field("a_livre.montant"),
                        field("article.prix"),
                        field("article.ref_mode_de_livraison"),
                        field("article.ref_condition_payement"),
                        field("condition_livraison_libelle_langue.ref_mode_de_livraison")
                )
                .from(table("article"))
                .join(table("a_livre"))
                .on(field("a_livre.ref_article").eq(field("article.id_article")))
                .join(table("personne"))
                .on(field("a_livre.ref_acheteur").eq(field("personne.id_personne")))
                .leftJoin(table("condition_livraison_libelle_langue"))
                .on(field("article.ref_mode_de_livraison")
                        .eq(field("condition_livraison_libelle_langue.ref_mode_de_livraison")))
                .where(field("personne.email").eq(email))
                .and(field("a_livre.ref_statut").eq(8)) //Montre tous les articles à livrés
                .and(field("a_livre.ref_mode_de_livraison").in(1,2))
                .limit(pageSize)
                .offset((page - 1) * pageSize)
                .fetch()
                .map(r -> {
                    Article a = new Article();
                    a.setIdArticle(           r.get(field("article.id_article"),              Long.class));
                    a.setRefAcheteur(         r.get(field("a_livre.ref_acheteur"),             Long.class));
                    a.setRefVendeur(          r.get(field("a_livre.ref_vendeur"),              Integer.class));
                    a.setNom(                 r.get(field("article.nom"),                     String.class));
                    a.setPochette(            r.get(field("article.pochette"),                String.class));
                    a.setPochette2(            r.get(field("article.pochette2"),                String.class));
                    a.setPrix(               r.get(field("article.prix"),                    Double.class));
                    a.setMontant(            r.get(field("a_livre.montant"),                  BigDecimal.class));
                    a.setRefModeDeLivraison( r.get(field("article.ref_mode_de_livraison"),   Integer.class));
                    a.setRefConditionPayement(r.get(field("article.ref_condition_payement"), Integer.class));
                    return a;
                });
    }


    public List<Article>  findArticlesALivrerParPoste(String email, int page, int pageSize) {
        return dsl.selectDistinct(
                        field("article.id_article"),
                        field("article.pochette"),
                        field("article.pochette2"),
                        field("article.nom"),
                        field("a_livre.ref_vendeur"),
                        field("a_livre.ref_acheteur"),
                        field("a_livre.id_a_livre"),
                        field("a_livre.montant"),
                        field("article.prix"),
                        field("article.ref_mode_de_livraison"),
                        field("article.ref_condition_payement"),
                        field("condition_livraison_libelle_langue.ref_mode_de_livraison")
                )
                .from(table("article"))
                .join(table("a_livre"))
                .on(field("a_livre.ref_article").eq(field("article.id_article")))
                .join(table("personne"))
                .on(field("a_livre.ref_vendeur").eq(field("personne.id_personne")))
                .leftJoin(table("condition_livraison_libelle_langue"))
                .on(field("article.ref_mode_de_livraison")
                        .eq(field("condition_livraison_libelle_langue.ref_mode_de_livraison")))
                .where(field("personne.email").eq(email))
                .and(field("a_livre.ref_statut").eq(8)) //Montre tous les articles à livrés
                .and(field("a_livre.ref_mode_de_livraison").in(1,2))
                .limit(pageSize)
                .offset((page - 1) * pageSize)
                .fetch()
                .map(r -> {
                    Article a = new Article();
                    a.setIdArticle(           r.get(field("article.id_article"),              Long.class));
                    a.setRefAcheteur(         r.get(field("a_livre.ref_acheteur"),             Long.class));
                    a.setRefVendeur(          r.get(field("a_livre.ref_vendeur"),              Integer.class));
                    a.setNom(                 r.get(field("article.nom"),                     String.class));
                    a.setPochette(            r.get(field("article.pochette"),                String.class));
                    a.setPochette2(            r.get(field("article.pochette2"),                String.class));
                    a.setPrix(               r.get(field("article.prix"),                    Double.class));
                    a.setMontant(            r.get(field("a_livre.montant"),                  BigDecimal.class));
                    a.setRefModeDeLivraison( r.get(field("article.ref_mode_de_livraison"),   Integer.class));
                    a.setRefConditionPayement(r.get(field("article.ref_condition_payement"), Integer.class));
                    return a;
                });
    }

    public List<Article> findArticlesAllerChercher(String email, int limit, int offset) {
        return dsl.select(
                        field("article.id_article"),
                        field("article.pochette"),
                        field("article.pochette2"),
                        field("article.nom"),
                        field("a_livre.ref_vendeur"),
                        field("a_livre.ref_acheteur"),       // ✅ ajout
                        field("a_livre.id_a_livre"),
                        field("a_livre.montant"),            // ✅ ajout
                        field("article.prix"),               // ✅ ajout
                        field("article.ref_mode_de_livraison"),       // ✅ ajout
                        field("article.ref_condition_payement"),      // ✅ ajout
                        field("condition_livraison_libelle_langue.ref_mode_de_livraison")
                )
                .from(
                        table("article"),
                        table("a_livre"),
                        table("personne"),
                        table("condition_livraison_libelle_langue")
                )
                .where(field("a_livre.ref_article").eq(field("article.id_article")))
                .and(field("a_livre.ref_acheteur").eq(field("personne.id_personne")))
                .and(field("personne.email").eq(email))
                .and(
                        field("a_livre.ref_statut").eq(8)
                )
                .and(field("article.ref_mode_de_livraison").equal(3))
                .and(field("article.ref_mode_de_livraison")
                .eq(field("condition_livraison_libelle_langue.ref_mode_de_livraison")))
                .limit(50)
                .offset(0)
                .fetch()
                .map(r -> {
                    Article a = new Article();
                    a.setIdArticle(           r.get(field("article.id_article"),              Long.class));
                    a.setRefAcheteur(         r.get(field("a_livre.ref_acheteur"),            Long.class));
                    a.setRefVendeur(          r.get(field("a_livre.ref_vendeur"),             Integer.class));
                    a.setNom(                 r.get(field("article.nom"),                     String.class));
                    a.setPochette(            r.get(field("article.pochette"),                String.class));
                    a.setPochette2(            r.get(field("article.pochette2"),                    String.class));
                    a.setPrix(                r.get(field("article.prix"),                    Double.class));
                    a.setMontant(             r.get(field("a_livre.montant"),                 BigDecimal.class)); // ✅
                    a.setRefModeDeLivraison(  r.get(field("article.ref_mode_de_livraison"),   Integer.class));   // ✅
                    a.setRefConditionPayement(r.get(field("article.ref_condition_payement"),  Integer.class));   // ✅
                    return a;                });
    }
    // ✅ Parenthèses manquantes corrigées sur ref_vendeur et ref_acheteur
    public void updateStatutEstLivre(Integer idArticle, Article article, Personne personne) {
        dsl.update(table("a_livre"))
                .set(field("ref_vendeur"),           article.getRefVendeur())
                .set(field("ref_acheteur"),          article.getRefAcheteur())
                .set(field("montant"),               article.getPrix())
                .set(field("date_reception"),        LocalDateTime.now())
                .set(field("ref_mode_de_livraison"), article.getRefModeDeLivraison())
                .set(field("ref_statut"),            12)
                .set(field("quantite"),              1)
                .where(field("ref_article").eq(idArticle))
                .and(field("ref_acheteur").eq(article.getRefAcheteur()))
                .execute();
    }

    public void updateFaitDeLapublicite(Integer idArticle) {
        dsl.update(table("article"))
                .set(field("pub"), 1)
                .where(field("id_article").eq(idArticle))
                .execute();
    }

    public void confirmerReception (Integer idArticle, Integer refAchteur) {
        dsl.update(table("a_livre"))
                .set(field("ref_statut"), 12)
                .set(field("date_achat"), LocalDateTime.now())
                .where(field("ref_article").eq(idArticle))
                .and(field  ("ref_acheteur").eq(refAchteur))
                .execute();

    }
    public void updateStatutPaye(Integer idArticle, Article article) {
        dsl.update(table("a_paye"))
                .set(field("ref_statut"), 7)
                .where(field("ref_article").eq(idArticle))
                .and(field("ref_vendeur").eq(article.getRefVendeur()))
                .execute();
        dsl.insertInto(table("a_livre"))
                .set(field("ref_article"),           idArticle)
                .set(field("ref_statut"),            8)
                .set(field("ref_vendeur"),           article.getRefVendeur())
                .set(field("date_achat"),            LocalDateTime.now())
                .set(field("ref_acheteur"),          article.getRefAcheteur())
                .set(field("ref_mode_de_livraison"), article.getRefModeDeLivraison())
                .set(field("montant"),               article.getMontant())
                .execute();
    }

    public void insertAPaye(
            Integer refArticle,
            Integer refVendeur,
            Long refAcheteur,
            BigDecimal montant,
            LocalDateTime dateFermetureEnchere,
            Integer refModeDeLivraison,
            Integer refConditionPayement,
            Integer quantite,
            Integer refCanton
    ) {
        dsl.update(table("a_paye"))
                .set(field("ref_vendeur"),           refVendeur)
                .set(field("ref_acheteur"),          refAcheteur)
                .set(field("montant"),               montant)
                .set(field("date_fermeture_enchere"),dateFermetureEnchere)
                .set(field("ref_mode_de_livraison"), refModeDeLivraison)
                .set(field("ref_statut"),            14)
                .set(field("ref_condition_payement"),refConditionPayement)
                .set(field("quantite"),              quantite)
                .set(field("ref_canton"),            refCanton)
                .where(field("ref_article").eq(refArticle))
                .and(field("ref_acheteur").eq(refAcheteur))
                .execute();

        /*
        dsl.insertInto(table("a_livre"))
                .set(field("ref_article"),           refArticle)
                .set(field("ref_statut"),            8)
                .set(field("ref_vendeur"),           refVendeur)
                .set(field("date_achat"),            dateFermetureEnchere)
                .set(field("ref_acheteur"),          refAcheteur)
                .set(field("ref_mode_de_livraison"), refModeDeLivraison)
                .set(field("montant"),               montant)
                .execute();

         */
    }

    public int getMaxIdEnchere(int refArticle) {
        return dsl.select(max(field("id_enchere", Integer.class)))
                .from(table("enchere"))
                .where(field("ref_article").eq(refArticle))
                .fetchOne(max(field("id_enchere", Integer.class)));
    }

    public Integer findRefUtilisateurByEmail(String email) {
        return dsl.select(field("id_personne", Integer.class))
                .from(table("personne"))
                .where(field("email").eq(email))
                .fetchOneInto(Integer.class);
    }

    public List<Article> findArticleAttenteDePayement(String email, int limit, int offset) {
        return dsl.selectDistinct(
                        field("article.pochette"),
                        field("article.pochette2"),
                        field("article.nom"),
                        field("article.prix"),
                        field("article.marque"),
                        field("article.id_article"),
                        field("article.ref_mode_de_livraison"),
                        field("article.ref_condition_payement"),
                        field("article.ref_canton"),
                        field("article.quantite"),
                        field("article.prix_achat"),
                        field("a_paye.id_a_paye"),
                        field("a_paye.ref_acheteur"),
                        field("a_paye.ref_vendeur")
                )
                .from(table("article"))
                .join(table("a_paye"))
                .on(field("a_paye.ref_article").eq(field("article.id_article")))
                .join(table("personne"))
                .on(field("a_paye.ref_vendeur").eq(field("personne.id_personne")))
                .where(field("personne.email").eq(email))
                //.and(field("a_paye.ref_statut").eq(7))
                .and(field("a_paye.ref_statut").in(7, 8)) //anciennement 12 lors de l insertion c'est egal a 12
                .orderBy(field("article.id_article").desc())
                .limit(limit)
                .offset(offset)
                .fetch()
                .map(r -> {
                    Article a = new Article();
                    a.setIdArticle(            r.get(field("article.id_article"),             Long.class));
                    a.setNom(                  r.get(field("article.nom"),                    String.class));
                    a.setPochette(             r.get(field("article.pochette"),               String.class));
                    a.setPochette2(             r.get(field("article.pochette2"),               String.class));
                    a.setPrix(                 r.get(field("article.prix"),                   Double.class));
                    a.setMarque(               r.get(field("article.marque"),                 String.class));
                    a.setRefAcheteur(          r.get(field("a_paye.ref_acheteur"),            Long.class));
                    a.setRefArticle(           r.get(field("article.id_article"),             Long.class).intValue());
                    a.setRefVendeur(           r.get(field("a_paye.ref_vendeur")     ,        Integer.class));
                    a.setMontant(              r.get(field("article.prix_achat"),             BigDecimal.class));
                    a.setDateFermetureEnchere( LocalDateTime.now());
                    a.setRefModeDeLivraison(   r.get(field("article.ref_mode_de_livraison"),  Integer.class));
                    a.setRefConditionPayement( r.get(field("article.ref_condition_payement"), Integer.class));
                    a.setQuantite(             r.get(field("article.quantite"),               Integer.class));
                    return a;
                });
    }

    public int countAPayerByUsername(String email) {
        return dsl.selectCount()
                .from(table("article"), table("a_paye"), table("personne")) // ← a_paye
                .where(field("a_paye.ref_article").eq(field("article.id_article")))
                .and(field("a_paye.ref_acheteur").eq(field("personne.id_personne")))
                .and(field("personne.email").eq(email))
                .and(field("a_paye.ref_statut").eq(12))
                .fetchOne(0, Integer.class);
    }

    public int countArticlesVendus(String email) {
        return dsl.selectCount()
                .from(table("article"), table("a_paye"), table("personne")) // ← a_paye
                .where(field("a_paye.ref_article").eq(field("article.id_article")))
                .and(field("a_paye.ref_vendeur").eq(field("personne.id_personne")))
                .and(field("personne.email").eq(email))
                .and(field("a_paye.ref_statut").eq(12))
                .fetchOne(0, Integer.class);

    }


    public int countALivrerByUsername(String email) {
        return dsl.selectCount()
                .from(
                        table("article"),
                        table("a_livre"),
                        table("personne"))
                .where(field("ref_article").eq(field("id_article")))
                .and(field("ref_acheteur").eq(field("id_personne")))
                .and(field("email").eq(email))
                .and(
                        field("a_livre.ref_statut").eq("14")
                                .or(field("a_livre.ref_statut").eq("9"))
                )
                .and(field("article.ref_mode_de_livraison").notEqual(8))
                .and(field("article.ref_mode_de_livraison").notEqual(9))
                .fetchOne(0, Integer.class);
    }

    public int countAllerChercherByUsername(String email) {
        return dsl.selectCount()
                .from(
                        table("article"),
                        table("a_livre"),
                        table("personne"))
                .where(field("ref_article").eq(field("id_article")))
                .and(field("ref_acheteur").eq(field("id_personne")))
                .and(field("email").eq(email))
                .and(
                        field("a_livre.ref_statut").eq("14")
                                .or(field("a_livre.ref_statut").eq("9"))
                )
                .and(field("article.ref_mode_de_livraison").equal(3))
                .fetchOne(0, Integer.class);
    }

    public int countEnAttenteDePayementCounter(String username) {
        return dsl.select(count(field("transaction.id_transaction")))
                .from(table("article"))
                .join(table("transaction"))
                .on(field("transaction.ref_article").eq(field("article.id_article")))
                .join(table("personne"))
                .on(field("transaction.ref_vendeur").eq(field("personne.id_personne")))
                .where(field("personne.email").eq(username))
                .and(field("article.vendu").eq(1))
                .fetchOne(0, Integer.class);
    }

    public int countAll() {
        return dsl.fetchCount(
                dsl.selectFrom(table("article"))
                        .where(field("vendu").eq(0))
        );
    }

    public List<Article> findByFiltres(String nom, String marque, Integer refCategorie,
                                       Integer refSousCategorie, Double prixMin, Double prixMax,
                                       boolean encheresOnly, int limit, int offset) {
        var query = dsl.select(articleFields())
                .from(table("article"))
                .where(field("vendu").eq(0));

        if (refCategorie     != null) query = query.and(field("ref_categorie").eq(refCategorie));
        if (refSousCategorie != null) query = query.and(field("ref_subcategorie").eq(refSousCategorie));
        if (nom    != null && !nom.isBlank())    query = query.and(field("nom").like("%" + nom + "%"));
        if (marque != null && !marque.isBlank()) query = query.and(field("marque").like("%" + marque + "%"));
        if (prixMin != null) query = query.and(field("prix").greaterOrEqual(prixMin));
        if (prixMax != null) query = query.and(field("prix").lessOrEqual(prixMax));
        if (encheresOnly)    query = query.and(field("enchere").eq(1));

        return query
                .orderBy(field("id_article").desc())
                .limit(limit).offset(offset)
                .fetch()
                .map(this::toArticle);
    }

    public int countByFiltres(String nom, String marque, Integer refCategorie,
                              Integer refSousCategorie, Double prixMin, Double prixMax,
                              boolean encheresOnly) {
        var query = dsl.selectCount()
                .from(table("article"))
                .where(field("vendu").eq(0));

        if (refCategorie     != null) query = query.and(field("ref_categorie").eq(refCategorie));
        if (refSousCategorie != null) query = query.and(field("ref_subcategorie").eq(refSousCategorie));
        if (nom    != null && !nom.isBlank())    query = query.and(field("nom").like("%" + nom + "%"));
        if (marque != null && !marque.isBlank()) query = query.and(field("marque").like("%" + marque + "%"));
        if (prixMin != null) query = query.and(field("prix").greaterOrEqual(prixMin));
        if (prixMax != null) query = query.and(field("prix").lessOrEqual(prixMax));
        if (encheresOnly)    query = query.and(field("enchere").eq(1));

        return query.fetchOne(0, Integer.class);
    }

    public Optional<Article> findById(Long id) {
        return dsl.select(articleFields())
                .from(table("article"))
                .where(field("id_article").eq(id))
                .fetchOptional()
                .map(this::toArticle);
    }

    public List<Article> findBySousCategorie(int refCategorie, int refSousCategorie, int limit, int offset) {
        return dsl.select(articleFields())
                .from(table("article"))
                .where(field("ref_categorie").eq(refCategorie))
                .and(field("ref_subcategorie").eq(refSousCategorie))
                .and(field("vendu").eq(0))
                .orderBy(field("id_article").desc())
                .limit(limit).offset(offset)
                .fetch()
                .map(this::toArticle);
    }

    public List<Article> findByCategorie(Integer refCategorie, int limit, int offset) {
        return dsl.select(articleFields())
                .from(table("article"))
                .where(field("ref_categorie").eq(refCategorie))
                .and(field("vendu").eq(0))
                .orderBy(field("id_article").desc())
                .limit(limit).offset(offset)
                .fetch()
                .map(this::toArticle);
    }

    public List<Article> search(String query, int limit) {
        String pattern = "%" + query + "%";
        return dsl.select(articleFields())
                .from(table("article"))
                .where(field("nom").like(pattern)
                        .or(field("marque").like(pattern))
                        .or(field("label").like(pattern)))
                .and(field("vendu").eq(0))
                .limit(limit)
                .fetch()
                .map(this::toArticle);
    }

    public List<Article> findActiveAuctions() {
        return dsl.select(articleFields())
                .from(table("article"))
                .where(field("enchere").eq(1))
                .and(field("vendu").eq(0))
                .orderBy(field("enchere_date_fin").asc())
                .fetch()
                .map(this::toArticle);
    }

    public List<Article> findByVendeur(Long refVendeur) {
        return dsl.select(articleFields())
                .from(table("article"))
                .join(table("met_en_vente"))
                .on(field("article.id_article").eq(field("met_en_vente.ref_article")))
                .where(field("met_en_vente.ref_vendeur").eq(refVendeur))
                .orderBy(field("article.id_article").desc())
                .fetch()
                .map(this::toArticle);
    }

    public Article findByIdArticle(Long id) {
        return dsl.select(
                        field("article.id_article").as("id_article"),
                        field("article.nom").as("nom"),
                        field("article.marque").as("marque"),
                        field("article.label").as("label"),
                        field("article.prix").as("prix"),
                        field("article.pochette").as("pochette"),
                        field("article.pochette2").as("pochette2"),
                        field("article.presound").as("presound"),
                        field("article.ref_genre").as("ref_genre"),
                        field("article.etat").as("etat"),
                        field("article.ref_categorie").as("ref_categorie"),
                        field("article.ref_subcategorie").as("ref_subcategorie"),
                        field("article.quantite").as("quantite"),
                        field("article.owned").as("owned"),
                        field("article.ref_statut").as("ref_statut"),
                        field("article.ref_depot").as("ref_depot"),
                        field("article.enchere").as("enchere"),
                        field("article.ref_condition_payement").as("ref_condition_payement"),
                        field("article.ref_mode_de_livraison").as("ref_mode_de_livraison"),
                        field("article.vendu").as("vendu"),
                        field("article.visites").as("visites"),
                        field("article.nbr_enchere").as("nbr_enchere"),
                        field("article.lang").as("lang"),
                        field("article.ref_canton").as("ref_canton"),
                        field("article.lieu").as("lieu"),
                        field("article.adresse").as("adresse"),
                        field("article.npa").as("npa"),
                        field("article.ref_pays").as("ref_pays"),
                        field("article.link_youtube").as("link_youtube"),
                        field("article.nb_cheveaux").as("nb_cheveaux"),
                        field("article.nb_cylindre").as("nb_cylindre"),
                        field("article.nb_km").as("nb_km"),
                        field("article.annee").as("annee"),
                        field("article.premiere_immatriculation").as("premiere_immatriculation"),
                        field("article.essence_ou_diesel").as("essence_ou_diesel"),
                        field("article.ref_boite_de_vitesse").as("ref_boite_de_vitesse"),
                        field("article.clima").as("clima"),
                        field("article.nb_piece").as("nb_piece"),
                        field("article.surface_habitable").as("surface_habitable"),
                        field("article.superficie_terrain").as("superficie_terrain"),
                        field("article.annee_construction").as("annee_construction"),
                        field("article.ref_location_ou_achat").as("ref_location_ou_achat"),
                        field("article.ref_departement").as("ref_departement"),
                        field("article.ref_cepage").as("ref_cepage"),
                        field("article.tx").as("tx"),
                        field("article.ref_type_de_vin").as("ref_type_de_vin"),
                        field("article.ref_pays_region_vin").as("ref_pays_region_vin"),
                        field("article.acteurs").as("acteurs"),
                        field("article.realisateur").as("realisateur"),
                        field("article.auteur").as("auteur"),
                        field("article.ref_type_de_jeux").as("ref_type_de_jeux"),
                        field("article.ref_type_ecran").as("ref_type_ecran"),
                        field("article.dimension").as("dimension"),
                        field("article.processeur").as("processeur"),
                        field("article.ram").as("ram"),
                        field("article.disque_dur").as("disque_dur"),
                        field("article.taille").as("taille"),
                        field("NULLIF(CAST(article.date AS CHAR), '0000-00-00 00:00:00')").as("date"),
                        field("NULLIF(CAST(article.enchere_date_debut AS CHAR), '0000-00-00 00:00:00')").as("enchere_date_debut"),
                        field("NULLIF(CAST(article.enchere_date_fin AS CHAR), '0000-00-00 00:00:00')").as("enchere_date_fin"),
                        field("met_en_vente.ref_vendeur").as("ref_vendeur"),       // ✅ virgule corrigée
                        field("a_paye.ref_acheteur").as("ref_acheteur")            // ✅
                )
                .from(table("article"))
                .join(table("met_en_vente"))
                .on(field("article.id_article").eq(field("met_en_vente.ref_article")))
                .leftJoin(table("a_paye"))                                         // ✅ LEFT JOIN car a_paye peut ne pas exister
                .on(field("article.id_article").eq(field("a_paye.ref_article")))
                .where(field("met_en_vente.ref_article").eq(id))
                .fetchOne(this::toArticle);
    }
    public Long insert(Article a) {
        var record = dsl.insertInto(table("article"))
                .set(field("marque"),                   a.getMarque()                  != null ? a.getMarque()                  : "")
                .set(field("nom"),                      a.getNom()                     != null ? a.getNom()                     : "")
                .set(field("label"),                    a.getLabel()                   != null ? a.getLabel()                   : "")
                .set(field("prix"),                     a.getPrix()                    != null ? a.getPrix()                    : 0.0)
                .set(field("pochette"),                 a.getPochette()                != null ? a.getPochette()                : "")
                .set(field("pochette2"),                a.getPochette2()               != null ? a.getPochette2()               : "")
                .set(field("presound"),                 a.getPresound()                != null ? a.getPresound()                : "")
                .set(field("ref_genre"),                a.getRefGenre()                != null ? a.getRefGenre()                : 0)
                .set(field("etat"),                     a.getEtat()                    != null ? a.getEtat()                    : 0)
                .set(field("ref_categorie"),            a.getRefCategorie())
                .set(field("ref_subcategorie"),         a.getRefSubcategorie())
                .set(field("quantite"),                 a.getQuantite()                != null ? a.getQuantite()                : 1)
                .set(field("owned"),                    a.getOwned()                   != null ? a.getOwned()                   : 0)
                .set(field("ref_statut"),               a.getRefStatut()               != null ? a.getRefStatut()               : 1)
                .set(field("ref_depot"),                a.getRefDepot()                != null ? a.getRefDepot()                : 9L)
                .set(field("ref_condition_payement"),   a.getRefConditionPayement())
                .set(field("ref_mode_de_livraison"),    a.getRefModeDeLivraison())
                .set(field("vendu"),                    0)
                .set(field("lang"),                     a.getLang()                    != null ? a.getLang()                    : "fr")
                .set(field("link_youtube"),             a.getLinkYoutube()             != null ? a.getLinkYoutube()             : "")
                .set(field("enchere"),                  a.getEnchere()                 != null ? a.getEnchere()                 : 0)
                .set(field("enchere_date_debut"),       a.getEnchereDateDebut()        != null ? a.getEnchereDateDebut()        : "")
                .set(field("enchere_date_fin"),         a.getEnchereDateFin()          != null ? a.getEnchereDateFin()          : "")
                .set(field("nb_cheveaux"),              a.getNbCheveaux())
                .set(field("nb_cylindre"),              a.getNbCylindre())
                .set(field("nb_km"),                    a.getNbKm())
                .set(field("annee"),                    a.getAnnee())
                .set(field("premiere_immatriculation"), a.getPremiereImmatriculation() != null ? a.getPremiereImmatriculation() : "")
                .set(field("essence_ou_diesel"),        a.getEssenceOuDiesel())
                .set(field("ref_boite_de_vitesse"),     a.getRefBoiteDeVitesse())
                .set(field("clima"),                    a.getClima()                   != null ? a.getClima()                   : (byte) 0)
                .set(field("ref_location_ou_achat"),    a.getRefLocationOuAchat())
                .set(field("ref_pays"),                 a.getRefPays())
                .set(field("ref_canton"),               a.getRefCanton())
                .set(field("ref_departement"),          a.getRefDepartement())
                .set(field("lieu"),                     a.getLieu()                    != null ? a.getLieu()                    : "")
                .set(field("adresse"),                  a.getAdresse()                 != null ? a.getAdresse()                 : "")
                .set(field("npa"),                      a.getNpa()                     != null ? a.getNpa()                     : "")
                .set(field("nb_piece"),                 a.getNbPiece())
                .set(field("surface_habitable"),        a.getSurfaceHabitable())
                .set(field("superficie_terrain"),       a.getSuperficieTerrain()       != null ? a.getSuperficieTerrain()       : "")
                .set(field("annee_construction"),       a.getAnneeConstruction()       != null ? a.getAnneeConstruction()       : "")
                .set(field("ref_pays_region_vin"),      a.getRefPaysRegionVin())
                .set(field("ref_cepage"),               a.getRefCepage())
                .set(field("ref_type_de_vin"),          a.getRefTypeDeVin())
                .set(field("tx"),                       a.getMillesime()               != null ? a.getMillesime()               : "")
                .set(field("acteurs"),                  a.getActeurs()                 != null ? a.getActeurs()                 : "")
                .set(field("realisateur"),              a.getRealisateur()             != null ? a.getRealisateur()             : "")
                .set(field("auteur"),                   a.getAuteur()                  != null ? a.getAuteur()                  : "")
                .set(field("ref_type_de_jeux"),         a.getRefTypeDeJeux())
                .set(field("ref_type_ecran"),           a.getRefTypeEcran())
                .set(field("dimension"),                a.getDimension())
                .set(field("processeur"),               a.getProcesseur())
                .set(field("ram"),                      a.getRam()                     != null ? a.getRam()                     : "")
                .set(field("disque_dur"),               a.getDisqueDur()               != null ? a.getDisqueDur()               : "")
                .set(field("taille"),                   a.getTaille()                  != null ? a.getTaille()                  : "")
                .returningResult(field("id_article"))
                .fetchOne();
        return record != null ? record.get(field("id_article"), Long.class) : null;
    }

    public void incrementVisites(Long idArticle) {
        dsl.update(table("article"))
                .set(field("visites", Long.class), field("visites", Long.class).add(1))
                .where(field("id_article").eq(idArticle))
                .execute();
    }

    public void markAsSold(Long idArticle) {
        dsl.update(table("article"))
                .set(field("vendu"), 1)
                .where(field("id_article").eq(idArticle))
                .execute();
    }


    // ── Mapper ────────────────────────────────────────────────────────────────
    private Article     toArticle(org.jooq.Record r) {
        Article a = new Article();

        if (r.field("ref_vendeur") != null) {
            a.setRefVendeur(r.get(field("ref_vendeur"), Integer.class)); //
        }
        if (r.field("ref_acheteur") != null) {
            a.setRefAcheteur(r.get(field("ref_acheteur"), Long.class)); //
        }

        if (r.field("pub") != null) {
            a.setPub(r.get(field("pub"), Integer.class)); //
        }

        a.setIdArticle(              r.get(field("id_article"),               Long.class));
        a.setAuteur(                 r.get(field("auteur"),                   String.class));
        a.setMarque(                 r.get(field("marque"),                   String.class));
        a.setNom(                    r.get(field("nom"),                      String.class));
        a.setLabel(                  r.get(field("label"),                    String.class));
        a.setPrix(                   r.get(field("prix"),                     Double.class));
        a.setPochette(               r.get(field("pochette"),                 String.class));
        a.setPochette2(               r.get(field("pochette2"),                 String.class));
        a.setPresound(               r.get(field("presound"),                 String.class));
        a.setRefGenre(               r.get(field("ref_genre"),                Integer.class));
        a.setEtat(                   r.get(field("etat"),                     Integer.class));
        a.setRefCategorie(           r.get(field("ref_categorie"),            Integer.class));
        a.setRefSubcategorie(        r.get(field("ref_subcategorie"),         Integer.class));
        a.setOwned(                  r.get(field("owned"),                    Integer.class));
        a.setRefStatut(              r.get(field("ref_statut"),               Integer.class));
        a.setRefDepot(               r.get(field("ref_depot"),                Long.class));
        a.setEnchere(                r.get(field("enchere"),                  Integer.class));
        a.setRefConditionPayement(   r.get(field("ref_condition_payement"),   Integer.class));
        if (r.field("ref_mode_de_livraison") != null) {
            a.setRefModeDeLivraison(r.get("ref_mode_de_livraison", Integer.class));
        }

        // ✅ Lecture sécurisée des dates via Object pour éviter la conversion jOOQ
        Object dateRaw  = r.get("date");
        a.setDate(dateRaw != null ? dateRaw.toString() : null);

        Object debutRaw = r.get("enchere_date_debut");
        a.setEnchereDateDebut(debutRaw != null ? debutRaw.toString() : null);

        Object finRaw   = r.get("enchere_date_fin");
        a.setEnchereDateFin(finRaw != null ? finRaw.toString() : null);

        a.setVendu(                  r.get(field("vendu"),                    Integer.class));
        a.setVisites(                r.get(field("visites"),                  Long.class));
        a.setNbrEnchere(             r.get(field("nbr_enchere"),              Long.class));
        a.setLang(                   r.get(field("lang"),                     String.class));
        a.setRefCanton(              r.get(field("ref_canton"),               Integer.class));
        a.setLieu(                   r.get(field("lieu"),                     String.class));
        a.setAdresse(                r.get(field("adresse"),                  String.class));
        a.setNpa(                    r.get(field("npa"),                      String.class));
        a.setRefPays(                r.get(field("ref_pays"),                 Integer.class));
        a.setQuantite(               r.get(field("quantite"),                 Integer.class));
        a.setLinkYoutube(            r.get(field("link_youtube"),             String.class));
        a.setNbCheveaux(             r.get(field("nb_cheveaux"),              Integer.class));
        a.setNbCylindre(             r.get(field("nb_cylindre"),              Integer.class));
        a.setNbKm(                   r.get(field("nb_km"),                    Integer.class));
        a.setAnnee(                  r.get(field("annee"),                    Integer.class));
        a.setPremiereImmatriculation(r.get(field("premiere_immatriculation"), String.class));
        a.setEssenceOuDiesel(        r.get(field("essence_ou_diesel"),        Integer.class));
        a.setRefBoiteDeVitesse(      r.get(field("ref_boite_de_vitesse"),     Integer.class));
        a.setClima(                  r.get(field("clima"),                    Byte.class));
        a.setNbPiece(                r.get(field("nb_piece"),                 Integer.class));
        a.setSurfaceHabitable(       r.get(field("surface_habitable"),        Integer.class));
        a.setSuperficieTerrain(      r.get(field("superficie_terrain"),       String.class));
        a.setAnneeConstruction(      r.get(field("annee_construction"),       String.class));
        a.setRefLocationOuAchat(     r.get(field("ref_location_ou_achat"),    Integer.class));
        a.setRefDepartement(         r.get(field("ref_departement"),          Integer.class));
        a.setRefCepage(              r.get(field("ref_cepage"),               Integer.class));
        a.setMillesime(              r.get(field("tx"),                       String.class));
        a.setRefTypeDeVin(           r.get(field("ref_type_de_vin"),          Integer.class));
        a.setRefPaysRegionVin(       r.get(field("ref_pays_region_vin"),      Integer.class));
        a.setActeurs(                r.get(field("acteurs"),                  String.class));
        a.setRealisateur(            r.get(field("realisateur"),              String.class));
        a.setRefTypeDeJeux(          r.get(field("ref_type_de_jeux"),         Integer.class));
        a.setRefTypeEcran(           r.get(field("ref_type_ecran"),           Integer.class));
        a.setDimension(              r.get(field("dimension"),                Integer.class));
        a.setProcesseur(             r.get(field("processeur"),               java.math.BigDecimal.class));
        a.setRam(                    r.get(field("ram"),                      String.class));
        a.setDisqueDur(              r.get(field("disque_dur"),               String.class));
        a.setTaille(                 r.get(field("taille"),                   String.class));
        return a;
    }

    public void cloturerEncheresExpirees() {
        dsl.update(table("article"))
                .set(field("vendu"), 1)
                .set(field("ref_statut"), 7) // statut = vendu aux enchères
                .where(field("enchere").eq(1))
                .and(field("vendu").eq(0))
                .and(field("enchere_date_fin").lessThan(LocalDateTime.now()))
                .execute();
    }
}
